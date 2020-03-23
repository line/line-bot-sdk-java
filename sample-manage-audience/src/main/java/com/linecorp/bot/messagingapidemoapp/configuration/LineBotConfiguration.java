/*
 * Copyright 2020 LINE Corporation
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
 *
 */

package com.linecorp.bot.messagingapidemoapp.configuration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linecorp.bot.client.ChannelTokenSupplier;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.spring.boot.LineBotProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(LineBotProperties.class)
@AllArgsConstructor
public class LineBotConfiguration {
    private LineBotProperties lineBotProperties;

    @Bean
    public MyInterceptor myInterceptor() {
        return new MyInterceptor();
    }

    @Bean
    public LineMessagingClient lineMessagingClient(ChannelTokenSupplier channelTokenSupplier,
                                                   MyInterceptor myInterceptor) {
        return LineMessagingClient
                .builder(channelTokenSupplier)
                .apiEndPoint(lineBotProperties.getApiEndPoint())
                .blobEndPoint(lineBotProperties.getBlobEndPoint())
                .connectTimeout(lineBotProperties.getConnectTimeout())
                .readTimeout(lineBotProperties.getReadTimeout())
                .writeTimeout(lineBotProperties.getWriteTimeout())
                .additionalInterceptors(Collections.singletonList(myInterceptor))
                .build();
    }

    public static class MyInterceptor implements Interceptor {
        private List<ApiCallLog> logs = new ArrayList<>();

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            ApiCallRequest apiCallRequest = ApiCallRequest.create(request);
            Response response = chain.proceed(request);
            ApiCallResponse apiCallResponse = ApiCallResponse.create(response);
            ApiCallLog apiCallLog = new ApiCallLog(apiCallRequest, apiCallResponse);
            logs.add(apiCallLog);
            if (logs.size() > 40) {
                logs.remove(0);
            }
            return response;
        }

        public List<ApiCallLog> getLogs() {
            return Collections.unmodifiableList(logs);
        }
    }

    @Value
    public static class ApiCallLog {
        ApiCallRequest request;
        ApiCallResponse response;
    }

    @Builder
    @Getter
    public static class ApiCallRequest {
        public static ApiCallRequest create(Request request) throws IOException {
            return ApiCallRequest.builder()
                                 .method(request.method())
                                 .url(request.url().toString())
                                 .headers(buildHeaders(request.headers()))
                                 .body(buildBody(request.body()))
                                 .build();
        }

        private static String buildBody(RequestBody body) throws IOException {
            if (body == null) {
                return "<EMPTY>";
            }
            if (body.isDuplex()) {
                return "<DUPLEX>";
            }
            if (body.isOneShot()) {
                return "<ONESHOT>";
            }
            boolean result = false;
            MediaType mediaType = body.contentType();
            if (mediaType != null) {
                String type = mediaType.type();
                result = "text".equals(type) || "application".equals(type);
            }
            if (result) {
                Buffer buffer = new Buffer();
                body.writeTo(buffer);
                return buffer.readString(StandardCharsets.UTF_8);
            } else {
                return "<BINARY: " + body.contentType() + ">";
            }
        }

        String method;
        String url;
        List<HeaderEntry> headers;
        String body;
    }

    @Value
    public static class HeaderEntry {
        String name;
        String value;
    }

    private static List<HeaderEntry> buildHeaders(Headers headers) {
        return headers.names()
                      .stream()
                      .flatMap(name -> headers.values(name)
                                              .stream()
                                              .map(value -> new HeaderEntry(name, value)))
                      .collect(Collectors.toList());
    }

    @Builder
    @Getter
    public static class ApiCallResponse {
        public static ApiCallResponse create(Response response) throws IOException {
            return ApiCallResponse.builder()
                                  .code(response.code())
                                  .message(response.message())
                                  .headers(buildHeaders(response.headers()))
                                  .body(buildBody(response.body()))
                                  .build();
        }

        int code;
        String message;
        List<HeaderEntry> headers;
        String body;
    }

    private static String buildBody(ResponseBody body) throws IOException {
        if (body == null) {
            return "<EMPTY>";
        }

        MediaType contentType = body.contentType();
        if (contentType == null) {
            return "<BINARY>";
        }
        String type = contentType.type();
        Charset charset = contentType.charset();
        if (("text".equals(type) || "application".equals(type))) {
            if (charset == null) {
                if ("application".equals(type)) {
                    charset = StandardCharsets.UTF_8;
                } else {
                    return "<BINARY: " + contentType + ">";
                }
            }
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.getBuffer();
            return buffer.clone().readString(charset);
        } else {
            return "<BINARY: " + body.contentType() + ">";
        }
    }
}
