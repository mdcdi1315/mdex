package com.github.mdcdi1315.mdex.api;

/**
 * Defines different teleporting states that can be returned by the Teleporter API.
 */
public enum TeleportRequestState
{
    /**
     * The teleporting request was completed synchronously.
     */
    COMPLETED(0),
    /**
     * The teleporting request was scheduled to run asynchronously later.
     */
    SCHEDULED(1),
    /**
     * The teleporting request could not be performed.
     */
    FAILED(-1),
    /**
     * The teleporting request was to be scheduled, but that is not possible.
     */
    SCHEDULING_FAILED(-2);

    private final byte value;

    TeleportRequestState(int value) {
        this.value = (byte) value;
    }

    /**
     * Gets a value whether the current state represents a failure.
     * @return A value whether this state value represents a failure.
     */
    public boolean HasFailed() {
        return value < 0;
    }

    public boolean EqualsWith(TeleportRequestState s) {
        return s != null && (s.value == this.value);
    }
}
