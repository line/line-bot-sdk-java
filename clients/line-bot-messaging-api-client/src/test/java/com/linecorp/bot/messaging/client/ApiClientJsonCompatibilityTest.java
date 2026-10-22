/*
 * Copyright 2026 LINE Corporation
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

package com.linecorp.bot.messaging.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.linecorp.bot.client.base.ApiClientBuilder;
import com.linecorp.bot.client.base.BlobContent;
import com.linecorp.bot.client.base.BotAwareJacksonConverter;
import com.linecorp.bot.client.base.LineClientJsonParseException;
import com.linecorp.bot.client.base.Result;
import com.linecorp.bot.client.base.UploadFile;
import com.linecorp.bot.jackson.ModelObjectMapper;
import com.linecorp.bot.messaging.model.ErrorResponse;
import com.linecorp.bot.messaging.model.GetWebhookEndpointResponse;
import com.linecorp.bot.messaging.model.TestWebhookEndpointRequest;
import com.linecorp.bot.messaging.model.TestWebhookEndpointResponse;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Okio;
import retrofit2.Converter;
import retrofit2.Retrofit;

class ApiClientJsonCompatibilityTest {
    private static final String WEBHOOK_ENDPOINT_PATH = "/v2/bot/channel/webhook/endpoint";

    private WireMockServer server;

    @BeforeEach
    void startServer() {
        server = new WireMockServer(wireMockConfig().dynamicPort());
        server.start();
    }

    @AfterEach
    void stopServer() {
        server.stop();
    }

    @Test
    void writesRequestAndIgnoresUnknownResponseProperty() {
        server.stubFor(post(urlEqualTo("/v2/bot/channel/webhook/test")).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withHeader("X-Line-Request-Id", "request-1")
                .withBody("{\"success\":true,\"statusCode\":200,\"futureField\":true}")));

        Result<TestWebhookEndpointResponse> result = newBuilder().build()
                .testWebhookEndpoint(
                        new TestWebhookEndpointRequest(URI.create("https://example.com")))
                .orTimeout(5, TimeUnit.SECONDS).join();

        assertThat(result.body().success()).isTrue();
        assertThat(result.body().statusCode()).isEqualTo(200);
        assertThat(result.requestId()).isEqualTo("request-1");
        server.verify(postRequestedFor(urlEqualTo("/v2/bot/channel/webhook/test"))
                .withRequestBody(equalTo("{\"endpoint\":\"https://example.com\"}")));
    }

    @Test
    void rejectsUnknownResponsePropertyWhenStrictParsingEnabled() {
        stubWebhookEndpointWithUnknownProperty();
        MessagingApiClient client = newBuilder().failOnUnknownProperties(true).build();

        assertThatThrownBy(() -> client.getWebhookEndpoint()
                .orTimeout(5, TimeUnit.SECONDS).join())
                .hasMessageContaining("futureField");
    }

    @Test
    void strictParsingEnabledAfterBuildRejectsUnknownPropertyOnFirstRequest() {
        stubWebhookEndpointWithUnknownProperty();

        ApiClientBuilder<MessagingApiClient> builder = newBuilder();
        MessagingApiClient client = builder.build();
        builder.failOnUnknownProperties(true);

        assertThatThrownBy(() -> client.getWebhookEndpoint()
                .orTimeout(5, TimeUnit.SECONDS).join())
                .hasMessageContaining("futureField");
    }

    @Test
    void strictParsingEnabledAfterFirstRequestStillIgnoresUnknownProperty() {
        stubWebhookEndpointWithUnknownProperty();

        ApiClientBuilder<MessagingApiClient> builder = newBuilder();
        MessagingApiClient client = builder.build();
        assertThat(client.getWebhookEndpoint().orTimeout(5, TimeUnit.SECONDS).join().body())
                .isEqualTo(expectedWebhookEndpoint());

        builder.failOnUnknownProperties(true);

        assertThat(client.getWebhookEndpoint().orTimeout(5, TimeUnit.SECONDS).join().body())
                .isEqualTo(expectedWebhookEndpoint());
    }

    @Test
    void mapsJsonErrorResponseToClientException() {
        server.stubFor(get(urlEqualTo(WEBHOOK_ENDPOINT_PATH)).willReturn(aResponse()
                .withStatus(400)
                .withBody("{\"message\":\"request rejected\"}")));

        assertThatThrownBy(() -> newBuilder().build().getWebhookEndpoint()
                .orTimeout(5, TimeUnit.SECONDS).join())
                .hasRootCauseInstanceOf(MessagingApiClientException.class)
                .hasMessageContaining("request rejected");
    }

    @Test
    void wrapsMalformedErrorResponseInJsonParseException() {
        server.stubFor(get(urlEqualTo(WEBHOOK_ENDPOINT_PATH)).willReturn(aResponse()
                .withStatus(400)
                .withBody("{\"message\":")));

        Throwable thrown = catchThrowable(() -> newBuilder().build().getWebhookEndpoint()
                .orTimeout(5, TimeUnit.SECONDS).join());

        assertThat(thrown).hasCauseInstanceOf(LineClientJsonParseException.class);
        assertThat(thrown.getCause().getCause()).isInstanceOf(IOException.class);
    }

    @Test
    void reportsMalformedSuccessResponseAsIOException() {
        server.stubFor(get(urlEqualTo(WEBHOOK_ENDPOINT_PATH)).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"endpoint\":")));

        assertThatThrownBy(() -> newBuilder().build().getWebhookEndpoint()
                .orTimeout(5, TimeUnit.SECONDS).join())
                .hasCauseInstanceOf(IOException.class);
    }

    @Test
    void reportsRequestSerializationFailureAsIOException() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("self", payload);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(server.baseUrl() + "/").build();
        Converter<?, RequestBody> converter = BotAwareJacksonConverter
                .create(ModelObjectMapper.createNewObjectMapper())
                .requestBodyConverter(Map.class, new Annotation[0], new Annotation[0], retrofit);

        @SuppressWarnings("unchecked")
        Converter<Object, RequestBody> objectConverter =
                (Converter<Object, RequestBody>) converter;
        assertThatThrownBy(() -> objectConverter.convert(payload))
                .isInstanceOf(IOException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "{\"endpoint\":\""})
    void propagatesOriginalIoExceptionFromResponseConverter(String prefix) {
        IOException failure = new SocketTimeoutException("response read timed out");
        Retrofit retrofit = new Retrofit.Builder().baseUrl(server.baseUrl() + "/").build();
        Converter<ResponseBody, ?> converter = BotAwareJacksonConverter
                .create(ModelObjectMapper.createNewObjectMapper())
                .responseBodyConverter(GetWebhookEndpointResponse.class,
                        new Annotation[0], retrofit);

        assertThatThrownBy(() -> converter.convert(bodyThatFailsOnRead(prefix, failure)))
                .isSameAs(failure);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "{\"message\":\""})
    void propagatesOriginalIoExceptionFromErrorResponseParsing(String prefix) {
        IOException failure = new SocketTimeoutException("error response read timed out");
        try (Response response = errorResponse(bodyThatFailsOnRead(prefix, failure))) {
            assertThatThrownBy(() -> new MessagingApiExceptionBuilder().build(response))
                    .isSameAs(failure);
        }
    }

    @Test
    void throwsIOExceptionWhenErrorResponseExceedsNestingLimit() {
        String json = "{\"future\":" + "[".repeat(1000) + "0" + "]".repeat(1000) + "}";

        try (Response response = errorResponse(
                ResponseBody.create(json, MediaType.get("application/json")))) {
            assertThatThrownBy(() -> new MessagingApiExceptionBuilder().build(response))
                    .isInstanceOf(IOException.class)
                    .isNotInstanceOf(LineClientJsonParseException.class);
        }
    }

    @Test
    void wrapsErrorPropertyLengthLimitFailureInJsonParseException() throws IOException {
        String json = "{\"message\":\"" + "x".repeat(20_100_000) + "\"}";

        try (Response response = errorResponse(
                ResponseBody.create(json, MediaType.get("application/json")))) {
            assertThat(new MessagingApiExceptionBuilder().build(response))
                    .isInstanceOf(LineClientJsonParseException.class)
                    .hasCauseInstanceOf(IOException.class);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"{\"message\":", "{\"message\":[]}"})
    void allowsSubclassToCatchInvalidErrorJsonAsIOException(String json) throws IOException {
        MessagingApiExceptionBuilder builder = new MessagingApiExceptionBuilder() {
            @Override
            protected ErrorResponse parseErrorBody(Response response) throws IOException {
                try {
                    return super.parseErrorBody(response);
                } catch (IOException e) {
                    return new ErrorResponse.Builder("fallback error").build();
                }
            }
        };

        try (Response response = errorResponse(
                ResponseBody.create(json, MediaType.get("application/json")))) {
            assertThat(builder.build(response))
                    .isInstanceOf(MessagingApiClientException.class)
                    .hasMessageContaining("fallback error");
        }
    }

    @Test
    void rejectsNullMapperWhenCreatingConverter() {
        assertThatThrownBy(() -> BotAwareJacksonConverter.create(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("mapper == null");
    }

    @Test
    void returnsBlobContentWithMimeType() throws Exception {
        server.stubFor(get(urlEqualTo("/v2/bot/message/message-1/content"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "image/png")
                        .withBody("raw content")));

        BlobContent body = newBlobClient().getMessageContent("message-1")
                .orTimeout(5, TimeUnit.SECONDS).join().body();

        assertThat(body.mimeType()).isEqualTo("image/png");
        assertThat(body.string()).isEqualTo("raw content");
    }

    @Test
    void uploadsRawContentWithoutJsonEncoding() {
        server.stubFor(post(urlEqualTo("/v2/bot/richmenu/rich-menu-1/content"))
                .willReturn(aResponse().withStatus(200)));
        byte[] content = "raw content".getBytes(StandardCharsets.UTF_8);

        newBlobClient().setRichMenuImage(
                        "rich-menu-1", UploadFile.fromByteArray(content, "text/plain"))
                .orTimeout(5, TimeUnit.SECONDS).join();

        server.verify(postRequestedFor(urlEqualTo("/v2/bot/richmenu/rich-menu-1/content"))
                .withRequestBody(equalTo("raw content")));
    }

    private void stubWebhookEndpointWithUnknownProperty() {
        server.stubFor(get(urlEqualTo(WEBHOOK_ENDPOINT_PATH)).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(
                        "{\"endpoint\":\"https://example.com\","
                                + "\"active\":true,\"futureField\":true}")));
    }

    private ResponseBody bodyThatFailsOnRead(String prefix, IOException failure) {
        InputStream input = new InputStream() {
            private final InputStream initialBytes =
                    new ByteArrayInputStream(prefix.getBytes(StandardCharsets.UTF_8));

            @Override
            public int read() throws IOException {
                int value = initialBytes.read();
                if (value != -1) {
                    return value;
                }
                throw failure;
            }
        };
        return ResponseBody.create(Okio.buffer(Okio.source(input)),
                MediaType.get("application/json"), -1);
    }

    private Response errorResponse(ResponseBody body) {
        return new Response.Builder()
                .request(new Request.Builder().url(server.baseUrl()).build())
                .protocol(Protocol.HTTP_1_1)
                .code(400)
                .message("Bad Request")
                .body(body)
                .build();
    }

    private GetWebhookEndpointResponse expectedWebhookEndpoint() {
        return new GetWebhookEndpointResponse(URI.create("https://example.com"), true);
    }

    private ApiClientBuilder<MessagingApiClient> newBuilder() {
        return MessagingApiClient.builder("token")
                .apiEndPoint(URI.create(server.baseUrl()));
    }

    private MessagingApiBlobClient newBlobClient() {
        return MessagingApiBlobClient.builder("token")
                .apiEndPoint(URI.create(server.baseUrl()))
                .build();
    }
}
