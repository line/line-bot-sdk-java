/*
 * Copyright 2023 LINE Corporation
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

package com.linecorp.bot.spring.boot.handler.properties;

import java.net.URI;

import jakarta.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration for LineMessageHandler and EventMapping.
 */
@Validated
@ConfigurationProperties(prefix = "line.bot.handler")
public record HandlerProperties(
        /**
         * Flag to enable/disable LineMessageHandler and EventMapping.
         *
         * <p>Default: {@code true}
         */
        @DefaultValue("true")
        boolean enabled,
        /**
         * REST endpoint path of dispatcher.
         */
        @NotNull
        @DefaultValue("/callback")
        URI path

) {
}
