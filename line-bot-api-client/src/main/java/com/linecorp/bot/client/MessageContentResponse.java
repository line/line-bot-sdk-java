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
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class MessageContentResponse implements AutoCloseable {
    /** File size of this content. */
    final long length;

    /** File input stream of this content. */
    final InputStream stream;

    /** File contents type represented by MIME. */
    final String mimeType;

    /**
     * All HTTP headers of API response.
     *
     * <p>Note: there are no SPEC for those headers.
     * Current field values are provided AS-IS and can be changed/removed without announces.
     */
    final Map<String, List<String>> allHeaders;

    @Override
    public void close() throws IOException {
        stream.close();
    }
}
