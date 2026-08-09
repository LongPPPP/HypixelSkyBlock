package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities.crow;

import net.swofty.type.skyblockgeneric.item.handlers.pet.PetHandler;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbilityRegistration;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetEvent;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetEventHandler;

import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.utility.RarityValue;

import java.util.Arrays;
import java.util.List;

import static net.swofty.commons.StringUtility.decimalify;

@PetAbilityRegistration(pet = PetHandler.CROW, minimumRarity = Rarity.COMMON)
public final class QuickHandsAbility implements PetAbility {
    private static final int BASE = 3;
    private static final RarityValue<Double> PER_LEVEL = new RarityValue<>(0.07, 0.07, 0.07, 0.12, 0.12, 0.0, 0.0);

    @Override
    public String getName() {
        return "Quick Hands";
    }

    @Override
    public List<String> getDescription(SkyBlockItem pet) {
        Rarity rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);
        double value = BASE + PER_LEVEL.getForRarity(rarity) * level;

        return Arrays.asList(
                "§7Lowers the cooldown of your §cWeapon",
                "§7abilities by §a+" + decimalify(value, 2) + "%§7."
        );
    }

    @PetEventHandler
    public void onAbilityCooldown(PetEvent.AbilityCooldown event) {
        Rarity rarity = event.pet().getAttributeHandler().getRarity();
        int level = event.pet().getAttributeHandler().getPetData().getAsLevel(rarity);
        double reduction = BASE + PER_LEVEL.getForRarity(rarity) * level;

        event.cooldown(event.cooldown() * (1 - reduction / 100));
    }
}
