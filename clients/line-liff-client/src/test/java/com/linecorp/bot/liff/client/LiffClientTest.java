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


package com.linecorp.bot.liff.client;

import static org.assertj.core.api.Assertions.assertThat;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.linecorp.bot.client.base.BlobContent;
import com.linecorp.bot.client.base.UploadFile;

import java.net.URI;

import java.util.Map;


import com.linecorp.bot.liff.model.AddLiffAppRequest;
import com.linecorp.bot.liff.model.AddLiffAppResponse;
import com.linecorp.bot.liff.model.GetAllLiffAppsResponse;
import com.linecorp.bot.liff.model.UpdateLiffAppRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.ocadotechnology.gembus.test.Arranger;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.github.tomakehurst.wiremock.WireMockServer;

/**
* API tests for LiffClient
*/
@Timeout(5)
public class LiffClientTest {
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private WireMockServer wireMockServer;
    private LiffClient api;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());


        api = LiffClient.builder("MY_OWN_TOKEN")
            .apiEndPoint(URI.create(wireMockServer.baseUrl()))
            .build();
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void addLIFFAppTest() {
        stubFor(post(urlPathTemplate("/liff/v1/apps")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            AddLiffAppRequest addLiffAppRequest = Arranger.some(AddLiffAppRequest.class);

        AddLiffAppResponse response = api.addLIFFApp(addLiffAppRequest).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void deleteLIFFAppTest() {
        stubFor(delete(urlPathTemplate("/liff/v1/apps/{liffId}")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String liffId = Arranger.some(String.class);

        api.deleteLIFFApp(liffId).join().body();

        // TODO: test validations
    }

    @Test
    public void getAllLIFFAppsTest() {
        stubFor(get(urlPathTemplate("/liff/v1/apps")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));


        GetAllLiffAppsResponse response = api.getAllLIFFApps().join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void updateLIFFAppTest() {
        stubFor(put(urlPathTemplate("/liff/v1/apps/{liffId}")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String liffId = Arranger.some(String.class);
            UpdateLiffAppRequest updateLiffAppRequest = Arranger.some(UpdateLiffAppRequest.class);

        api.updateLIFFApp(liffId, updateLiffAppRequest).join().body();

        // TODO: test validations
    }

}
