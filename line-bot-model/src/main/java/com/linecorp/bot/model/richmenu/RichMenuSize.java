package com.linecorp.bot.model.richmenu;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Value;

@Value
public class RichMenuSize {
    public static final RichMenuSize FULL = new RichMenuSize(2500, 1686);
    public static final RichMenuSize HALF = new RichMenuSize(2500, 843);

    /** Width of the rich menu. Must be 2500. */
    int width;
    /** Height of the rich menu. Possible values: 1686 or 843. */
    int height;

    @JsonCreator
    public RichMenuSize(final int width, final int height) {
        this.width = width;
        this.height = height;
    }
}
