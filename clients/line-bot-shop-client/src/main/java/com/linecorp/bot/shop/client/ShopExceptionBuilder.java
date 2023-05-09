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

package com.linecorp.bot.shop.client;

import java.io.IOException;

import com.linecorp.bot.client.base.AbstractExceptionBuilder;
import com.linecorp.bot.shop.model.ErrorResponse;

import okhttp3.Response;

public class ShopExceptionBuilder extends AbstractExceptionBuilder<ErrorResponse> {
    public ShopExceptionBuilder() {
        super(ErrorResponse.class);
    }

    @Override
    protected IOException buildException(Response response, ErrorResponse errorBody) {
        return new ShopClientException(
                response,
                errorBody.message(), errorBody.details());
    }
}
