package com.linecorp.bot.model.richmenu;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Value;

@Value
public class RichMenuResponse implements RichMenuCommonProperties {
    /** Rich menu ID */
    String richMenuId;

    /**
     * {@link RichMenuSize} object which contains the width and height of the rich menu displayed in the chat.
     *
     * <p>Rich menu images must be one of the following sizes: 2500x1686, 2500x843.
     */
    RichMenuSize size;

    /** true to display the rich menu by default. Otherwise, false. */
    boolean selected;

    /**
     * Name of the rich menu. This value can be used to help manage your rich menus and is not displayed to users.
     *
     * <p>Maximum of 300 characters.
     */
    String name;

    /**
     * Text displayed in the chat bar.
     *
     * <p>Maximum of 14 characters.
     */
    String chatBarText;

    /**
     * Array of {@link RichMenuArea} objects which define the coordinates and size of tappable areas.
     *
     * <p>Maximum of 20 area objects.
     */
    List<RichMenuArea> areas;

    @JsonCreator
    public RichMenuResponse(final String richMenuId, final RichMenuSize size, final boolean selected,
                            final String name, final String chatBarText, final List<RichMenuArea> areas) {
        this.richMenuId = richMenuId;
        this.size = size;
        this.selected = selected;
        this.name = name;
        this.chatBarText = chatBarText;
        this.areas = areas;
    }
}
