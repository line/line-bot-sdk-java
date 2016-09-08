package com.linecorp.bot.model.message;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.NonNull;
import lombok.Value;

@Value
@JsonTypeName("sticker")
public class StickerMessage implements Message {
    @NonNull
    private final String packageId;

    @NonNull
    private final String stickerId;

    public StickerMessage(String packageId, String stickerId) {
        this.packageId = packageId;
        this.stickerId = stickerId;
    }
}
