/*
 * Copyright 2018 LINE Corporation
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

package com.linecorp.bot.liff;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.liff.LiffView.Type;
import com.linecorp.bot.liff.request.LiffAppAddRequest;
import com.linecorp.bot.liff.response.LiffAppAddResponse;
import com.linecorp.bot.liff.response.LiffAppsResponse;
import com.linecorp.bot.model.testutil.TestUtil;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LiffModelSerializeDeserializeTest {
    private static final LiffView LIFF_VIEW = new LiffView(Type.COMPACT, URI.create("https://example.com"));
    private static final LiffApp LIFF_APP = new LiffApp("liffid", LIFF_VIEW);
    private static final LiffAppsResponse LIFF_APPS_RESPONSE =
            new LiffAppsResponse(singletonList(LIFF_APP));
    private static final LiffAppAddResponse ADD_LIFF_APP_RESPONSE = new LiffAppAddResponse("liffId");
    private static final LiffAppAddRequest ADD_LIFF_APP_REQUEST = new LiffAppAddRequest(LIFF_VIEW);

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = TestUtil.objectMapperWithProductionConfiguration(false);
    }

    @Test
    public void testLiffView() {
        test(LIFF_VIEW, LiffView.class);
    }

    @Test
    public void testLiffApp() {
        test(LIFF_APP, LiffApp.class);
    }

    @Test
    public void testLiffAppsResponse() {
        test(LIFF_APPS_RESPONSE, LiffAppsResponse.class);
    }

    @Test
    public void testAddLiffAppRequest() {
        test(ADD_LIFF_APP_REQUEST, LiffAppAddRequest.class);
    }

    @Test
    public void testAddLiffAppResponse() {
        test(ADD_LIFF_APP_RESPONSE, LiffAppAddResponse.class);
    }

    ///////////////////////////////////////////////////////////////////

    private <T> void test(final T original, final Class<T> clazz) {
        final T reconstructed = serializeThenDeserialize(original, clazz);
        assertThat(reconstructed).isEqualTo(original);
    }

    @SneakyThrows
    private <T> T serializeThenDeserialize(final T original, final Class<T> clazz) {
        log.info("Original:      {}", original);
        final String asJson = objectMapper.writeValueAsString(original);
        log.info("AS JSON:       {}", asJson);
        final T reconstructed = objectMapper.readValue(asJson, clazz);
        log.info("Reconstructed: {}", reconstructed);

        return reconstructed;
    }
}
