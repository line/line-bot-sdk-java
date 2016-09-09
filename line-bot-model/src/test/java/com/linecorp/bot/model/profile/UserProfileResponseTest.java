package com.linecorp.bot.model.profile;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UserProfileResponseTest {
    @Test
    public void test() throws IOException {
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(
                "user-profiles.json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            UserProfileResponse userProfileResponse = objectMapper.readValue(resourceAsStream,
                                                                             UserProfileResponse.class);
            Assert.assertNotNull(userProfileResponse.getRequestId());
            Assert.assertNotNull(userProfileResponse.getDisplayName());
        }
    }
}
