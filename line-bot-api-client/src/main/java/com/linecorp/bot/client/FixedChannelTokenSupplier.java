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

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * Implementation of {@link ChannelTokenSupplier} which always return fixed channel token.
 */
@AllArgsConstructor(staticName = "of")
@ToString
public final class FixedChannelTokenSupplier implements ChannelTokenSupplier {
    @NonNull
    private final String channelToken;

    @Override
    public String get() {
        return channelToken;
    }
}
