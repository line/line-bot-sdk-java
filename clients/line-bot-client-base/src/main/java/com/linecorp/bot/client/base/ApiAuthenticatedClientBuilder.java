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

import static java.util.Objects.requireNonNull;

import java.net.URI;

import com.linecorp.bot.client.base.channel.ChannelTokenSupplier;
import com.linecorp.bot.client.base.channel.FixedChannelTokenSupplier;

public class ApiAuthenticatedClientBuilder<T> extends ApiClientBuilder<T> {
    public ApiAuthenticatedClientBuilder(URI apiEndPoint, Class<T> clientClass, ExceptionBuilder exceptionBuilder, ChannelTokenSupplier channelTokenSupplier) {
        super(apiEndPoint, clientClass, exceptionBuilder);
        this.channelTokenSupplier = channelTokenSupplier;
    }

    public ApiAuthenticatedClientBuilder(URI apiEndPoint, Class<T> clientClass, ExceptionBuilder exceptionBuilder, String channelToken) {
        super(apiEndPoint, clientClass, exceptionBuilder);
        this.channelTokenSupplier = FixedChannelTokenSupplier.of(channelToken);
    }

    /**
     * Channel token supplier of this client.
     *
     * <p>MUST BE NULL except you configured your own
     */
    private final ChannelTokenSupplier channelTokenSupplier;

    /**
     * Add authentication header.
     *
     * <p>Default = {@value}. If you manage authentication header yourself, set to {@code false}.
     */
    private boolean addAuthenticationHeader = true;

    /**
     * Add authentication header or not.
     */
    public ApiAuthenticatedClientBuilder<T> addAuthenticationHeader(boolean addAuthenticationHeader) {
        this.addAuthenticationHeader = addAuthenticationHeader;
        return this;
    }

    @Override
    public T build() {
        if (addAuthenticationHeader) {
            requireNonNull(channelTokenSupplier,
                    "You need to set the channelTokenSupplier"
                            + " for the ApiAuthenticatedClientBuilder before building instance.");
            this.addInterceptor(HeaderInterceptor.forChannelTokenSupplier(channelTokenSupplier));
        }
        return super.build();
    }

    @Override
    public String toString() {
        return "ApiAuthenticatedClientBuilder{"
                + "channelTokenSupplier=" + channelTokenSupplier
                + ", addAuthenticationHeader=" + addAuthenticationHeader
                + ", (" + super.toString()
                + ")}";
    }
}
