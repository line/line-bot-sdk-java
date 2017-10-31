package com.linecorp.bot.model.richmenu;

import com.fasterxml.jackson.annotation.JsonCreator;

import com.linecorp.bot.model.action.Action;

import lombok.Value;

@Value
public class RichMenuArea {
    /**
     * Object describing the boundaries of the area in pixels.
     */
    RichMenuBounds bounds;

    /**
     * Action performed when the area is tapped. See {@link Action} objects.
     *
     * Note: The label field is not supported for actions in rich menus.
     */
    Action action;

    @JsonCreator
    public RichMenuArea(final RichMenuBounds bounds, final Action action) {
        this.bounds = bounds;
        this.action = action;
    }
}
