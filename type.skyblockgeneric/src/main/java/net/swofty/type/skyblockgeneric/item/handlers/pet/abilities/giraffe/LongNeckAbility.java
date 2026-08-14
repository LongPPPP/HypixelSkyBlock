package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities.giraffe;

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

@PetAbilityRegistration(pet = PetHandler.GIRAFFE, minimumRarity = Rarity.LEGENDARY, order = 2,
        implemented = false, notImplementedReason = "logic complete; awaits dispatch(PetEvent.MeleeDamageDealt) in the player damage pipeline")
public final class LongNeckAbility implements PetAbility {
    private static final double BASE_DAMAGE = 50;
    private static final RarityValue<Double> DAMAGE_PER_LEVEL =
            new RarityValue<>(0.0, 0.0, 0.0, 0.0, 0.5, 0.0, 0.0);

    @Override
    public String getName() {
        return "Long Neck";
    }

    @Override
    public List<String> getDescription(SkyBlockItem pet) {
        Rarity rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);
        String value = decimalify(BASE_DAMAGE + DAMAGE_PER_LEVEL.getForRarity(rarity) * level, 2);

        return List.of(
                "<7>Increases your melee damage by <c>" + value + "%<7>",
                "<7>if you are more than 3 blocks away from",
                "<7>the target."
        );
    }

    @PetEventHandler
    public void onDamageDealt(PetEvent.MeleeDamageDealt event) {
        double distance = event.player().getPosition().distance(event.mob().getPosition());
        if (distance <= 3) return;

        Rarity rarity = event.pet().getAttributeHandler().getRarity();
        int level = event.pet().getAttributeHandler().getPetData().getAsLevel(rarity);
        double percent = (BASE_DAMAGE + DAMAGE_PER_LEVEL.getForRarity(rarity) * level) / 100;
        event.damage(event.damage() * (1 + percent));
    }
}
