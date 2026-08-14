package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities.ghoul;

import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.type.skyblockgeneric.entity.mob.MobType;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.PetHandler;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbilityRegistration;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetEvent;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetEventHandler;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.utility.RarityValue;

import java.util.List;

import static net.swofty.commons.StringUtility.decimalify;

@PetAbilityRegistration(pet = PetHandler.GHOUL, minimumRarity = Rarity.EPIC, order = 0)
public final class UndeadSlayerAbility implements PetAbility {
    private static final RarityValue<Double> XP_MULTIPLIER_PER_LEVEL =
            new RarityValue<>(0.0, 0.0, 0.0, 0.005, 0.005, 0.0, 0.0);

    @Override
    public String getName() {
        return "Undead Slayer";
    }

    @Override
    public List<String> getDescription(SkyBlockItem pet) {
        Rarity rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);
        String multiplier = decimalify(1 + XP_MULTIPLIER_PER_LEVEL.getForRarity(rarity) * level, 2);

        return List.of(
                "<7>Gain <b>" + multiplier + "x <7>Combat XP against",
                "<a>Zombies<7>."
        );
    }

    @PetEventHandler
    public void onKill(PetEvent.KilledMob kill) {
        if (!kill.mob().getMobTypes().contains(MobType.UNDEAD)) return;

        Rarity rarity = kill.pet().getAttributeHandler().getRarity();
        int level = kill.pet().getAttributeHandler().getPetData().getAsLevel(rarity);
        double extra = kill.mob().getOtherLoot().getSkillXPAmount()
                * XP_MULTIPLIER_PER_LEVEL.getForRarity(rarity) * level;
        if (extra > 0) {
            kill.player().getSkills().increase(kill.player(), SkillCategories.COMBAT, extra);
        }
    }
}
