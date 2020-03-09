package com.linecorp.bot.messagingapidemoapp.configuration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linecorp.bot.client.ChannelTokenSupplier;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.spring.boot.LineBotProperties;

import kotlin.Pair;
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

        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
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

        private static String buildBody(@Nullable RequestBody body) throws IOException {
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
        List<Pair<String, String>> headers;
        String body;
    }

    @NotNull
    private static List<Pair<String, String>> buildHeaders(Headers headers) {
        return headers.names()
                      .stream()
                      .flatMap(name -> headers.values(name)
                                              .stream()
                                              .map(value -> new Pair<>(name, value)))
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
        List<Pair<String, String>> headers;
        String body;
    }

    private static String buildBody(@Nullable ResponseBody body) throws IOException {
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
