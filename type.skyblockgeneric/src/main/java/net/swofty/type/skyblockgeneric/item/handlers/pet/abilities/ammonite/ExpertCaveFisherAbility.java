package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities.ammonite;

import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.PetHandler;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbilityRegistration;

import java.util.List;

import static net.swofty.commons.StringUtility.decimalify;

@PetAbilityRegistration(pet = PetHandler.AMMONITE, minimumRarity = Rarity.LEGENDARY, order = 1,
        implemented = false, notImplementedReason = "Crystal Hollows region not implemented")
public final class ExpertCaveFisherAbility implements PetAbility {
    private static final double PER_LEVEL = 0.005;

    @Override
    public String getName() {
        return "Expert Cave Fisher";
    }

    @Override
    public List<String> getDescription(SkyBlockItem pet) {
        Rarity rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);
        double value = PER_LEVEL * level;

        return List.of(
                "<7>Grants <9>+" + decimalify(value, 2) + " <stat:double_hook_chance> <7>for each",
                "<5>Heart of the Mountain <7>level",
                "<7>while in the <5>Crystal Hollows<7>."
        );
    }
}
