package com.linecorp.bot.model.v2.message;

import com.linecorp.bot.model.v2.message.template.Template;

import lombok.Value;

@Value
public class TemplateMessage implements Message {

    public static final String TYPE = "template";

    private String altText;
    private Template template;

    @Override
    public String getType() {
        return TYPE;
    }
}
