package com.linecorp.bot.model.manageaudience.response;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum AudienceGroupFailedType {
    AUDIENCE_GROUP_AUDIENCE_INSUFFICIENT,
    INTERNAL_ERROR,
    @JsonEnumDefaultValue
    UNKNOWN
}
