package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities.goblin;

import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.PetHandler;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbilityRegistration;
import net.swofty.type.skyblockgeneric.utility.RarityValue;

import java.util.List;

import static net.swofty.commons.StringUtility.commaify;

@PetAbilityRegistration(pet = PetHandler.GOBLIN, minimumRarity = Rarity.LEGENDARY, order = 1,
        implemented = false, notImplementedReason = "awaits a Mines of Divan region (Dwarven Mines); region-gated MINING_SPREAD stat")
public final class FetidThiefAbility implements PetAbility {
    private static final RarityValue<Double> MINING_SPREAD_PER_LEVEL =
            new RarityValue<>(0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0);

    @Override
    public String getName() {
        return "Fetid Thief";
    }

    @Override
    public List<String> getDescription(SkyBlockItem pet) {
        Rarity rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);
        String value = commaify(MINING_SPREAD_PER_LEVEL.getForRarity(rarity) * level);

        return List.of(
                "<7>Gain <e>+" + value + " <stat:mining_spread> <7>while in the",
                "<2>Mines of Divan<7>."
        );
    }
}
