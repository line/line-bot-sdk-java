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

import java.net.URL;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpRequest {
    private final Request request;

    HttpRequest(Request request) {
        this.request = request;
    }

    public Request toOkHttpRequest() {
        return this.request;
    }

    public HttpRequestBuilder newBuilder() {
        return new HttpRequestBuilder(this.request.newBuilder());
    }

    public String method() {
        return this.request.method();
    }

    public URL url() {
        return this.request.url().url();
    }

    public Map<String, List<String>> headers() {
        return this.request.headers().toMultimap();
    }

    public HttpRequestBody body() {
        RequestBody body = this.request.body();
        if (body == null) {
            return null;
        }
        return new HttpRequestBody(body);
    }

    public record HttpRequestBuilder(Request.Builder builder) {
        public HttpRequest build() {
            return new HttpRequest(this.builder.build());
        }

        public HttpRequestBuilder addHeader(String name, String value) {
            this.builder.addHeader(name, value);
            return this;
        }
    }
}
