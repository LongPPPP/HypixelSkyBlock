package net.swofty.type.skyblockgeneric.item.handlers.pet.abstr;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * FIFO window of event timestamps; each entry expires independently after a duration.
 * Expired entries are pruned lazily by {@link #active(long, long)}.
 */
public final class ProcWindow {
    private final Deque<Long> procs = new ArrayDeque<>();

    public void record(long now) {
        procs.addLast(now);
    }

    public int active(long now, long durationMs) {
        while (!procs.isEmpty() && procs.peekFirst() + durationMs <= now)
            procs.removeFirst();
        return procs.size();
    }
}
