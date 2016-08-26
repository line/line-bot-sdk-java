package com.linecorp.bot.model.v2.event.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("stickerId")
public class StickerMessageContent extends MessageContent {
    private final String packageId;
    private final String stickerId;

    @JsonCreator
    public StickerMessageContent(
            @JsonProperty("id") String id,
            @JsonProperty("packageId") String packageId,
            @JsonProperty("stickerId") String stickerId) {
        super(id);
        this.packageId = packageId;
        this.stickerId = stickerId;
    }
}
