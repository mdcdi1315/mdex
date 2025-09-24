package com.github.mdcdi1315.mdex.api;

/**
 * Defines different teleporting states that can be returned by the Teleporter API.
 */
public enum TeleportRequestState
{
    /**
     * The teleporting request was completed synchronously.
     */
    COMPLETED,
    /**
     * The teleporting request was scheduled to run asynchronously later.
     */
    SCHEDULED,
    /**
     * The teleporting request could not be performed.
     */
    FAILED
}
