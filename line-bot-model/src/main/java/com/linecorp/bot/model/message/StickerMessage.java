package com.linecorp.bot.model.message;

import lombok.NonNull;
import lombok.Value;

@Value
public class StickerMessage implements Message {

    public static final String TYPE = "sticker";

    @NonNull
    private final String packageId;

    @NonNull
    private final String stickerId;

    public StickerMessage(String packageId, String stickerId) {
        this.packageId = packageId;
        this.stickerId = stickerId;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
