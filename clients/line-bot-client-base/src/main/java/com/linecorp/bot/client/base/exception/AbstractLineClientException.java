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

package com.linecorp.bot.client.base.exception;

import java.io.IOException;
import java.net.URL;

import okhttp3.Response;

@SuppressWarnings("serial")
public class AbstractLineClientException extends IOException {
    private final Response response;

    public AbstractLineClientException(Response response, String message, IOException ioException) {
        super("API returns error: code=" + response.code()
                + " requestUrl=" + response.request().url()
                + " requestId=" + response.headers().get("x-line-request-id")
                + " " + message, ioException);
        this.response = response;
    }

    public AbstractLineClientException(Response response, String message) {
        super("API returns error: code=" + response.code()
                + " requestUrl=" + response.request().url()
                + " requestId=" + response.headers().get("x-line-request-id")
                + " " + message);
        this.response = response;
    }

    public int getCode() {
        return response.code();
    }

    public URL getRequestUrl() {
        return response.request().url().url();
    }

    public String getRequestId() {
        return response.headers().get("x-line-request-id");
    }
}
