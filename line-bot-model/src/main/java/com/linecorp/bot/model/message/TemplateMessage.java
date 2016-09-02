package com.linecorp.bot.model.message;

import com.linecorp.bot.model.message.template.Template;

import lombok.Value;

@Value
public class TemplateMessage implements Message {

    private static final String TYPE = "template";

    private String altText;
    private Template template;

    @Override
    public String getType() {
        return TYPE;
    }
}
