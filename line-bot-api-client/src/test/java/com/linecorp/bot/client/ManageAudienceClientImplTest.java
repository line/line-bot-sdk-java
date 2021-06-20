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

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import com.linecorp.bot.model.manageaudience.AudienceGroup;
import com.linecorp.bot.model.manageaudience.AudienceGroupAuthorityLevel;
import com.linecorp.bot.model.manageaudience.AudienceGroupCreateRoute;
import com.linecorp.bot.model.manageaudience.AudienceGroupPermission;
import com.linecorp.bot.model.manageaudience.AudienceGroupStatus;
import com.linecorp.bot.model.manageaudience.AudienceGroupType;
import com.linecorp.bot.model.manageaudience.request.Audience;
import com.linecorp.bot.model.manageaudience.request.CreateAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.CreateClickBasedAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.CreateImpBasedAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.UpdateAudienceGroupAuthorityLevelRequest;
import com.linecorp.bot.model.manageaudience.request.UpdateAudienceGroupDescriptionRequest;
import com.linecorp.bot.model.manageaudience.response.CreateAudienceGroupResponse;
import com.linecorp.bot.model.manageaudience.response.CreateClickBasedAudienceGroupResponse;
import com.linecorp.bot.model.manageaudience.response.CreateImpBasedAudienceGroupResponse;
import com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse;
import com.linecorp.bot.model.manageaudience.response.GetAudienceGroupAuthorityLevelResponse;
import com.linecorp.bot.model.manageaudience.response.GetAudienceGroupsResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.richmenu.RichMenuIdResponse;

import okhttp3.Headers;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@ExtendWith(MockitoExtension.class)
@Timeout(5)
public class ManageAudienceClientImplTest {
    private static final String REQUEST_ID_FIXTURE = "REQUEST_ID_FIXTURE";
    private static final BotApiResponseBody BOT_API_SUCCESS_RESPONSE_BODY =
            new BotApiResponseBody("", emptyList());
    private static final BotApiResponse BOT_API_SUCCESS_RESPONSE =
            BOT_API_SUCCESS_RESPONSE_BODY.withRequestId(REQUEST_ID_FIXTURE);
    private static final RichMenuIdResponse RICH_MENU_ID_RESPONSE = new RichMenuIdResponse("ID");

    @Mock
    private ManageAudienceService retrofitMock;

    @InjectMocks
    private ManageAudienceClientImpl target;

