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

package com.linecorp.bot.webhook.model;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CallbackRequestTest {
    @FunctionalInterface
    interface RequestTester {
        void call(CallbackRequest request) throws IOException;
    }

    private void parse(String resourceName, RequestTester callback) throws IOException {
        try (InputStream resource = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            String json = new String(requireNonNull(resource).readAllBytes(), StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
            CallbackRequest callbackRequest = objectMapper.readValue(json, CallbackRequest.class);

            callback.call(callbackRequest);
        }
    }

    private static void assertDestination(CallbackRequest request) {
        Assertions.assertThat(request.destination()).isEqualTo("Uab012345678901234567890123456789");
    }

    @Test
    public void textTextUser() throws IOException {
        parse("callback/text-user.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Assertions.assertThat(callbackRequest.destination()).isEqualTo("Uab012345678901234567890123456789");
            Assertions.assertThat(callbackRequest.events()).hasSize(1);
            Event event = callbackRequest.events().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            Assertions.assertThat(event.source())
                    .isInstanceOf(UserSource.class);
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.ACTIVE);

            MessageEvent messageEvent = (MessageEvent) event;
            Assertions.assertThat(messageEvent.replyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
            MessageContent message = messageEvent.message();
            assertThat(message).isInstanceOf(TextMessageContent.class);
        });
    }

    @Test
    public void textTextEmojis() throws IOException {
        parse("callback/text-emojis.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Assertions.assertThat(callbackRequest.destination()).isEqualTo("Uab012345678901234567890123456789");
            Assertions.assertThat(callbackRequest.events()).hasSize(1);
            Event event = callbackRequest.events().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            Assertions.assertThat(event.source())
                    .isInstanceOf(UserSource.class);
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.ACTIVE);

            MessageEvent messageEvent = (MessageEvent) event;
            Assertions.assertThat(messageEvent.replyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
            MessageContent message = messageEvent.message();
            assertThat(message).isInstanceOf(TextMessageContent.class);

            List<Emoji> emojis = ((TextMessageContent) message).emojis();
            assertThat(emojis).hasSize(1);
            assertThat(emojis.get(0)).isInstanceOf(Emoji.class);
            Assertions.assertThat(emojis.get(0).index()).isEqualTo(14);
            Assertions.assertThat(emojis.get(0).length()).isEqualTo(6);
            Assertions.assertThat(emojis.get(0).productId()).isEqualTo("5ac1bfd5040ab15980c9b435");
            Assertions.assertThat(emojis.get(0).emojiId()).isEqualTo("001");
        });
    }

    @Test
    public void textTextMention() throws IOException {
        parse("callback/text-mention.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Assertions.assertThat(callbackRequest.destination()).isEqualTo("Uab012345678901234567890123456789");
            Assertions.assertThat(callbackRequest.events()).hasSize(1);
            Event event = callbackRequest.events().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            Assertions.assertThat(event.source())
                    .isInstanceOf(UserSource.class);
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.ACTIVE);

            MessageEvent messageEvent = (MessageEvent) event;
            Assertions.assertThat(messageEvent.replyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
            MessageContent message = messageEvent.message();
            assertThat(message).isInstanceOf(TextMessageContent.class);

            Mention mention = ((TextMessageContent) message).mention();
            assertThat(mention).isInstanceOf(Mention.class);
            Assertions.assertThat(mention.mentionees()).isNotNull();

            List<Mentionee> mentionees = mention.mentionees();
            assertThat(mentionees).hasSize(2);

            UserMentionee mentioneeBrown = (UserMentionee) mentionees.get(0);
            assertThat(mentioneeBrown).isInstanceOf(UserMentionee.class);
            Assertions.assertThat(mentioneeBrown.index()).isEqualTo(13);
            Assertions.assertThat(mentioneeBrown.length()).isEqualTo(6);
            Assertions.assertThat(mentioneeBrown.userId()).isEqualTo("U12345678901234567890123456780");

            AllMentionee mentioneeCony = (AllMentionee) mentionees.get(1);
            assertThat(mentioneeCony).isInstanceOf(AllMentionee.class);
            Assertions.assertThat(mentioneeCony.index()).isEqualTo(24);
            Assertions.assertThat(mentioneeCony.length()).isEqualTo(5);
        });
    }

    @Test
    public void textTextGroup() throws IOException {
        parse("callback/text-group.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Assertions.assertThat(callbackRequest.events()).hasSize(1);
            Event event = callbackRequest.events().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            Assertions.assertThat(event.source())
                    .isInstanceOf(GroupSource.class);
            Assertions.assertThat(event.source().userId())
                    .isNull();
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.STANDBY);

            MessageEvent messageEvent = (MessageEvent) event;
            Assertions.assertThat(messageEvent.replyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
            MessageContent message = messageEvent.message();
            assertThat(message).isInstanceOf(TextMessageContent.class);
        });
    }

    @Test
    public void testImage() throws IOException {
        parse("callback/image.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Assertions.assertThat(callbackRequest.events()).hasSize(1);
            Event event = callbackRequest.events().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            Assertions.assertThat(event.source())
                    .isInstanceOf(UserSource.class);
            Assertions.assertThat(event.source().userId())
                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.ACTIVE);

            MessageEvent messageEvent = (MessageEvent) event;
            Assertions.assertThat(messageEvent.replyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
            MessageContent message = messageEvent.message();
            assertThat(message).isInstanceOf(ImageMessageContent.class);
            ImageMessageContent image = (ImageMessageContent) message;
            Assertions.assertThat(image.id()).isEqualTo("325708");
            Assertions.assertThat(image.contentProvider()).isEqualTo(
                    new ContentProvider(
                            ContentProvider.Type.EXTERNAL,
                            URI.create("https://example.com/original.jpg"),
                            URI.create("https://example.com/preview.jpg")
                    )
            );
        });
    }

    @Test
    public void testLocation() throws IOException {
        parse("callback/location.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Assertions.assertThat(callbackRequest.events()).hasSize(1);
            Event event = callbackRequest.events().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            Assertions.assertThat(event.source())
                    .isInstanceOf(UserSource.class);
            Assertions.assertThat(event.source().userId())
                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
            Assertions.assertThat(event.timestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z").toEpochMilli());
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.ACTIVE);

            MessageEvent messageEvent = (MessageEvent) event;
            Assertions.assertThat(messageEvent.replyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
            MessageContent message = messageEvent.message();
            assertThat(message).isInstanceOf(LocationMessageContent.class);
            if (message instanceof LocationMessageContent) {
                Assertions.assertThat(((LocationMessageContent) message).address())
                        .isEqualTo("〒150-0002 東京都渋谷区渋谷２丁目２１−１");
            }
        });
    }

    @Test
    public void testFile() throws IOException {
        parse("callback/file.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Assertions.assertThat(callbackRequest.events()).hasSize(1);
            Event event = callbackRequest.events().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            Assertions.assertThat(event.source())
                    .isInstanceOf(UserSource.class);
            Assertions.assertThat(event.source().userId())
                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.ACTIVE);

            MessageEvent messageEvent = (MessageEvent) event;
            Assertions.assertThat(messageEvent.replyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
            MessageContent message = messageEvent.message();
            assertThat(message).isInstanceOf(FileMessageContent.class);
            if (message instanceof FileMessageContent) {
                Assertions.assertThat(((FileMessageContent) message).fileName())
                        .isEqualTo("sample.pdf");
                Assertions.assertThat(((FileMessageContent) message).fileSize())
                        .isEqualTo(22016);
            }
        });
    }

    @Test
    public void testSticker() throws IOException {
        parse("callback/sticker.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Assertions.assertThat(callbackRequest.events()).hasSize(1);
            Event event = callbackRequest.events().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            Assertions.assertThat(event.source())
                    .isInstanceOf(UserSource.class);
            Assertions.assertThat(event.source().userId())
                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
            Assertions.assertThat(event.timestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z").toEpochMilli());
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.ACTIVE);

            MessageEvent messageEvent = (MessageEvent) event;
            Assertions.assertThat(messageEvent.replyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
            MessageContent message = messageEvent.message();
            assertThat(message).isInstanceOf(StickerMessageContent.class);
            if (message instanceof StickerMessageContent) {
                Assertions.assertThat(((StickerMessageContent) message).stickerId())
                        .isEqualTo("1");
                Assertions.assertThat(((StickerMessageContent) message).packageId())
                        .isEqualTo("1");
                Assertions.assertThat(((StickerMessageContent) message).stickerResourceType())
                        .isEqualTo(StickerMessageContent.StickerResourceType.STATIC);
                Assertions.assertThat(((StickerMessageContent) message).keywords())
                        .containsExactly("bed", "sleep", "bedtime");
            }
        });
    }

    @Test
    public void testStickerWithText() throws IOException {
        parse("callback/sticker-with-text.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Assertions.assertThat(callbackRequest.events()).hasSize(1);
            Event event = callbackRequest.events().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            Assertions.assertThat(event.source())
                    .isInstanceOf(UserSource.class);
            Assertions.assertThat(event.source().userId())
                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
            Assertions.assertThat(event.timestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z").toEpochMilli());
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.ACTIVE);

            MessageEvent messageEvent = (MessageEvent) event;
            Assertions.assertThat(messageEvent.replyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
            MessageContent message = messageEvent.message();
            assertThat(message).isInstanceOf(StickerMessageContent.class);
            if (message instanceof StickerMessageContent) {
                Assertions.assertThat(((StickerMessageContent) message).stickerId())
                        .isEqualTo("1");
                Assertions.assertThat(((StickerMessageContent) message).packageId())
                        .isEqualTo("1");
                Assertions.assertThat(((StickerMessageContent) message).stickerResourceType())
                        .isEqualTo(StickerMessageContent.StickerResourceType.MESSAGE);
                Assertions.assertThat(((StickerMessageContent) message).keywords())
                        .containsExactly("bed", "sleep", "bedtime");
                Assertions.assertThat(((StickerMessageContent) message).text())
                        .isEqualTo("userEnteredText");
            }
        });
    }

    @Test
    public void testStickerKeywordsBecomeArray() throws IOException {
        parse("callback/sticker-keywords-array.json", callbackRequest -> {
            MessageEvent messageEvent = (MessageEvent) callbackRequest.events().get(0);
            StickerMessageContent message = (StickerMessageContent) messageEvent.message();
            Assertions.assertThat(message.keywords()).isEqualTo(List.of("keyword"));
        });
    }

    @Test
    public void testStickerKeywordsRemoved() throws IOException {
        parse("callback/sticker-keywords-remove.json", callbackRequest -> {
            MessageEvent messageEvent = (MessageEvent) callbackRequest.events().get(0);
            MessageContent message = messageEvent.message();
            if (message instanceof StickerMessageContent) {
                Assertions.assertThat(((StickerMessageContent) message).keywords()).isNull();
            }
        });
    }

    @Test
    public void testFollow() throws IOException {
        parse("callback/follow.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Assertions.assertThat(callbackRequest.events()).hasSize(1);
            Event event = callbackRequest.events().get(0);
            assertThat(event).isInstanceOf(FollowEvent.class);
            Assertions.assertThat(event.source())
                    .isInstanceOf(UserSource.class);
            Assertions.assertThat(event.source().userId())
                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
            Assertions.assertThat(event.timestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z").toEpochMilli());
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.ACTIVE);

            FollowEvent followEvent = (FollowEvent) event;
            Assertions.assertThat(followEvent.replyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
        });
    }

    @Test
    public void testUnfollow() throws IOException {
        parse("callback/unfollow.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Assertions.assertThat(callbackRequest.events()).hasSize(1);
            Event event = callbackRequest.events().get(0);
            assertThat(event).isInstanceOf(UnfollowEvent.class);
            Assertions.assertThat(event.source())
                    .isInstanceOf(UserSource.class);
            Assertions.assertThat(event.source().userId())
                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
            Assertions.assertThat(event.timestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z").toEpochMilli());
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.ACTIVE);
        });
    }

    @Test
    public void testJoin() throws IOException {
        parse("callback/join.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Assertions.assertThat(callbackRequest.events()).hasSize(1);
            Event event = callbackRequest.events().get(0);
            assertThat(event).isInstanceOf(JoinEvent.class);
            Assertions.assertThat(event.source())
                    .isInstanceOf(GroupSource.class);
            Assertions.assertThat(((GroupSource) event.source()).groupId())
                    .isEqualTo("cxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            Assertions.assertThat(event.timestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z").toEpochMilli());
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.ACTIVE);
        });
    }

    @Test
    public void testLeave() throws IOException {
        parse("callback/leave.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Assertions.assertThat(callbackRequest.events()).hasSize(1);
            Event event = callbackRequest.events().get(0);
            assertThat(event).isInstanceOf(LeaveEvent.class);
            Assertions.assertThat(event.source())
                    .isInstanceOf(GroupSource.class);
            Assertions.assertThat(((GroupSource) event.source()).groupId())
                    .isEqualTo("cxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            Assertions.assertThat(event.timestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z").toEpochMilli());
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.ACTIVE);
        });
    }

    @Test
    public void testPostback() throws IOException {
        parse("callback/postback.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Assertions.assertThat(callbackRequest.events()).hasSize(1);
            Event event = callbackRequest.events().get(0);
            assertThat(event).isInstanceOf(PostbackEvent.class);
            Assertions.assertThat(event.source())
                    .isInstanceOf(UserSource.class);
            Assertions.assertThat(event.source().userId())
                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
            Assertions.assertThat(event.timestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z").toEpochMilli());
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.ACTIVE);

            PostbackEvent postbackEvent = (PostbackEvent) event;
            Assertions.assertThat(postbackEvent.postback().data())
                    .isEqualTo("action=buyItem&itemId=123123&color=red");
        });
    }

    @Test
    public void testBeacon() throws IOException {
        parse("callback/beacon.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Assertions.assertThat(callbackRequest.events()).hasSize(1);
            Event event = callbackRequest.events().get(0);
            assertThat(event).isInstanceOf(BeaconEvent.class);
            Assertions.assertThat(event.source())
                    .isInstanceOf(UserSource.class);
            Assertions.assertThat(event.source().userId())
                    .isEqualTo("U012345678901234567890123456789ab");
            Assertions.assertThat(event.timestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z").toEpochMilli());
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.ACTIVE);

            BeaconEvent beaconEvent = (BeaconEvent) event;
            Assertions.assertThat(beaconEvent.beacon().hwid())
                    .isEqualTo("374591320");
            Assertions.assertThat(beaconEvent.beacon().type())
                    .isEqualTo(BeaconContent.Type.ENTER);
            Assertions.assertThat(beaconEvent.beacon().dm())
                    .isNull();
        });
    }

    @Test
    public void testBeaconWithDeviceMessage() throws IOException {
        parse("callback/beacon_with_dm.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Assertions.assertThat(callbackRequest.events()).hasSize(1);
            Event event = callbackRequest.events().get(0);
            assertThat(event).isInstanceOf(BeaconEvent.class);
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.ACTIVE);

            BeaconEvent beaconEvent = (BeaconEvent) event;

            Assertions.assertThat(beaconEvent.source())
                    .isInstanceOf(UserSource.class);
            Assertions.assertThat(beaconEvent.source().userId())
                    .isEqualTo("U012345678901234567890123456789ab");
            Assertions.assertThat(beaconEvent.timestamp())
                    .isEqualTo(Instant.parse("2017-04-24T00:00:00Z").toEpochMilli());
            Assertions.assertThat(beaconEvent.beacon().hwid())
                    .isEqualTo("374591320");
            Assertions.assertThat(beaconEvent.beacon().type())
                    .isEqualTo(BeaconContent.Type.ENTER);
            Assertions.assertThat(beaconEvent.beacon().dm())
                    .isEqualTo("1234567890abcdef");
        });
    }

    @Test
    public void testAccountLink() throws IOException {
        parse("callback/account_link.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Assertions.assertThat(callbackRequest.events()).hasSize(1);
            Event event = callbackRequest.events().get(0);
            assertThat(event).isInstanceOf(AccountLinkEvent.class);
            Assertions.assertThat(event.source())
                    .isInstanceOf(UserSource.class);
            Assertions.assertThat(event.source().userId())
                    .isEqualTo("U012345678901234567890123456789ab");
            Assertions.assertThat(event.timestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z").toEpochMilli());
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.ACTIVE);

            AccountLinkEvent accountLinkEvent = (AccountLinkEvent) event;
            Assertions.assertThat(accountLinkEvent.link().result())
                    .isEqualTo(LinkContent.Result.OK);
            Assertions.assertThat(accountLinkEvent.link().nonce())
                    .isEqualTo("xxxxxxxxxxxxxxx");
        });
    }
    @Test
    public void testMemberJoined() throws IOException {
        parse("callback/member_joined.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Event event = callbackRequest.events().get(0);
            Assertions.assertThat(event.source()).isInstanceOf(GroupSource.class);
            assertThat(event).isInstanceOf(MemberJoinedEvent.class);
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.ACTIVE);

            MemberJoinedEvent memberJoinedEvent = (MemberJoinedEvent) event;
            String uids = memberJoinedEvent.joined().members().stream()
                    .map(Source::userId)
                    .collect(Collectors.joining(","));
            assertThat(uids).isEqualTo("U111111");
        });
    }

    @Test
    public void testMemberLeft() throws IOException {
        parse("callback/member_left.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Event event = callbackRequest.events().get(0);
            Assertions.assertThat(event.source()).isInstanceOf(GroupSource.class);
            assertThat(event).isInstanceOf(MemberLeftEvent.class);
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.ACTIVE);

            MemberLeftEvent memberLeftEvent = (MemberLeftEvent) event;
            String uids = memberLeftEvent.left().members().stream()
                    .map(Source::userId)
                    .collect(Collectors.joining(","));
            assertThat(uids).isEqualTo("U111111");
        });
    }

    @Test
    public void testUnsend() throws IOException {
        parse("callback/unsend.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Event event = callbackRequest.events().get(0);
            Assertions.assertThat(event.source()).isInstanceOf(UserSource.class);
            assertThat(event).isInstanceOf(UnsendEvent.class);
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.ACTIVE);

            UnsendEvent unsendEvent = (UnsendEvent) event;
            String messageId = unsendEvent.unsend().messageId();
            assertThat(messageId).isEqualTo("325708");
        });
    }

    @Test
    public void testVideo() throws IOException {
        parse("callback/video.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Event event = callbackRequest.events().get(0);
            Assertions.assertThat(event.source()).isInstanceOf(UserSource.class);
            assertThat(event).isInstanceOf(MessageEvent.class);
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.ACTIVE);

            MessageEvent messageEvent = (MessageEvent) event;
            VideoMessageContent videoMessageContent = (VideoMessageContent) messageEvent.message();
            Assertions.assertThat(videoMessageContent.duration()).isEqualTo(60000L);
        });
    }

    @Test
    public void testVideoPlayComplete() throws IOException {
        parse("callback/video-play-complete.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Event event = callbackRequest.events().get(0);
            Assertions.assertThat(event.source()).isInstanceOf(UserSource.class);
            assertThat(event).isInstanceOf(VideoPlayCompleteEvent.class);
            Assertions.assertThat(event.mode())
                    .isEqualTo(EventMode.ACTIVE);

            VideoPlayCompleteEvent videoPlayCompleteEvent = (VideoPlayCompleteEvent) event;
            Assertions.assertThat(videoPlayCompleteEvent.videoPlayComplete().trackingId())
                    .isEqualTo("track_id");
        });
    }

    // Event, that has brand new eventType
    @Test
    public void testUnknown() throws IOException {
        parse("callback/unknown.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Assertions.assertThat(callbackRequest.events()).hasSize(2);

            Event event1 = callbackRequest.events().get(0);
            assertThat(event1).isInstanceOf(UnknownEvent.class);
            Assertions.assertThat(event1.source())
                    .isInstanceOf(UserSource.class);
            Assertions.assertThat(event1.source().userId())
                    .isEqualTo("U012345678901234567890123456789ab");
            Assertions.assertThat(event1.timestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z").toEpochMilli());
            Assertions.assertThat(((UnknownEvent) event1).type())
                    .isEqualTo("greatNewFeature");
            Assertions.assertThat(event1.mode())
                    .isEqualTo(EventMode.ACTIVE);

            Event event2 = callbackRequest.events().get(1);
            assertThat(event2).isInstanceOf(MessageEvent.class);
            Assertions.assertThat(event2.source()).isInstanceOf(UnknownSource.class);
            Assertions.assertThat(event2.mode())
                    .isEqualTo(EventMode.ACTIVE);
            MessageEvent messageEvent = (MessageEvent) event2;
            Assertions.assertThat(messageEvent.message()).isInstanceOf(UnknownMessageContent.class);
        });
    }

    @Test
    public void testWebhookRedelivery() throws IOException {
        parse("callback/webhook-redelivery.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Event event = callbackRequest.events().get(0);

            MessageEvent messageEvent = (MessageEvent) event;
            Assertions.assertThat(messageEvent.webhookEventId())
                    .isEqualTo("01G2KZKG2DS765NMRH3GZFD8AP");
            Assertions.assertThat(messageEvent.deliveryContext().isRedelivery())
                    .isEqualTo(true);
        });
    }
}
