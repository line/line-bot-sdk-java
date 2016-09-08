package com.linecorp.bot.model.message;

import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.message.template.Template;

import lombok.Value;

@Value
@JsonTypeName("template")
public class TemplateMessage implements Message {
    private String altText;
    private Template template;
}
