package net.swofty.type.skyblockgeneric.item.handlers.pet.abstr;

import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

/**
 * One-shot timed buff: a trigger calls {@link #applyBuff(SkyBlockItem)} and
 * {@code getStatistics} returns stats until the buff expires.
 * <p>
 * Use when one trigger enables a stat buff for a fixed duration. For stacking
 * combos, use {@link StackingBuffPetAbility}.
 */
public abstract class TimedBuffPetAbility implements PetAbility {
    private long buffUntil;

    @Override
    public final ItemStatistics getStatistics(SkyBlockPlayer player, SkyBlockItem pet) {
        if (buffUntil <= System.currentTimeMillis()) return ItemStatistics.empty();
        return computeStatistics(player, pet);
    }

    protected abstract long buffDuration(SkyBlockItem pet);

    protected abstract ItemStatistics computeStatistics(SkyBlockPlayer player, SkyBlockItem pet);

    protected final void applyBuff(SkyBlockItem pet) {
        buffUntil = System.currentTimeMillis() + buffDuration(pet);
    }
}
