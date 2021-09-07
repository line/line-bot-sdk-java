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
 */

package com.linecorp.bot.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.net.URI;

import org.junit.jupiter.api.Test;

public class LineClientBuildersTest {
    @Test
    public void testLineBlobClientBuilder() throws Exception {
        // Do
        final LineBlobClientBuilder defaultBuilder = new LineBlobClientBuilder();
        final Field field = defaultBuilder.getClass().getDeclaredField("apiEndPoint");
        field.setAccessible(true);
        final Object apiEndPoint = field.get(defaultBuilder);

        // Verify
        assertThat(apiEndPoint)
                .isEqualTo(URI.create("https://api-data.line.me/"));
    }

    @Test
    public void testLineMessagingClientBuilder() throws Exception {
        // Do
        final LineMessagingClientBuilder defaultBuilder = new LineMessagingClientBuilder();
        final Field field = defaultBuilder.getClass().getDeclaredField("apiEndPoint");
        field.setAccessible(true);
        final Object apiEndPoint = field.get(defaultBuilder);

        // Verify
        assertThat(apiEndPoint)
                .isEqualTo(URI.create("https://api.line.me/"));
    }

    @Test
    public void testChannelManagementClientBuilder() throws Exception {
        // Do
        final ChannelManagementClientBuilder defaultBuilder = new ChannelManagementClientBuilder();
        final Field field = defaultBuilder.getClass().getDeclaredField("apiEndPoint");
        field.setAccessible(true);
        final Object apiEndPoint = field.get(defaultBuilder);

        // Verify
        assertThat(apiEndPoint)
                .isEqualTo(URI.create("https://api.line.me/"));
    }
}
