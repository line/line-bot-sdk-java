/** Get event source. */
Source source();

/** Time of the event. */
Long timestamp();

/**
 * Channel state.
 * <dl>
 * <dt>active</dt>
 * <dd>The channel is active. You can send a reply message or push message from the bot server that received
 * this webhook event.</dd>
 * <dt>standby (under development)</dt>
 * <dd>The channel is waiting. The bot server that received this webhook event shouldn't send any messages.
 * </dd>
 * </dl>
 */
EventMode mode();

/**
 * Webhook Event ID. An ID that uniquely identifies a webhook event. This is a string in ULID format.
 */
String webhookEventId();

/**
 * Get delivery context.
 */
DeliveryContext deliveryContext();
