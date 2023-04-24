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

package com.linecorp.bot.messagingapidemoapp.configuration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linecorp.bot.client.base.channel.ChannelTokenSupplier;
import com.linecorp.bot.client.base.http.HttpChain;
import com.linecorp.bot.client.base.http.HttpInterceptor;
import com.linecorp.bot.client.base.http.HttpMediaType;
import com.linecorp.bot.client.base.http.HttpRequest;
import com.linecorp.bot.client.base.http.HttpRequestBody;
import com.linecorp.bot.client.base.http.HttpResponse;
import com.linecorp.bot.client.base.http.HttpResponseBody;
import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.linecorp.bot.spring.boot.core.properties.LineBotProperties;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(LineBotProperties.class)
public class LineBotConfiguration {
    private final LineBotProperties lineBotProperties;

    public LineBotConfiguration(LineBotProperties lineBotProperties) {
        this.lineBotProperties = lineBotProperties;
    }

    @Bean
    public MyInterceptor myInterceptor() {
        return new MyInterceptor();
    }

    @Bean
    public MessagingApiClient messagingApiClient(ChannelTokenSupplier channelTokenSupplier,
                                                 MyInterceptor myInterceptor) {
        return MessagingApiClient
                .builder(channelTokenSupplier)
                .apiEndPoint(lineBotProperties.apiEndPoint())
                .connectTimeout(lineBotProperties.connectTimeout())
                .readTimeout(lineBotProperties.readTimeout())
                .writeTimeout(lineBotProperties.writeTimeout())
                .addInterceptor(myInterceptor)
                .build();
    }

    public static class MyInterceptor implements HttpInterceptor {
        private final List<ApiCallLog> logs = new ArrayList<>();

        @Override
        public HttpResponse intercept(HttpChain chain) throws IOException {
            HttpRequest request = chain.request();
            ApiCallRequest apiCallRequest = ApiCallRequest.create(request);
            HttpResponse response = chain.proceed(request);
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

    public record ApiCallLog(
            ApiCallRequest request,
            ApiCallResponse response
    ) {
    }

    public record ApiCallRequest(
            String method,
            String url,
            List<HeaderEntry> headers,
            String body
    ) {
        public static ApiCallRequest create(HttpRequest request) throws IOException {
            return new ApiCallRequest(
                    request.method(),
                    request.url().toString(),
                    buildHeaders(request.headers()),
                    buildBody(request.body())
            );
        }

        private static String buildBody(HttpRequestBody body) throws IOException {
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
            HttpMediaType mediaType = body.contentType();
            if (mediaType != null) {
                String type = mediaType.type();
                result = "text".equals(type) || "application".equals(type);
            }
            if (result) {
                return new String(body.readByteArray(), StandardCharsets.UTF_8);
            } else {
                return "<BINARY: " + body.contentType() + ">";
            }
        }
    }

    public record HeaderEntry(
            String name,
            String value
    ) {
    }

    private static List<HeaderEntry> buildHeaders(Map<String, List<String>> headers) {
        return headers.keySet()
                .stream()
                .flatMap(name -> headers.get(name)
                        .stream()
                        .map(value -> new HeaderEntry(name, value)))
                .collect(Collectors.toList());
    }

    public record ApiCallResponse(
            int code,
            String message,
            List<HeaderEntry> headers,
            String body
    ) {
        public static ApiCallResponse create(HttpResponse response) throws IOException {
            return new ApiCallResponse(
                    response.code(),
                    response.message(),
                    buildHeaders(response.headers()),
                    buildBody(response.body())
            );
        }
    }

    private static String buildBody(HttpResponseBody body) throws IOException {
        if (body == null) {
            return "<EMPTY>";
        }

        HttpMediaType contentType = body.contentType();
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
            return new String(body.readByteArray(), charset);
        } else {
            return "<BINARY: " + body.contentType() + ">";
        }
    }
}
