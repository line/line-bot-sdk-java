package com.linecorp.bot.model.response.manageaudience;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum AudienceGroupFailedType {
    AUDIENCE_GROUP_AUDIENCE_INSUFFICIENT,
    INTERNAL_ERROR,
    @JsonEnumDefaultValue
    UNKNOWN
}
