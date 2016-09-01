package com.linecorp.bot.model.message;

import java.util.List;

import com.linecorp.bot.model.action.Action;

import lombok.Value;

@Value
public class ImageMapMessage implements Message {

    public static final String TYPE = "imagemap";

    private String baseUrl;
    private String altText;
    private BaseSize baseSize;
    private List<Action> actions;

    @Override
    public String getType() {
        return TYPE;
    }

    @Value
    public static class BaseSize {
        private int height;
        private int width;
    }
}
