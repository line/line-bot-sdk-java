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

package com.linecorp.bot.model.event;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import org.junit.Test;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.linecorp.bot.model.event.message.FileMessageContent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.message.UnknownMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.UnknownSource;
import com.linecorp.bot.model.event.source.UserSource;

public class CallbackRequestTest {
    interface RequestTester {
        void call(CallbackRequest request) throws IOException;
    }

    private void parse(String resourceName, RequestTester callback) throws IOException {
        try (InputStream resource = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            String json = StreamUtils.copyToString(resource, StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.registerModule(new JavaTimeModule())
                        .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
            CallbackRequest callbackRequest = objectMapper.readValue(json, CallbackRequest.class);

            callback.call(callbackRequest);
        }
    }

    @Test
    public void textTextUser() throws IOException {
        parse("callback/text-user.json", callbackRequest -> {
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(UserSource.class);

            MessageEvent messageEvent = (MessageEvent) event;
            assertThat(messageEvent.getReplyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
            MessageContent message = messageEvent.getMessage();
            assertThat(message).isInstanceOf(TextMessageContent.class);
        });
    }

    @Test
    public void textTextGroup() throws IOException {
        parse("callback/text-group.json", callbackRequest -> {
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(GroupSource.class);
            assertThat(event.getSource().getUserId())
                    .isNull();

            MessageEvent messageEvent = (MessageEvent) event;
            assertThat(messageEvent.getReplyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
            MessageContent message = messageEvent.getMessage();
            assertThat(message).isInstanceOf(TextMessageContent.class);
        });
    }

    @Test
    public void testImage() throws IOException {
        parse("callback/image.json", callbackRequest -> {
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(UserSource.class);
            assertThat(event.getSource().getUserId())
                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");

            MessageEvent messageEvent = (MessageEvent) event;
            assertThat(messageEvent.getReplyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
            MessageContent message = messageEvent.getMessage();
            assertThat(message).isInstanceOf(ImageMessageContent.class);
        });
    }

//    @Test
//    public void testLocation() throws IOException {
//        parse("callback/location.json", callbackRequest -> {
//            assertThat(callbackRequest.getEvents()).hasSize(1);
//            Event event = callbackRequest.getEvents().get(0);
//            assertThat(event).isInstanceOf(MessageEvent.class);
//            assertThat(event.getSource())
//                    .isInstanceOf(UserSource.class);
//            assertThat(event.getSource().getUserId())
//                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
//            assertThat(event.getTimestamp())
//                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));
//
//            MessageEvent messageEvent = (MessageEvent) event;
//            assertThat(messageEvent.getReplyToken())
//                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
//            MessageContent message = messageEvent.getMessage();
//            assertThat(message).isInstanceOf(LocationMessageContent.class);
//            if (message instanceof LocationMessageContent) {
//                assertThat(((LocationMessageContent) message).getAddress())
//                        .isEqualTo("ã€’150-0002 æ�±äº¬éƒ½æ¸‹è°·åŒºæ¸‹è°·ï¼’ä¸�ç›®ï¼’ï¼‘âˆ’ï¼‘");
//            }
//        });
//    }

    @Test
    public void testFile() throws IOException {
        parse("callback/file.json", callbackRequest -> {
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(UserSource.class);
            assertThat(event.getSource().getUserId())
                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");

            MessageEvent messageEvent = (MessageEvent) event;
            assertThat(messageEvent.getReplyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
            MessageContent message = messageEvent.getMessage();
            assertThat(message).isInstanceOf(FileMessageContent.class);
            if (message instanceof FileMessageContent) {
                assertThat(((FileMessageContent) message).getFileName())
                        .isEqualTo("sample.pdf");
                assertThat(((FileMessageContent) message).getFileSize())
                        .isEqualTo(22016);
            }
        });
    }

    @Test
    public void testSticker() throws IOException {
        parse("callback/sticker.json", callbackRequest -> {
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(UserSource.class);
            assertThat(event.getSource().getUserId())
                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
            assertThat(event.getTimestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));

            MessageEvent messageEvent = (MessageEvent) event;
            assertThat(messageEvent.getReplyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
            MessageContent message = messageEvent.getMessage();
            assertThat(message).isInstanceOf(StickerMessageContent.class);
            if (message instanceof StickerMessageContent) {
                assertThat(((StickerMessageContent) message).getStickerId())
                        .isEqualTo("1");
                assertThat(((StickerMessageContent) message).getPackageId())
                        .isEqualTo("1");
            }
        });
    }

    @Test
    public void testFollow() throws IOException {
        parse("callback/follow.json", callbackRequest -> {
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(FollowEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(UserSource.class);
            assertThat(event.getSource().getUserId())
                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
            assertThat(event.getTimestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));

            FollowEvent followEvent = (FollowEvent) event;
            assertThat(followEvent.getReplyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
        });
    }

    @Test
    public void testUnfollow() throws IOException {
        parse("callback/unfollow.json", callbackRequest -> {
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(UnfollowEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(UserSource.class);
            assertThat(event.getSource().getUserId())
                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
            assertThat(event.getTimestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));
        });
    }

    @Test
    public void testJoin() throws IOException {
        parse("callback/join.json", callbackRequest -> {
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(JoinEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(GroupSource.class);
            assertThat(((GroupSource) event.getSource()).getGroupId())
                    .isEqualTo("cxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            assertThat(event.getTimestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));
        });
    }

    @Test
    public void testLeave() throws IOException {
        parse("callback/leave.json", callbackRequest -> {
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(LeaveEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(GroupSource.class);
            assertThat(((GroupSource) event.getSource()).getGroupId())
                    .isEqualTo("cxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            assertThat(event.getTimestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));
        });
    }

    @Test
    public void testPostback() throws IOException {
        parse("callback/postback.json", callbackRequest -> {
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(PostbackEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(UserSource.class);
            assertThat(event.getSource().getUserId())
                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
            assertThat(event.getTimestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));

            PostbackEvent postbackEvent = (PostbackEvent) event;
            assertThat(postbackEvent.getPostbackContent().getData())
                    .isEqualTo("action=buyItem&itemId=123123&color=red");
        });
    }

    @Test
    public void testBeacon() throws IOException {
        parse("callback/beacon.json", callbackRequest -> {
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(BeaconEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(UserSource.class);
            assertThat(event.getSource().getUserId())
                    .isEqualTo("U012345678901234567890123456789ab");
            assertThat(event.getTimestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));

            BeaconEvent beaconEvent = (BeaconEvent) event;
            assertThat(beaconEvent.getBeacon().getHwid())
                    .isEqualTo("374591320");
            assertThat(beaconEvent.getBeacon().getType())
                    .isEqualTo("enter");
            assertThat(beaconEvent.getBeacon().getDeviceMessage())
                    .isNull();
            assertThat(beaconEvent.getBeacon().getDeviceMessageAsHex())
                    .isNull();
        });
    }

    @Test
    public void testBeaconWithDeviceMessage() throws IOException {
        parse("callback/beacon_with_dm.json", callbackRequest -> {
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(BeaconEvent.class);

            BeaconEvent beaconEvent = (BeaconEvent) event;

            assertThat(beaconEvent.getSource())
                    .isInstanceOf(UserSource.class);
            assertThat(beaconEvent.getSource().getUserId())
                    .isEqualTo("U012345678901234567890123456789ab");
            assertThat(beaconEvent.getTimestamp())
                    .isEqualTo(Instant.parse("2017-04-24T00:00:00Z"));
            assertThat(beaconEvent.getBeacon().getHwid())
                    .isEqualTo("374591320");
            assertThat(beaconEvent.getBeacon().getType())
                    .isEqualTo("enter");
            assertThat(beaconEvent.getBeacon().getDeviceMessage())
                    .containsExactly(0x12, 0x34, 0x56, 0x78, 0x90, 0xab, 0xcd, 0xef);
            assertThat(beaconEvent.getBeacon().getDeviceMessageAsHex())
                    .isEqualTo("1234567890abcdef");
        });
    }

    // Event, that has brand new eventType
    @Test
    public void testUnknown() throws IOException {
        parse("callback/unknown.json", callbackRequest -> {
            assertThat(callbackRequest.getEvents()).hasSize(2);

            Event event1 = callbackRequest.getEvents().get(0);
            assertThat(event1).isInstanceOf(UnknownEvent.class);
            assertThat(event1.getSource())
                    .isInstanceOf(UserSource.class);
            assertThat(event1.getSource().getUserId())
                    .isEqualTo("U012345678901234567890123456789ab");
            assertThat(event1.getTimestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));
            assertThat(((UnknownEvent) event1).getType())
                    .isEqualTo("greatNewFeature");

            Event event2 = callbackRequest.getEvents().get(1);
            assertThat(event2).isInstanceOf(MessageEvent.class);
            assertThat(event2.getSource()).isInstanceOf(UnknownSource.class);
            MessageEvent messageEvent = (MessageEvent) event2;
            assertThat(messageEvent.getMessage()).isInstanceOf(UnknownMessageContent.class);
        });
    }
}
