package com.linecorp.bot.model.richmenu;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class RichMenuBounds {
    /** Horizontal position relative to the top-left corner of the area. */
    int x;

    /** Vertical position relative to the top-left corner of the area. */
    int y;

    /** Width of the area. */
    int width;

    /** Height of the area. */
    int height;
}
