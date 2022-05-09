package com.linecorp.bot.model.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = DeliveryContext.DeliveryContextBuilder.class)
public class DeliveryContext {
    @JsonPOJOBuilder(withPrefix = "")
    public static class DeliveryContextBuilder {
        // Providing builder instead of public constructor. Class body is filled by lombok.
    }

    /**
     * Whether the webhook event is a redelivered one or not. The values of other properties such as webhookEventId and
     * timestamp remain the same for redelivered webhook events.
     */
    Boolean isRedelivery;
}
