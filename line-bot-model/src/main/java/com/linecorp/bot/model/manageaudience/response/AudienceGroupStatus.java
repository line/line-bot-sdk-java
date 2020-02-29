package com.linecorp.bot.model.manageaudience.response;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum AudienceGroupStatus {
    IN_PROGRESS,
    READY,
    FAILED,
    EXPIRED,
    @JsonEnumDefaultValue
    UNKNOWN
}
