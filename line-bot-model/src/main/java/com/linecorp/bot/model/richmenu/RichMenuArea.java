package com.linecorp.bot.model.richmenu;

import com.fasterxml.jackson.annotation.JsonCreator;

import com.linecorp.bot.model.action.Action;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
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
}
