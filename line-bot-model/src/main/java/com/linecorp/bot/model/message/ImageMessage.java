package com.linecorp.bot.model.message;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

@Value
@JsonTypeName("image")
public class ImageMessage implements Message {
    private String originalContentUrl;
    private String previewImageUrl;
}
