package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities.dolphin;

import net.minestom.server.instance.Instance;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.PetHandler;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbilityRegistration;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.utility.RarityValue;

import java.util.Arrays;
import java.util.List;

import static net.swofty.commons.StringUtility.decimalify;

@PetAbilityRegistration(pet = PetHandler.DOLPHIN, minimumRarity = Rarity.COMMON)
public final class PodTacticsAbility implements PetAbility {
    private static final RarityValue<Double> PER_LEVEL = new RarityValue<>(0.06, 0.08, 0.08, 0.1, 0.1, 0.0, 0.0);

    @Override
    public String getName() {
        return "Pod Tactics";
    }

    @Override
    public List<String> getDescription(SkyBlockItem pet) {
        Rarity rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);
        double value = PER_LEVEL.getForRarity(rarity) * level;

        return Arrays.asList(
                "<7>Grants <stat:fishing_speed:+" + decimalify(value, 2) + ">,",
                "<7>for each player within <a>30",
                "<7>blocks, up to <a>5 <7>players."
        );
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockPlayer player, SkyBlockItem pet) {
        Rarity rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);
        double perPlayer = PER_LEVEL.getForRarity(rarity) * level;

        Instance instance = player.getInstance();
        int count = 0;
        if (instance != null) {
            count = Math.min((int) instance.getPlayers().stream()
                    .filter(p -> p != player && p.getPosition().distance(player.getPosition()) <= 30)
                    .count(), 5);
        }

        return ItemStatistics.builder()
                .withBase(ItemStatistic.FISHING_SPEED, perPlayer * count)
                .build();
    }
}
