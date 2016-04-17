/*
 * Copyright 2016 LINE Corporation
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

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

import lombok.NonNull;

public class CloseableMessageContentImpl implements CloseableMessageContent {
    private CloseableHttpClient httpClient;
    private final CloseableHttpResponse response;

    public CloseableMessageContentImpl(CloseableHttpClient httpClient, @NonNull CloseableHttpResponse response) {
        this.httpClient = httpClient;
        this.response = response;
    }

    @Override
    public String getFileName() {
        Header header = this.response.getFirstHeader("Content-Disposition");
        return header != null ? header.getValue() : null;
    }

    @Override
    public InputStream getContent() throws IOException {
        return this.response.getEntity().getContent();
    }

    @Override
    public void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            // ignore
        }
        try {
            response.close();
        } catch (IOException e) {
            // ignore
        }
    }
}
