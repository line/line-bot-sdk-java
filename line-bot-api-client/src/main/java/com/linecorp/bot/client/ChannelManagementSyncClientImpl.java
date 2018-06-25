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

package com.linecorp.bot.client;

import static com.linecorp.bot.client.LineMessagingClientImpl.toFuture;

import java.util.concurrent.ExecutionException;

import com.linecorp.bot.client.exception.LineMessagingException;
import com.linecorp.bot.liff.LiffView;
import com.linecorp.bot.liff.request.LiffAppAddRequest;
import com.linecorp.bot.liff.response.LiffAppAddResponse;
import com.linecorp.bot.liff.response.LiffAppsResponse;

import lombok.AllArgsConstructor;
import retrofit2.Call;

@AllArgsConstructor(staticName = "of")
public class ChannelManagementSyncClientImpl implements ChannelManagementSyncClient {
    ChannelManagementClientRetrofitIface retrofitImpl;

    @Override
    public LiffAppAddResponse addLiffApp(final LiffAppAddRequest liffAppAddRequest) {
        return syncGet(retrofitImpl.addLiffApp(liffAppAddRequest));
    }

    @Override
    public void updateLiffApp(String liffId, LiffView liffView) {
        syncGet(retrofitImpl.updateLiffApp(liffId, liffView));
    }

    @Override
    public LiffAppsResponse getAllLiffApps() {
        return syncGet(retrofitImpl.getAllLiffApps());
    }

    @Override
    public void deleteLiffApp(final String liffId) {
        syncGet(retrofitImpl.deleteLiffApp(liffId));
    }

    private <T> T syncGet(Call<T> wrap) {
        try {
            return toFuture(wrap).get();
        } catch (ExecutionException | InterruptedException e) {
            final Throwable cause;
            if (e.getCause() instanceof LineMessagingException) {
                cause = e.getCause();
            } else {
                cause = e;
            }
            throw new RuntimeException(cause);
        }
    }
}
