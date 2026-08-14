package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities.bal;

import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.PetHandler;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbilityRegistration;
import net.swofty.type.skyblockgeneric.utility.RarityValue;

import java.util.List;

import static net.swofty.commons.StringUtility.decimalify;

@PetAbilityRegistration(pet = PetHandler.BAL, minimumRarity = Rarity.EPIC, order = 0,
        implemented = false, notImplementedReason = "awaits a Magma Fields region (Crystal Hollows); region-gated PRISTINE stat")
public final class FurnaceAbility implements PetAbility {
    private static final RarityValue<Double> PRISTINE_PER_LEVEL =
            new RarityValue<>(0.0, 0.0, 0.0, 0.02, 0.03, 0.0, 0.0);

    @Override
    public String getName() {
        return "Furnace";
    }

    @Override
    public List<String> getDescription(SkyBlockItem pet) {
        Rarity rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);
        double value = PRISTINE_PER_LEVEL.getForRarity(rarity) * level;

        return List.of(
                "<7>Grants <5>+" + decimalify(value, 2) + " <stat:pristine> <7>while in the",
                "<c>Magma Fields<7>."
        );
    }
}
