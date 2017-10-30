package com.linecorp.bot.model.richmenu;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class RichMenuListResponse {
    List<RichMenuResponse> richmenus;
}
