package com.linecorp.bot.model.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RecipientType {
    USER(1);

    public final int code;

    RecipientType(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static RecipientType by(int code) {
        for (RecipientType recipientType : values()) {
            if (recipientType.code == code) {
                return recipientType;
            }
        }
        return null;
    }
}
