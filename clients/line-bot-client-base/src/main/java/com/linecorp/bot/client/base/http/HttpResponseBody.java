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

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class HttpResponseBody {
    private final ResponseBody body;

    public HttpResponseBody(ResponseBody body) {
        this.body = Objects.requireNonNull(body);
    }

    public HttpMediaType contentType() {
        return new HttpMediaType(this.body.contentType());
    }

    public byte[] readByteArray() throws IOException {
        BufferedSource source = this.body.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.getBuffer();
        return buffer.clone().readByteArray();
    }
}
