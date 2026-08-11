package net.swofty.type.skyblockgeneric.item.handlers.pet;

import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.PetComponent;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbilityRegistration;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetEvent;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetEventHandler;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PetAbilityRegistry {
    private static final Map<PetAbility, AbilityEntry> BY_ABILITY = new IdentityHashMap<>();
    private static final Map<PetHandler, List<AbilityEntry>> ABILITIES = load();
    private static final Map<Class<?>, Map<Class<? extends PetEvent>, List<Method>>> HANDLERS = new ConcurrentHashMap<>();

    private PetAbilityRegistry() {
    }

    public static List<PetAbility> getAbilities(SkyBlockItem item) {
        PetComponent component = item.getComponent(PetComponent.class);
        return getAbilitiesFor(PetHandler.valueOf(component.getHandlerId().toUpperCase()),
                item.getAttributeHandler().getRarity());
    }

    public static List<PetAbility> getAbilitiesFor(PetHandler pet, Rarity rarity) {
        return ABILITIES.getOrDefault(pet, List.of())
                .stream()
                .filter(e -> rarity.isAtLeast(e.minimumRarity))
                .map(AbilityEntry::ability)
                .toList();
    }

    public static @Nullable String notImplementedLine(PetAbility ability) {
        AbilityEntry entry = BY_ABILITY.get(ability);
        if (entry == null || entry.implemented()) return null;
        String reason = entry.notImplementedReason();
        return "<c>⚠ <l>NOT IMPLEMENTED<r><c>" + (reason.isEmpty() ? "" : " — " + reason);
    }

    public static void invoke(PetAbility ability, PetEvent event) {
        for (Method handler : HANDLERS.computeIfAbsent(ability.getClass(), PetAbilityRegistry::buildHandlers)
                .getOrDefault(event.getClass(), List.of())) {
            try {
                handler.invoke(ability, event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("Failed to invoke " + handler + " for " + event.getClass().getSimpleName(), e);
            }
        }
    }

    private static Map<PetHandler, List<AbilityEntry>> load() {
        Map<PetHandler, List<AbilityEntry>> registry = new EnumMap<>(PetHandler.class);
        for (Class<?> clazz : new Reflections("net.swofty.type.skyblockgeneric.item.handlers.pet.abilities")
                .getTypesAnnotatedWith(PetAbilityRegistration.class)) {
            PetAbilityRegistration meta = clazz.getAnnotation(PetAbilityRegistration.class);
            PetAbility ability = instantiate(clazz);
            AbilityEntry entry = new AbilityEntry(ability, meta.minimumRarity(), meta.order(),
                    meta.implemented(), meta.notImplementedReason());
            registry.computeIfAbsent(meta.pet(), _ -> new ArrayList<>()).add(entry);
            BY_ABILITY.put(ability, entry);
        }
        registry.values().forEach(list -> list.sort(
                Comparator.comparingInt((AbilityEntry e) -> e.minimumRarity().ordinal())
                        .thenComparingInt(AbilityEntry::order)));
        return Map.copyOf(registry);
    }

    private static PetAbility instantiate(Class<?> clazz) {
        try {
            return (PetAbility) clazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Pet ability " + clazz.getName() + " must have a public no-arg constructor", e);
        }
    }

    private static Map<Class<? extends PetEvent>, List<Method>> buildHandlers(Class<?> clazz) {
        Map<Class<? extends PetEvent>, List<Method>> handlers = new HashMap<>();
        for (Method method : clazz.getDeclaredMethods()) {
            PetEventHandler meta = method.getAnnotation(PetEventHandler.class);
            if (meta == null) continue;
            if (!Modifier.isPublic(method.getModifiers()) || method.getReturnType() != void.class
                    || method.getParameterCount() != 1
                    || !PetEvent.class.isAssignableFrom(method.getParameterTypes()[0])) {
                throw new IllegalStateException("@PetEventHandler " + method
                        + " must be a public void method with exactly one PetEvent parameter");
            }
            handlers.computeIfAbsent(method.getParameterTypes()[0].asSubclass(PetEvent.class), _ -> new ArrayList<>())
                    .add(method);
        }
        handlers.values().forEach(list -> list.sort(
                Comparator.comparingInt((Method m) -> m.getAnnotation(PetEventHandler.class).order())
                        .thenComparing(Method::getName)));
        return handlers;
    }

    private record AbilityEntry(PetAbility ability, Rarity minimumRarity, int order,
                                boolean implemented, String notImplementedReason) {
    }
}
