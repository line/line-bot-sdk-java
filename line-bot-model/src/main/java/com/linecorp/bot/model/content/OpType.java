package com.linecorp.bot.model.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OpType {

    ADDED_AS_FRIEND(4),
    BLOCKED(8),
    ;

    public final int code;

    OpType(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static OpType by(int code) {
        for (OpType opType : values()) {
            if (opType.code == code) {
                return opType;
            }
        }
        return null;
    }
}
