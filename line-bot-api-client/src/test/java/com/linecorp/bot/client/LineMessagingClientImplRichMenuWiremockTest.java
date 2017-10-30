package com.linecorp.bot.client;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.linecorp.bot.model.response.BotApiResponse;

import okhttp3.mockwebserver.MockResponse;

public class LineMessagingClientImplRichMenuWiremockTest extends AbstractWiremockTest {
    public static final BotApiResponse SUCCESS = new BotApiResponse("", emptyList());

    @Test(timeout = ASYNC_TEST_TIMEOUT)
    public void status200WithoutBodyTest() throws Exception {
        // Mocking
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        // Do
        final BotApiResponse botApiResponse = lineMessagingClient.deleteRichMenu("RICH_MENU_ID").get();
        assertThat(botApiResponse).isEqualTo(SUCCESS);
    }

    @Test(timeout = ASYNC_TEST_TIMEOUT)
    public void status200WithBodyTest() throws Exception {
        // Mocking
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("{}"));

        // Do
        final BotApiResponse botApiResponse = lineMessagingClient.deleteRichMenu("RICH_MENU_ID").get();
        assertThat(botApiResponse).isEqualTo(SUCCESS);
    }
}
