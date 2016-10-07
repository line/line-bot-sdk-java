/*
 * Copyright 2016 LINE Corporation
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

package com.linecorp.bot.model.message;

import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.message.template.Template;

import lombok.Value;

/**
 * Template messages are messages with predefined layouts which you can customize. There are three types of
 * templates available that can be used to interact with users through your bot.
 */
@Value
@JsonTypeName("template")
public class TemplateMessage implements Message {
    /**
     * Alternative text
     */
    private final String altText;

    /**
     * Object with the contents of the template.
     */
    private final Template template;
}
