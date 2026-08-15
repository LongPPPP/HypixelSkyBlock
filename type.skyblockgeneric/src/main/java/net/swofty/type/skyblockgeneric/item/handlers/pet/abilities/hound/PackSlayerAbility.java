package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities.hound;

import net.minestom.server.entity.EntityType;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.PetHandler;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbilityRegistration;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetEvent;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetEventHandler;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;

import java.util.List;

import static net.swofty.commons.StringUtility.decimalify;

@PetAbilityRegistration(pet = PetHandler.HOUND, minimumRarity = Rarity.LEGENDARY, order = 2)
public final class PackSlayerAbility implements PetAbility {
    private static final double BASE = 1.0;
    private static final double PER_LEVEL = 0.005;

    @Override
    public String getName() {
        return "Pack Slayer";
    }

    @Override
    public List<String> getDescription(SkyBlockItem pet) {
        Rarity rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);
        String multiplier = decimalify(BASE + PER_LEVEL * level, 3);

        return List.of(
                "<7>Gain <b>+" + multiplier + "x <7>Combat XP against <a>Wolves<7>."
        );
    }

    @PetEventHandler
    public void onKill(PetEvent.KilledMob kill) {
        if (kill.mob().getEntityType() != EntityType.WOLF) return;

        Rarity rarity = kill.pet().getAttributeHandler().getRarity();
        int level = kill.pet().getAttributeHandler().getPetData().getAsLevel(rarity);
        double extra = kill.mob().getOtherLoot().getSkillXPAmount() * PER_LEVEL * level;
        if (extra > 0) {
            kill.player().getSkills().increase(kill.player(), SkillCategories.COMBAT, extra);
        }
    }
}
