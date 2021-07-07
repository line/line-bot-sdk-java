/*
 * Copyright 2021 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.bot.model.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

/**
 * When this action is tapped, switch between rich menus and a postback event including the rich menu alias ID
 * selected by the user and the specified string in the data field is returned via webhook.
 *
 * <p>This action can be configured only with rich menus.
 */
@Value
@JsonTypeName("richmenuswitch")
@JsonInclude(Include.NON_NULL)
public class RichMenuSwitchAction implements Action {
    /**
     * Label for the action.
     *
     * <p>Max: 20 characters
     */
    String label;

    /**
     * String returned via webhook in the postback.data property of the postback event.
     *
     * <p>Max: 300 characters
     */
    String data;

    /**
     * RichMenuAliasId to switch rich menu to.
     */
    String richMenuAliasId;

    /**
     * Create new instance.
     *
     * @param label Label for the action. Max: 20 characters.
     * @param data String returned via webhook in the postback.data property of the postback event.
     *         Max: 300 characters.
     * @param richMenuAliasId RichMenuAliasId to switch rich menu to.
     */
    @JsonCreator
    public RichMenuSwitchAction(
            @JsonProperty("label") String label,
            @JsonProperty("data") String data,
            @JsonProperty("richMenuAliasId") String richMenuAliasId) {
        this.label = label;
        this.data = data;
        this.richMenuAliasId = richMenuAliasId;
    }

    public RichMenuSwitchAction(String data, String richMenuAliasId) {
        this(null, data, richMenuAliasId);
    }
}
