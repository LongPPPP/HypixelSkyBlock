package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities.silverfish;

import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.PetHandler;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbilityRegistration;
import net.swofty.type.skyblockgeneric.utility.RarityValue;

import java.util.List;

import static net.swofty.commons.StringUtility.decimalify;

@PetAbilityRegistration(pet = PetHandler.SILVERFISH, minimumRarity = Rarity.COMMON, order = 0,
        implemented = false, notImplementedReason = "awaits a unified XP-modifier pipeline")
public final class MagneticAbility implements PetAbility {
    private static final RarityValue<Double> EXP_PER_LEVEL =
            new RarityValue<>(0.3, 0.4, 0.4, 0.5, 0.5, 0.0, 0.0);

    @Override
    public String getName() {
        return "Magnetic";
    }

    @Override
    public List<String> getDescription(SkyBlockItem pet) {
        Rarity rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);
        String exp = decimalify(EXP_PER_LEVEL.getForRarity(rarity) * level, 1);

        return List.of(
                "<7>Earn <a>+" + exp + "% <7>more Exp",
                "<7>when mining."
        );
    }
}
