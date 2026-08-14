package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities.flying_fish;

import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.PetHandler;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbilityRegistration;
import net.swofty.type.skyblockgeneric.utility.RarityValue;

import java.util.List;

import static net.swofty.commons.StringUtility.decimalify;

@PetAbilityRegistration(pet = PetHandler.FLYING_FISH, minimumRarity = Rarity.MYTHIC, order = 5,
        implemented = false, notImplementedReason = "awaits an EnchantmentProc event + a Flash enchantment system")
public final class RapidDecayAbility implements PetAbility {
    private static final RarityValue<Double> PER_LEVEL =
            new RarityValue<>(0.0, 0.0, 0.0, 0.0, 0.0, 0.5, 0.0);

    @Override
    public String getName() {
        return "Rapid Decay";
    }

    @Override
    public List<String> getDescription(SkyBlockItem pet) {
        Rarity rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);
        double value = PER_LEVEL.getForRarity(rarity) * level;

        return List.of(
                "<7>Increases the chance to",
                "<7>activate the <d><l>Flash",
                "<d><l>Enchantment<r><a> by " + decimalify(value, 2) + "%<7>."
        );
    }
}
