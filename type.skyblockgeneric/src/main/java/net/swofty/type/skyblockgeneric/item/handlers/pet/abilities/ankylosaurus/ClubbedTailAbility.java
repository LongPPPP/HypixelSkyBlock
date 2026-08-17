package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities.ankylosaurus;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.PetHandler;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbilityRegistration;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetEvent;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetEventHandler;
import net.swofty.type.skyblockgeneric.utility.RarityValue;

import java.util.List;

import static net.swofty.commons.StringUtility.decimalify;

@PetAbilityRegistration(pet = PetHandler.ANKYLOSAURUS, minimumRarity = Rarity.LEGENDARY, order = 2,
        implemented = false, notImplementedReason = "awaits decision on splash-damage semantics (raw mob.damage vs defense-reduced) + a mob-damage-debuff system for the -10% part")
public final class ClubbedTailAbility implements PetAbility {
    private static final int HITS_REQUIRED = 5;
    private static final double AOE_RADIUS = 5;
    private static final RarityValue<Double> DAMAGE_PERCENT_PER_LEVEL =
            new RarityValue<>(0.0, 0.0, 0.0, 0.0, 0.5, 0.0, 0.0);

    private int hits;

    @Override
    public String getName() {
        return "Clubbed Tail";
    }

    @Override
    public List<String> getDescription(SkyBlockItem pet) {
        Rarity rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);
        double value = DAMAGE_PERCENT_PER_LEVEL.getForRarity(rarity) * level;

        return List.of(
                "<7>Every 5th hit deals <a>" + decimalify(value, 1) + "% <7>of your",
                "<7>final damage to enemies within 5",
                "<7>blocks. Enemies hit deal 10% less",
                "<7>damage for 10s."
        );
    }

    @PetEventHandler
    public void onDamageDealt(PetEvent.DamageDealt event) {
        if (++hits % HITS_REQUIRED != 0) return;

        Rarity rarity = event.pet().getAttributeHandler().getRarity();
        int level = event.pet().getAttributeHandler().getPetData().getAsLevel(rarity);
        double percent = DAMAGE_PERCENT_PER_LEVEL.getForRarity(rarity) * level;
        double clubDamage = event.damage() * percent / 100;

        for (Entity entity : event.player().getInstance().getNearbyEntities(event.player().getPosition(), AOE_RADIUS)) {
            if (entity == event.player()) continue;
            if (!(entity instanceof SkyBlockMob mob)) continue;
            if (entity.getEntityType() == EntityType.PLAYER || entity.getEntityType() == EntityType.VILLAGER) continue;

            mob.damage(new Damage(DamageType.PLAYER_ATTACK, event.player(), event.player(),
                    event.player().getPosition(), (float) clubDamage));
        }
    }
}
