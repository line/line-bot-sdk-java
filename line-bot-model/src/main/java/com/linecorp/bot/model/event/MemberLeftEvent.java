/*
 * Copyright (c) 2019 LINE Corporation. All rights reserved.
 * LINE Corporation PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.linecorp.bot.model.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.linecorp.bot.model.event.source.Source;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@JsonTypeName("memberLeft")
public class MemberLeftEvent implements Event {
    /**
     * JSON object which contains the source of the event.
     */
    private final Source source;

    /**
     * Time of the event.
     */
    private final Instant timestamp;


    /**
     * User ID of users who joined
     */
    private final LeftObject left;

    @JsonCreator
    public MemberLeftEvent(
            final Source source,
            final LeftObject left,
            final Instant timestamp) {
        this.source = source;
        this.left = left;
        this.timestamp = timestamp;
    }

    @Value
    public static class LeftObject {
        // User ID of users who joined
        List<Source> members;

        @JsonCreator
        public LeftObject(List<Source> members) {
            this.members = members;
        }
    }
}
