package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities.ankylosaurus;

import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.PetHandler;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbilityRegistration;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetEvent;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetEventHandler;

import java.util.List;

import static net.swofty.commons.StringUtility.decimalify;

@PetAbilityRegistration(pet = PetHandler.ANKYLOSAURUS, minimumRarity = Rarity.LEGENDARY, order = 2,
        implemented = false, notImplementedReason = "awaits DamageDealt dispatch in PlayerActionDamageMob + AoE/debuff hooks")
public final class ClubbedTailAbility implements PetAbility {
    private static final double PER_LEVEL = 0.5;

    @Override
    public String getName() {
        return "Clubbed Tail";
    }

    @Override
    public List<String> getDescription(SkyBlockItem pet) {
        Rarity rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);
        double value = PER_LEVEL * level;

        return List.of(
                "<7>Every 5th hit deals <a>" + decimalify(value, 1) + "% <7>of your",
                "<7>final damage to enemies within 5",
                "<7>blocks. Enemies hit deal 10% less",
                "<7>damage for 10s."
        );
    }

    @PetEventHandler
    public void onDamageDealt(PetEvent.DamageDealt event) {

    }
}
