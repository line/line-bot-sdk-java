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

package com.linecorp.bot.client.base.http;

import java.nio.charset.Charset;

import okhttp3.MediaType;

public class HttpMediaType {
    private final MediaType mediaType;

    public HttpMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String type() {
        return this.mediaType.type();
    }

    public Charset charset() {
        return this.mediaType.charset();
    }
}
