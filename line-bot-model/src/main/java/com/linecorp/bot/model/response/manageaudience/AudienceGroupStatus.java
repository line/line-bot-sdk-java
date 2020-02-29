package com.linecorp.bot.model.response.manageaudience;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum AudienceGroupStatus {
    IN_PROGRESS,
    READY,
    FAILED,
    EXPIRED,
    @JsonEnumDefaultValue
    UNKNOWN
}
