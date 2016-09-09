package com.linecorp.bot.model.message;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

@Value
@JsonTypeName("video")
public class VideoMessage implements Message {
    private String originalContentUrl;
    private String previewImageUrl;
}
