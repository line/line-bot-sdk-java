package com.linecorp.bot.model.event.operation;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import com.linecorp.bot.model.event.message.TextMessageContent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
@JsonSubTypes(@JsonSubTypes.Type(TextMessageContent.class))
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
public abstract class OperationContent {
    private final String revision;

    protected OperationContent(String revision) {
        this.revision = revision;
    }
}
