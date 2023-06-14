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

import java.util.List;
import java.util.Map;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpResponse {
    private final Response response;

    public HttpResponse(Response response) {
        this.response = response;
    }

    /**
     * Internal use only.
     */
    public Response toOkHttpResponse() {
        return this.response;
    }

    public int code() {
        return this.response.code();
    }

    public String message() {
        return this.response.message();
    }

    public Map<String, List<String>> headers() {
        return this.response.headers().toMultimap();
    }

    public HttpResponseBody body() {
        ResponseBody body = this.response.body();
        if (body == null) {
            return null;
        }
        return new HttpResponseBody(body);
    }

    public HttpRequest request() {
        return new HttpRequest(this.response.request());
    }
}
