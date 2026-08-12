package net.swofty.type.skyblockgeneric.item.handlers.pet.abstr;

import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

/**
 * Stacking buff: each trigger adds a stack, {@code getStatistics} scales with the
 * stack count, and all stacks expire together after inactivity.
 * <p>
 * Use for combo/stack mechanics. For a one-shot timed buff, use
 * {@link TimedBuffPetAbility}.
 */
public abstract class StackingBuffPetAbility implements PetAbility {
    private int stacks;
    private long lastProc;

    @Override
    public final ItemStatistics getStatistics(SkyBlockPlayer player, SkyBlockItem pet) {
        if (stacks <= 0) return ItemStatistics.empty();
        if (System.currentTimeMillis() - lastProc > activeDuration(pet, stacks)) {
            stacks = 0;
            return ItemStatistics.empty();
        }
        return computeStatistics(player, pet, stacks);
    }

    protected abstract long activeDuration(SkyBlockItem pet, int stacks);

    protected abstract ItemStatistics computeStatistics(SkyBlockPlayer player, SkyBlockItem pet, int stacks);

    protected final void increment() {
        stacks++;
        lastProc = System.currentTimeMillis();
    }

    protected final int stacks() {
        return stacks;
    }
}
