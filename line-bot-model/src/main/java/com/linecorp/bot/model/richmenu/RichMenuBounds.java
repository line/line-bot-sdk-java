package com.linecorp.bot.model.richmenu;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Value;

@Value
public class RichMenuBounds {
    /** Horizontal position relative to the top-left corner of the area. */
    int x;

    /** Vertical position relative to the top-left corner of the area. */
    int y;

    /** Width of the area. */
    int width;

    /** Height of the area. */
    int height;

    @JsonCreator
    public RichMenuBounds(final int x, final int y, final int width, final int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
