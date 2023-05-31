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

package com.linecorp.bot.client.base;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.util.concurrent.CompletableFuture;

import org.bbottema.javasocksproxyserver.SocksServer;
import org.junit.jupiter.api.Test;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import okhttp3.Response;
import retrofit2.http.GET;

class ApiClientBuilderTest {
    @Test
    void basic() {
        WireMockServer wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());

        stubFor(get(urlPathTemplate("/")).willReturn(
                aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{\"message\":\"OK\"}")));

        ApiClientBuilder<MyClient> apiClientBuilder = new ApiClientBuilder<>(
                URI.create(wireMockServer.baseUrl()),
                MyClient.class,
                new AbstractExceptionBuilder<>(MyErrorResponse.class) {
                    @Override
                    protected IOException buildException(Response response, MyErrorResponse errorBody) {
                        return new MyClientException();
                    }
                });

        MyClient client = apiClientBuilder.build();
        assertThat(client.get().join().body().message).isEqualTo("OK");

        wireMockServer.stop();
    }

    @Test
    void socksProxy() {
        int socksPort = 9020;

        SocksServer socksServer = new SocksServer();
        socksServer.start(socksPort);

        WireMockServer wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());

        stubFor(get(urlPathTemplate("/")).willReturn(
                aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{\"message\":\"OK\"}")));

        ApiClientBuilder<MyClient> apiClientBuilder = new ApiClientBuilder<>(
                URI.create(wireMockServer.baseUrl()),
                MyClient.class,
                new AbstractExceptionBuilder<>(MyErrorResponse.class) {
                    @Override
                    protected IOException buildException(Response response, MyErrorResponse errorBody) {
                        return new MyClientException();
                    }
                });

        MyClient client = apiClientBuilder.proxy(new Proxy(
                        Proxy.Type.SOCKS,
                        new InetSocketAddress(socksPort)
                ))
                .build();
        assertThat(client.get().join().body().message).isEqualTo("OK");

        wireMockServer.stop();
    }

    @Test
    void httpProxy() {
        int httpProxyPort = 9080;

        HttpProxyServer proxyServer =
                DefaultHttpProxyServer.bootstrap()
                        .withPort(httpProxyPort)
                        .start();

        WireMockServer wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());

        stubFor(get(urlPathTemplate("/")).willReturn(
                aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{\"message\":\"OK\"}")));

        ApiClientBuilder<MyClient> apiClientBuilder = new ApiClientBuilder<>(
                URI.create(wireMockServer.baseUrl()),
                MyClient.class,
                new AbstractExceptionBuilder<>(MyErrorResponse.class) {
                    @Override
                    protected IOException buildException(Response response, MyErrorResponse errorBody) {
                        return new MyClientException();
                    }
                });

        MyClient client = apiClientBuilder.proxy(new Proxy(
                        Proxy.Type.HTTP,
                        new InetSocketAddress("127.0.0.1", httpProxyPort)
                ))
                .build();
        assertThat(client.get().join().body().message).isEqualTo("OK");

        wireMockServer.stop();
        proxyServer.stop();
    }

    public interface MyClient {
        @GET("/")
        CompletableFuture<Result<MyResponse>> get();
    }

    public record MyResponse(String message) {
    }

    public record MyErrorResponse() {

    }

    @SuppressWarnings("serial")
    public static class MyClientException extends IOException {
        public MyClientException() {
        }
    }
}
