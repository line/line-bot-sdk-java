package com.linecorp.bot.model.message;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.message.imagemap.ImageMapAction;
import com.linecorp.bot.model.message.imagemap.ImageMapBaseSize;

import lombok.Value;

@Value
@JsonTypeName("imagemap")
public class ImageMapMessage implements Message {
    private String baseUrl;
    private String altText;
    private ImageMapBaseSize baseSize;
    private List<ImageMapAction> actions;
}
