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

package com.linecorp.bot.model.profile;

import static java.lang.ClassLoader.getSystemResourceAsStream;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ProfileResponseTest {
    @Test
    public void testProfile() throws IOException {
        try (InputStream resourceAsStream = getSystemResourceAsStream("user-profiles.json")) {
            ObjectMapper objectMapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

            final UserProfileResponse userProfileResponse =
                    objectMapper.readValue(resourceAsStream, UserProfileResponse.class);

            assertThat(userProfileResponse.getDisplayName()).isEqualTo("Tester");
            assertThat(userProfileResponse.getUserId()).isEqualTo("Uc6a5e7b3d4d08c33dd8d530fb3c02762");
            assertThat(userProfileResponse.getPictureUrl()).isEqualTo("https://example.com/picture.png");
            assertThat(userProfileResponse.getStatusMessage()).isEqualTo("Movie");
        }
    }

    @Test
    public void testMemberProfile() throws IOException {
        try (InputStream resourceAsStream = getSystemResourceAsStream("user-profiles_member.json")) {
            ObjectMapper objectMapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

            final UserProfileResponse userProfileResponse =
                    objectMapper.readValue(resourceAsStream, UserProfileResponse.class);

            assertThat(userProfileResponse.getDisplayName()).isEqualTo("Tester");
            assertThat(userProfileResponse.getUserId()).isEqualTo("Uc6a5e7b3d4d08c33dd8d530fb3c02762");
            assertThat(userProfileResponse.getPictureUrl()).isEqualTo("https://example.com/picture.png");
            assertThat(userProfileResponse.getStatusMessage()).isNull();
        }
    }
}