    @Test
    public void getAudienceGroups() throws Exception {
        final GetAudienceGroupsResponse response = GetAudienceGroupsResponse
                .builder()
                .audienceGroups(singletonList(AudienceGroup.builder()
                                                           .audienceGroupId(3L)
                                                           .type(AudienceGroupType.IMP)
                                                           .description("hello")
                                                           .status(AudienceGroupStatus.EXPIRED)
                                                           .failedType(null)
                                                           .audienceCount(3L)
                                                           .created(1583540070193L)
                                                           .requestId("6744d281-c0bd-4a63-91d5-cab659827828")
                                                           .clickUrl(null)
                                                           .isIfaAudience(false)
                                                           .permission(AudienceGroupPermission.READ_WRITE)
                                                           .createRoute(AudienceGroupCreateRoute.OA_MANAGER)
                                                           .build()))
                .hasNextPage(true)
                .totalCount(4L)
                .size(1L)
                .page(1L)
                .readWriteAudienceGroupTotalCount(1L)
                .build();
        whenCall(retrofitMock.getAudienceGroups(anyLong(), any(), any(), any(), any(),
                                                any()), response);
        final GetAudienceGroupsResponse actual =
                target.getAudienceGroups(1L, null, null, 40L, null, null).get();
        verify(retrofitMock, only()).getAudienceGroups(1L, null, null, 40L,
                                                       null, null);
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void createAudienceGroup() throws Exception {
        final CreateAudienceGroupResponse response =
                CreateAudienceGroupResponse.builder()
                                           .build();
        CreateAudienceGroupRequest request =
                CreateAudienceGroupRequest.builder()
                                          .description("test")
                                          .isIfaAudience(false)
                                          .uploadDescription("test")
                                          .audiences(singletonList(new Audience("Uabcdef")))
                                          .build();

        whenCall(retrofitMock.createAudienceGroup(any()), response);
        final CreateAudienceGroupResponse actual =
                target.createAudienceGroup(request).get();
        verify(retrofitMock, only()).createAudienceGroup(request);
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void createClickBasedAudienceGroup() throws Exception {
        CreateClickBasedAudienceGroupRequest request = CreateClickBasedAudienceGroupRequest.builder()
                                                                                           .build();
        CreateClickBasedAudienceGroupResponse response = CreateClickBasedAudienceGroupResponse.builder()
                                                                                              .build();

        whenCall(retrofitMock.createClickBasedAudienceGroup(any()), response);
        final CreateClickBasedAudienceGroupResponse actual =
                target.createClickBasedAudienceGroup(request).get();
        verify(retrofitMock, only()).createClickBasedAudienceGroup(request);
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void createImpBasedAudienceGroup() throws Exception {
        CreateImpBasedAudienceGroupRequest request = CreateImpBasedAudienceGroupRequest.builder()
                                                                                       .build();
        CreateImpBasedAudienceGroupResponse response = CreateImpBasedAudienceGroupResponse.builder()
                                                                                          .build();

        whenCall(retrofitMock.createImpBasedAudienceGroup(any()), response);
        final CreateImpBasedAudienceGroupResponse actual =
                target.createImpBasedAudienceGroup(request).get();
        verify(retrofitMock, only()).createImpBasedAudienceGroup(request);
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void updateAudienceGroupDescription() throws Exception {
        UpdateAudienceGroupDescriptionRequest request = UpdateAudienceGroupDescriptionRequest.builder()
                                                                                             .description(
                                                                                                     "Hello")
                                                                                             .build();
        whenCall(retrofitMock.updateAudienceGroupDescription(anyLong(), any()), null);
        final BotApiResponse actual =
                target.updateAudienceGroupDescription(5693L,
                                                      request
                ).get();
        verify(retrofitMock, only()).updateAudienceGroupDescription(5693L, request);
        assertThat(actual).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void deleteAudienceGroup() throws Exception {
        whenCall(retrofitMock.deleteAudienceGroup(anyLong()), null);
        final BotApiResponse actual =
                target.deleteAudienceGroup(5693L).get();
        verify(retrofitMock, only()).deleteAudienceGroup(5693L);
        assertThat(actual).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void getAudienceData() throws Exception {
        final GetAudienceDataResponse response = GetAudienceDataResponse
                .builder()
                .build();
        whenCall(retrofitMock.getAudienceData(anyLong()), response);
        final GetAudienceDataResponse actual =
                target.getAudienceData(4649L).get();
        verify(retrofitMock, only()).getAudienceData(4649L);
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void getAudienceGroupAuthorityLevel() throws Exception {
        final GetAudienceGroupAuthorityLevelResponse response = GetAudienceGroupAuthorityLevelResponse
                .builder()
                .authorityLevel(AudienceGroupAuthorityLevel.PRIVATE)
                .build();
        whenCall(retrofitMock.getAudienceGroupAuthorityLevel(), response);
        final GetAudienceGroupAuthorityLevelResponse actual =
                target.getAudienceGroupAuthorityLevel().get();
        verify(retrofitMock, only()).getAudienceGroupAuthorityLevel();
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void updateAudienceGroupAuthorityLevel() throws Exception {
        final UpdateAudienceGroupAuthorityLevelRequest request = UpdateAudienceGroupAuthorityLevelRequest
                .builder()
                .authorityLevel(AudienceGroupAuthorityLevel.PRIVATE)
                .build();
        whenCall(retrofitMock.updateAudienceGroupAuthorityLevel(request), null);
        final BotApiResponse actual =
                target.updateAudienceGroupAuthorityLevel(request).get();
        verify(retrofitMock, only()).updateAudienceGroupAuthorityLevel(request);
        assertThat(actual).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    // Utility methods

    private static <T> void whenCall(Call<T> call, T value) {
        final OngoingStubbing<Call<T>> callOngoingStubbing = when(call);
        callOngoingStubbing.thenReturn(enqueue(value));
    }

    private static <T> Call<T> enqueue(T value) {
        return new Call<T>() {
            @Override
            public Response<T> execute() throws IOException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void enqueue(Callback<T> callback) {
                final Headers headers = Headers.of(singletonMap("x-line-request-id", REQUEST_ID_FIXTURE));
                callback.onResponse(this, Response.success(value, headers));
            }

            @Override
            public boolean isExecuted() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void cancel() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isCanceled() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Call<T> clone() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Request request() {
                throw new UnsupportedOperationException();
            }

            @Override
            public okio.Timeout timeout() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
