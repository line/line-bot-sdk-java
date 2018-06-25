package com.linecorp.bot.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.liff.LiffView;
import com.linecorp.bot.liff.LiffView.Type;
import com.linecorp.bot.liff.request.LiffAppAddRequest;
import com.linecorp.bot.liff.response.LiffAppAddResponse;
import com.linecorp.bot.model.objectmapper.ModelObjectMapper;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

public class ChannelManagementSyncClientIntegrationWiremockTest
        extends AbstractWiremockTest {
    private static final ObjectMapper OBJECT_MAPPER = ModelObjectMapper.createNewObjectMapper();

    @Test(timeout = ASYNC_TEST_TIMEOUT)
    public void testAddLiffMenu() throws Exception {
        // Mocking
        LiffAppAddResponse response = new LiffAppAddResponse("NEW_LIFF_ID");
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                                                .setBody(OBJECT_MAPPER.writeValueAsString(response)));

        // Do
        LiffView liffView = new LiffView(Type.COMPACT, URI.create("https://example.com"));
        LiffAppAddRequest request = new LiffAppAddRequest(liffView);
        final LiffAppAddResponse liffAppAddResponse = channelManagementSyncClient.addLiffApp(request);

        // Verify
        final RecordedRequest recordedRequest = mockWebServer.takeRequest();
        final LiffAppAddRequest requestedBody = OBJECT_MAPPER
                .readValue(recordedRequest.getBody().readString(StandardCharsets.UTF_8),
                           LiffAppAddRequest.class);
        assertThat(requestedBody)
                .isEqualTo(request);
        assertThat(recordedRequest.getPath())
                .isEqualTo("/liff/v1/apps");
        assertThat(liffAppAddResponse.getLiffId())
                .isEqualTo("NEW_LIFF_ID");
    }
}
