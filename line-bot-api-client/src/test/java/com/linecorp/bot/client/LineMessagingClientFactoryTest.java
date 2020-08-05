package com.linecorp.bot.client;

import java.net.URI;

import org.junit.Test;

import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.message.TextMessage;

public class LineMessagingClientFactoryTest {
    @Test
    public void test() {
        LineMessagingClientFactory factory = LineMessagingClientFactory
                .builder()
                .apiEndPoint(URI.create("http://localhost/"))
                .build();

        Broadcast broadcast = new Broadcast(new TextMessage("Broadcast"), true);

        for (int i = 0; i < 100000; i++) {
            LineMessagingClient client = factory.createClient("AAAAA");
            try {
                client.broadcast(broadcast).get();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * This case creates tons of threads.
     */
    @Test
    public void withoutSharing() {
        Broadcast broadcast = new Broadcast(new TextMessage("Broadcast"), true);

        for (int i = 0; i < 100000; i++) {
            LineMessagingClient client = new LineMessagingClientBuilder()
                    .apiEndPoint(URI.create("http://localhost/"))
                    .channelToken("AHB")
                    .build();
            try {
                client.broadcast(broadcast).get();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
