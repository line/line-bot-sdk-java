package com.linecorp.bot.model.response;

/**
 * A status of current calculation.
 */
public enum Status {
    /**
     * You can get the number of messages.
     */
    Ready,
    /**
     * The message counting process for the date specified in {@code date} has not been completed yet.
     * Retry your request later. Normally, the counting process is completed within the next day.
     */
    Unready,
    /**
     * The date specified in date is earlier than March 31, 2018, when the operation of the counting system
     * started.
     */
    OutOfService
}
