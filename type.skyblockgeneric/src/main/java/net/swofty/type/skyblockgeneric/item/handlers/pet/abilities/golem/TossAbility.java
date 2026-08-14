package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities.golem;

import net.minestom.server.coordinate.Vec;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.PetHandler;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbilityRegistration;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetEvent;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetEventHandler;

import java.util.List;

@PetAbilityRegistration(pet = PetHandler.GOLEM, minimumRarity = Rarity.LEGENDARY, order = 2,
        implemented = false, notImplementedReason = "logic complete; no animation right now ;awaits dispatch(PetEvent.MeleeDamageDealt) + dispatch(PetEvent.RangedDamageDealt)")
public final class TossAbility implements PetAbility {
    private static final int HITS_REQUIRED = 5;
    private static final long COOLDOWN_MILLIS = 5_000;

    private int hits;
    private long lastToss;

    @Override
    public String getName() {
        return "Toss";
    }

    @Override
    public List<String> getDescription(SkyBlockItem pet) {
        return List.of(
                "<7>Every 5 hits, throw the enemy up into",
                "<7>the air and deal <a>5x <7>damage <8>(5s cooldown)<7>."
        );
    }

    @PetEventHandler
    public void onMeleeDamageDealt(PetEvent.MeleeDamageDealt event) {
        procToss(event);
    }

    @PetEventHandler
    public void onRangedDamageDealt(PetEvent.RangedDamageDealt event) {
        procToss(event);
    }

    private void procToss(PetEvent.DamageDealt event) {
        long now = System.currentTimeMillis();
        if (now - lastToss < COOLDOWN_MILLIS) return;
        if (++hits % HITS_REQUIRED != 0) return;

        lastToss = now;
        event.damage(event.damage() * 5);
        event.mob().setVelocity(new Vec(0, 1.5, 0));
    }
}
