package net.swofty.type.skyblockgeneric.item.handlers.pet.abstr;

import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public interface PetAbility {
    String getName();

    List<String> getDescription(SkyBlockItem instance);

    /**
     * Computes the statistics granted by this pet ability.
     * Do NOT call {@code player.getStatistics().allStatistics()} (or anything that
     * transitively computes it) from this method. Pet abilities are evaluated inside
     * {@code PlayerStatistics#allStatistics()}, so doing recurses infinitely
     * (allStatistics → petStatistics → getStatistics → allStatistics).
     * If you need the player's stats as a scaling base, use
     * {@code player.getStatistics().allNonPetStatistics(null, null)} instead.
     */
    default ItemStatistics getStatistics(SkyBlockPlayer player, SkyBlockItem pet) {
        return ItemStatistics.empty();
    }
}
