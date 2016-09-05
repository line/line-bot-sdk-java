package com.linecorp.bot.model.message;

import lombok.Value;

@Value
public class ImageMessage implements Message {
    private static final String TYPE = "image";

    private String originalContentUrl;
    private String previewImageUrl;

    @Override
    public String getType() {
        return TYPE;
    }
}
