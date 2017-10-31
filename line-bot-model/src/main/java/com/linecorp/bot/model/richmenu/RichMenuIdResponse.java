package com.linecorp.bot.model.richmenu;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Value;

@Value
public class RichMenuIdResponse {
    String richMenuId;

    @JsonCreator
    public RichMenuIdResponse(final String richMenuId) {
        this.richMenuId = richMenuId;
    }
}
