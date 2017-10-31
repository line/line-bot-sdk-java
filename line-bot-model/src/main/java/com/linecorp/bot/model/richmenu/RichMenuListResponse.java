package com.linecorp.bot.model.richmenu;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class RichMenuListResponse {
    @JsonProperty("richmenus")
    List<RichMenuResponse> richMenus;

    @JsonCreator
    public RichMenuListResponse(final List<RichMenuResponse> richMenus) {
        this.richMenus = richMenus;
    }
}
