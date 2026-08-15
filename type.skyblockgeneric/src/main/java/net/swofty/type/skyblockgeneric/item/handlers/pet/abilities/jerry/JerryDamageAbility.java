package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities.jerry;

import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.PetHandler;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbilityRegistration;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetEvent;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetEventHandler;

import java.util.List;

@PetAbilityRegistration(pet = PetHandler.JERRY, minimumRarity = Rarity.COMMON, order = 0,
        implemented = false, notImplementedReason = "awaits dispatch(PetEvent.DamageDealt)")
public final class JerryDamageAbility implements PetAbility {

    @Override
    public String getName() {
        return "Jerry";
    }

    @Override
    public List<String> getDescription(SkyBlockItem pet) {
        return List.of(
                "<7>Gain <a>50% <7>chance to deal",
                "<7>your regular damage."
        );
    }

    @PetEventHandler
    public void onDamageDealt(PetEvent.DamageDealt event) {
    }
}
