package com.linecorp.bot.model.message;

import java.util.List;

import com.linecorp.bot.model.message.imagemap.ImageMapAction;
import com.linecorp.bot.model.message.imagemap.ImageMapBaseSize;

import lombok.Value;

@Value
public class ImageMapMessage implements Message {

    private static final String TYPE = "imagemap";

    private String baseUrl;
    private String altText;
    private ImageMapBaseSize baseSize;
    private List<ImageMapAction> actions;

    @Override
    public String getType() {
        return TYPE;
    }

}
