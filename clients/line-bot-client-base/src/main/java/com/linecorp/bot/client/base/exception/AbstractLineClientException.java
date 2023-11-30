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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import okhttp3.Headers;
import okhttp3.Response;

@SuppressWarnings("serial")
public class AbstractLineClientException extends IOException {
    private final Response response;

    public AbstractLineClientException(Response response, String message, IOException ioException) {
        super("API returns error: code=" + response.code()
                + " requestUrl=" + response.request().url()
                + headerInfo(response.headers())
                + " " + message, ioException);
        this.response = response;
    }

    public AbstractLineClientException(Response response, String message) {
        super("API returns error: code=" + response.code()
                + " requestUrl=" + response.request().url()
                + headerInfo(response.headers())
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

    public String getHeader(String name) {
        return response.headers().get(name);
    }

    private static String headerInfo(Headers headers) {
        String headerInfo = StreamSupport.stream(headers.spliterator(), false)
                .filter(it -> it.getFirst().startsWith("x-line-"))
                .sorted((a, b) -> a.getFirst().compareTo(b.getSecond()))
                .map(it -> it.getFirst() + "=" + it.getSecond())
                .collect(Collectors.joining(" "));
        if (headerInfo.isEmpty()) {
            return "";
        } else {
            return " " + headerInfo;
        }
    }
}
