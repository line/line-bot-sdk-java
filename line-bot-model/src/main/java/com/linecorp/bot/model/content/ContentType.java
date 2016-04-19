package com.linecorp.bot.model.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ContentType {

    TEXT(1),

    IMAGE(2),

    VIDEO(3),

    AUDIO(4),

    LOCATION(7),

    STICKER(8),

    CONTACT(10),

    RICH_MESSAGE(12),
    ;

    public final int code;

    ContentType(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static ContentType by(int code) {
        for (ContentType contentType : values()) {
            if (contentType.code == code) {
                return contentType;
            }
        }
        return null;
    }
}
