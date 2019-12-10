/*
 * Copyright 2018 LINE Corporation
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

package com.linecorp.bot.client;

import java.net.URI;

/**
 * Common constant holder.
 */
public enum LineClientConstants {
    /* Only public static final fields in this enum. */;
    public static final URI DEFAULT_API_END_POINT = URI.create("https://api.line.me/");
    public static final URI DEFAULT_BLOB_END_POINT = URI.create("https://api-data.line.me/");
    public static final long DEFAULT_CONNECT_TIMEOUT_MILLIS = 10_000;
    public static final long DEFAULT_READ_TIMEOUT_MILLIS = 10_000;
    public static final long DEFAULT_WRITE_TIMEOUT_MILLIS = 10_000;
}
