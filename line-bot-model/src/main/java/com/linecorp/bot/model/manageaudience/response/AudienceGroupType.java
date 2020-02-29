package com.linecorp.bot.model.manageaudience.response;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum AudienceGroupType {
    UPLOAD,
    CLICK,
    IMP,
    @JsonEnumDefaultValue
    UNKNOWN // Messaging API may implement new audience group type in the future!
}
