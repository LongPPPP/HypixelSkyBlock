package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities.skeleton;

import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.PetHandler;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbilityRegistration;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetEvent;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetEventHandler;
import net.swofty.type.skyblockgeneric.utility.RarityValue;

import java.util.List;

import static net.swofty.commons.StringUtility.decimalify;

@PetAbilityRegistration(pet = PetHandler.SKELETON, minimumRarity = Rarity.RARE, order = 1,
        implemented = false, notImplementedReason = "awaits dispatch(PetEvent.RangedDamageDealt)")
public final class ComboAbility implements PetAbility {
    private static final RarityValue<Double> STACKS_PER_LEVEL =
            new RarityValue<>(0.0, 0.0, 0.15, 0.17, 0.2, 0.0, 0.0);

    @Override
    public String getName() {
        return "Combo";
    }

    @Override
    public List<String> getDescription(SkyBlockItem pet) {
        Rarity rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);
        String stacks = decimalify(STACKS_PER_LEVEL.getForRarity(rarity) * level, 2);

        return List.of(
                "<7>Gain a combo stack for every bow hit",
                "<7>granting +<a>3 <stat:strength><7>. Max <a>" + stacks + " stacks<7>,",
                "<7>stacks disappear after <a>8 <7>seconds."
        );
    }

    @PetEventHandler
    public void onRangedDamageDealt(PetEvent.RangedDamageDealt event) {

    }
}
