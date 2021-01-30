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

package com.linecorp.bot.model.event;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.event.link.LinkContent;
import com.linecorp.bot.model.event.message.ContentProvider;
import com.linecorp.bot.model.event.message.FileMessageContent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent.StickerResourceType;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent.Emoji;
import com.linecorp.bot.model.event.message.TextMessageContent.Mention;
import com.linecorp.bot.model.event.message.TextMessageContent.Mention.Mentionee;
import com.linecorp.bot.model.event.message.UnknownMessageContent;
import com.linecorp.bot.model.event.message.VideoMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.event.source.UnknownSource;
import com.linecorp.bot.model.event.source.UserSource;
import com.linecorp.bot.model.event.things.LinkThingsContent;
import com.linecorp.bot.model.event.things.ScenarioResultThingsContent;
import com.linecorp.bot.model.event.things.UnknownLineThingsContent;
import com.linecorp.bot.model.event.things.UnlinkThingsContent;
import com.linecorp.bot.model.event.things.result.BinaryActionResult;
import com.linecorp.bot.model.event.things.result.ScenarioResult;
import com.linecorp.bot.model.event.things.result.UnknownActionResult;
import com.linecorp.bot.model.event.things.result.VoidActionResult;
import com.linecorp.bot.model.testutil.TestUtil;

public class CallbackRequestTest {
    @FunctionalInterface
    interface RequestTester {
        void call(CallbackRequest request) throws IOException;
    }

    private void parse(String resourceName, RequestTester callback) throws IOException {
        try (InputStream resource = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            String json = StreamUtils.copyToString(resource, StandardCharsets.UTF_8);
            ObjectMapper objectMapper = TestUtil.objectMapperWithProductionConfiguration(false);
            CallbackRequest callbackRequest = objectMapper.readValue(json, CallbackRequest.class);

            callback.call(callbackRequest);
        }
    }

    private static void assertDestination(CallbackRequest request) {
        assertThat(request.getDestination()).isEqualTo("Uab012345678901234567890123456789");
    }

    @Test
    public void textTextUser() throws IOException {
        parse("callback/text-user.json", callbackRequest -> {
            assertDestination(callbackRequest);
            assertThat(callbackRequest.getDestination()).isEqualTo("Uab012345678901234567890123456789");
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(UserSource.class);
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);

            MessageEvent messageEvent = (MessageEvent) event;
            assertThat(messageEvent.getReplyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
            MessageContent message = messageEvent.getMessage();
            assertThat(message).isInstanceOf(TextMessageContent.class);
        });
    }

    @Test
    public void textTextEmojis() throws IOException {
        parse("callback/text-emojis.json", callbackRequest -> {
            assertDestination(callbackRequest);
            assertThat(callbackRequest.getDestination()).isEqualTo("Uab012345678901234567890123456789");
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(UserSource.class);
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);

            MessageEvent messageEvent = (MessageEvent) event;
            assertThat(messageEvent.getReplyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
            MessageContent message = messageEvent.getMessage();
            assertThat(message).isInstanceOf(TextMessageContent.class);

            List<Emoji> emojis = ((TextMessageContent) message).getEmojis();
            assertThat(emojis).hasSize(1);
            assertThat(emojis.get(0)).isInstanceOf(TextMessageContent.Emoji.class);
            assertThat(emojis.get(0).getIndex()).isEqualTo(14);
            assertThat(emojis.get(0).getLength()).isEqualTo(6);
            assertThat(emojis.get(0).getProductId()).isEqualTo("5ac1bfd5040ab15980c9b435");
            assertThat(emojis.get(0).getEmojiId()).isEqualTo("001");
        });
    }

    @Test
    public void textTextMention() throws IOException {
        parse("callback/text-mention.json", callbackRequest -> {
            assertDestination(callbackRequest);
            assertThat(callbackRequest.getDestination()).isEqualTo("Uab012345678901234567890123456789");
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(UserSource.class);
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);

            MessageEvent messageEvent = (MessageEvent) event;
            assertThat(messageEvent.getReplyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
            MessageContent message = messageEvent.getMessage();
            assertThat(message).isInstanceOf(TextMessageContent.class);

            Mention mention = ((TextMessageContent) message).getMention();
            assertThat(mention).isInstanceOf(TextMessageContent.Mention.class);
            assertThat(mention.getMentionees()).isNotNull();

            List<Mentionee> mentionees = mention.getMentionees();
            assertThat(mentionees).hasSize(2);

            Mentionee mentioneeBrown = mentionees.get(0);
            assertThat(mentioneeBrown).isInstanceOf(TextMessageContent.Mention.Mentionee.class);
            assertThat(mentioneeBrown.getIndex()).isEqualTo(13);
            assertThat(mentioneeBrown.getLength()).isEqualTo(6);
            assertThat(mentioneeBrown.getUserId()).isEqualTo("U12345678901234567890123456780");

            Mentionee mentioneeCony = mentionees.get(1);
            assertThat(mentioneeCony).isInstanceOf(TextMessageContent.Mention.Mentionee.class);
            assertThat(mentioneeCony.getIndex()).isEqualTo(24);
            assertThat(mentioneeCony.getLength()).isEqualTo(5);
            assertThat(mentioneeCony.getUserId()).isNull();
        });
    }

    @Test
    public void textTextGroup() throws IOException {
        parse("callback/text-group.json", callbackRequest -> {
            assertDestination(callbackRequest);
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(GroupSource.class);
            assertThat(event.getSource().getUserId())
                    .isNull();
            assertThat(event.getMode())
                    .isEqualTo(EventMode.STANDBY);

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
            assertDestination(callbackRequest);
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(UserSource.class);
            assertThat(event.getSource().getUserId())
                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);

            MessageEvent messageEvent = (MessageEvent) event;
            assertThat(messageEvent.getReplyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
            MessageContent message = messageEvent.getMessage();
            assertThat(message).isInstanceOf(ImageMessageContent.class);
            ImageMessageContent image = (ImageMessageContent) message;
            assertThat(image.getId()).isEqualTo("325708");
            assertThat(image.getContentProvider()).isEqualTo(
                    ContentProvider.builder()
                                   .type("external")
                                   .originalContentUrl(URI.create("https://example.com/original.jpg"))
                                   .previewImageUrl(URI.create(("https://example.com/preview.jpg")))
                                   .build());
        });
    }

    @Test
    public void testLocation() throws IOException {
        parse("callback/location.json", callbackRequest -> {
            assertDestination(callbackRequest);
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(UserSource.class);
            assertThat(event.getSource().getUserId())
                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
            assertThat(event.getTimestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);

            MessageEvent messageEvent = (MessageEvent) event;
            assertThat(messageEvent.getReplyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
            MessageContent message = messageEvent.getMessage();
            assertThat(message).isInstanceOf(LocationMessageContent.class);
            if (message instanceof LocationMessageContent) {
                assertThat(((LocationMessageContent) message).getAddress())
                        .isEqualTo("〒150-0002 東京都渋谷区渋谷２丁目２１−１");
            }
        });
    }

    @Test
    public void testFile() throws IOException {
        parse("callback/file.json", callbackRequest -> {
            assertDestination(callbackRequest);
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(UserSource.class);
            assertThat(event.getSource().getUserId())
                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);

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
            assertDestination(callbackRequest);
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(MessageEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(UserSource.class);
            assertThat(event.getSource().getUserId())
                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
            assertThat(event.getTimestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);

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
                assertThat(((StickerMessageContent) message).getStickerResourceType())
                        .isEqualTo(StickerResourceType.STATIC);
                assertThat(((StickerMessageContent) message).getKeywords())
                        .containsExactly("bed", "sleep", "bedtime");
            }
        });
    }

    @Test
    public void testStickerKeywordsBecomeString() throws IOException {
        parse("callback/sticker-keywords-string.json", callbackRequest -> {
            MessageEvent messageEvent = (MessageEvent) callbackRequest.getEvents().get(0);
            MessageContent message = messageEvent.getMessage();
            if (message instanceof StickerMessageContent) {
                assertThat(((StickerMessageContent) message).getKeywords()).isNull();
            }
        });
    }

    @Test
    public void testStickerKeywordsBecomeMap() throws IOException {
        parse("callback/sticker-keywords-map.json", callbackRequest -> {
            MessageEvent messageEvent = (MessageEvent) callbackRequest.getEvents().get(0);
            MessageContent message = messageEvent.getMessage();
            if (message instanceof StickerMessageContent) {
                assertThat(((StickerMessageContent) message).getKeywords()).isNull();
            }
        });
    }

    @Test
    public void testStickerKeywordsRemoved() throws IOException {
        parse("callback/sticker-keywords-remove.json", callbackRequest -> {
            MessageEvent messageEvent = (MessageEvent) callbackRequest.getEvents().get(0);
            MessageContent message = messageEvent.getMessage();
            if (message instanceof StickerMessageContent) {
                assertThat(((StickerMessageContent) message).getKeywords()).isNull();
            }
        });
    }

    @Test
    public void testFollow() throws IOException {
        parse("callback/follow.json", callbackRequest -> {
            assertDestination(callbackRequest);
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(FollowEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(UserSource.class);
            assertThat(event.getSource().getUserId())
                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
            assertThat(event.getTimestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);

            FollowEvent followEvent = (FollowEvent) event;
            assertThat(followEvent.getReplyToken())
                    .isEqualTo("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA");
        });
    }

    @Test
    public void testUnfollow() throws IOException {
        parse("callback/unfollow.json", callbackRequest -> {
            assertDestination(callbackRequest);
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(UnfollowEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(UserSource.class);
            assertThat(event.getSource().getUserId())
                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
            assertThat(event.getTimestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);
        });
    }

    @Test
    public void testJoin() throws IOException {
        parse("callback/join.json", callbackRequest -> {
            assertDestination(callbackRequest);
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(JoinEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(GroupSource.class);
            assertThat(((GroupSource) event.getSource()).getGroupId())
                    .isEqualTo("cxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            assertThat(event.getTimestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);
        });
    }

    @Test
    public void testLeave() throws IOException {
        parse("callback/leave.json", callbackRequest -> {
            assertDestination(callbackRequest);
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(LeaveEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(GroupSource.class);
            assertThat(((GroupSource) event.getSource()).getGroupId())
                    .isEqualTo("cxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            assertThat(event.getTimestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);
        });
    }

    @Test
    public void testPostback() throws IOException {
        parse("callback/postback.json", callbackRequest -> {
            assertDestination(callbackRequest);
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(PostbackEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(UserSource.class);
            assertThat(event.getSource().getUserId())
                    .isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
            assertThat(event.getTimestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);

            PostbackEvent postbackEvent = (PostbackEvent) event;
            assertThat(postbackEvent.getPostbackContent().getData())
                    .isEqualTo("action=buyItem&itemId=123123&color=red");
        });
    }

    @Test
    public void testBeacon() throws IOException {
        parse("callback/beacon.json", callbackRequest -> {
            assertDestination(callbackRequest);
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(BeaconEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(UserSource.class);
            assertThat(event.getSource().getUserId())
                    .isEqualTo("U012345678901234567890123456789ab");
            assertThat(event.getTimestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);

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
            assertDestination(callbackRequest);
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(BeaconEvent.class);
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);

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

    @Test
    public void testAccountLink() throws IOException {
        parse("callback/account_link.json", callbackRequest -> {
            assertDestination(callbackRequest);
            assertThat(callbackRequest.getEvents()).hasSize(1);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(AccountLinkEvent.class);
            assertThat(event.getSource())
                    .isInstanceOf(UserSource.class);
            assertThat(event.getSource().getUserId())
                    .isEqualTo("U012345678901234567890123456789ab");
            assertThat(event.getTimestamp())
                    .isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);

            AccountLinkEvent accountLinkEvent = (AccountLinkEvent) event;
            assertThat(accountLinkEvent.getLink().getResult())
                    .isEqualTo(LinkContent.Result.OK);
            assertThat(accountLinkEvent.getLink().getNonce())
                    .isEqualTo("xxxxxxxxxxxxxxx");
        });
    }

    @Test
    public void testLineThingsLink() throws IOException {
        parse("callback/line-things-link.json", callbackRequest -> {
            assertThat(callbackRequest.getEvents()).hasSize(1);
            final ThingsEvent event = (ThingsEvent) callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(ThingsEvent.class);
            assertThat(event.getSource()).isInstanceOf(UserSource.class);
            assertThat(event.getSource().getUserId()).isEqualTo("U012345678901234567890123456789ab");
            assertThat(event.getTimestamp()).isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);

            final LinkThingsContent things = (LinkThingsContent) event.getThings();
            assertThat(things.getDeviceId()).isEqualTo("t016560bc3fb1e42b9fe9293ca6e2db71");
        });
    }

    @Test
    public void testLineThingsUnlink() throws IOException {
        parse("callback/line-things-unlink.json", callbackRequest -> {
            assertThat(callbackRequest.getEvents()).hasSize(1);
            final ThingsEvent event = (ThingsEvent) callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(ThingsEvent.class);
            assertThat(event.getSource()).isInstanceOf(UserSource.class);
            assertThat(event.getSource().getUserId()).isEqualTo("U012345678901234567890123456789ab");
            assertThat(event.getTimestamp()).isEqualTo(Instant.ofEpochMilli(1462629479859L));
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);

            final UnlinkThingsContent things = (UnlinkThingsContent) event.getThings();
            assertThat(things.getDeviceId()).isEqualTo("t016560bc3fb1e42b9fe9293ca6e2db71");
        });
    }

    @Test
    public void testLineThingsScenarioResult() throws IOException {
        parse("callback/line-things-scenario-result.json", callbackRequest -> {
            assertThat(callbackRequest.getEvents()).hasSize(1);
            final ThingsEvent event = (ThingsEvent) callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(ThingsEvent.class);
            assertThat(event.getSource()).isInstanceOf(UserSource.class);
            assertThat(event.getSource().getUserId()).isEqualTo("uXXX");
            assertThat(event.getTimestamp()).isEqualTo(Instant.ofEpochMilli(1547817848122L));
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);

            final ScenarioResultThingsContent things = (ScenarioResultThingsContent) event.getThings();
            assertThat(things.getDeviceId()).isEqualTo("tXXX");

            final ScenarioResult result = things.getResult();
            assertThat(result.getScenarioId()).isEqualTo("XXX");
            assertThat(result.getRevision()).isEqualTo(2);
            assertThat(result.getStartTime()).isEqualTo(Instant.ofEpochMilli(1547817845950L));
            assertThat(result.getEndTime()).isEqualTo(Instant.ofEpochMilli(1547817845952L));
            assertThat(result.getResultCode()).isEqualTo("success");
            assertThat(result.getActionResults().get(0)).isEqualTo(
                    BinaryActionResult.builder()
                                      .data("/w==")
                                      .build());
            assertThat(result.getActionResults().get(1)).isInstanceOf(VoidActionResult.class);
            assertThat(result.getActionResults().get(2)).isInstanceOf(UnknownActionResult.class);
            assertThat(result.getBleNotificationPayload()).isEqualTo("AQ==");
            assertThat(result.getErrorReason()).isEqualTo(null);
        });
    }

    @Test
    public void testLineThingsUnknown() throws IOException {
        parse("callback/line-things-unknown.json", callbackRequest -> {
            assertThat(callbackRequest.getEvents()).hasSize(1);
            final ThingsEvent event = (ThingsEvent) callbackRequest.getEvents().get(0);
            assertThat(event).isInstanceOf(ThingsEvent.class);
            assertThat(event.getSource()).isInstanceOf(UserSource.class);
            assertThat(event.getSource().getUserId()).isEqualTo("U012345678901234567890123456789ab");
            assertThat(event.getTimestamp()).isEqualTo(Instant.ofEpochMilli(1462629479859L));
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);

            assertThat(event.getThings()).isInstanceOf(UnknownLineThingsContent.class);
        });
    }

    @Test
    public void testMemberJoined() throws IOException {
        parse("callback/member_joined.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event.getSource()).isInstanceOf(GroupSource.class);
            assertThat(event).isInstanceOf(MemberJoinedEvent.class);
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);

            MemberJoinedEvent memberJoinedEvent = (MemberJoinedEvent) event;
            String uids = memberJoinedEvent.getJoined().getMembers().stream()
                                           .map(Source::getUserId)
                                           .collect(Collectors.joining(","));
            assertThat(uids).isEqualTo("U111111");
        });
    }

    @Test
    public void testMemberLeft() throws IOException {
        parse("callback/member_left.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event.getSource()).isInstanceOf(GroupSource.class);
            assertThat(event).isInstanceOf(MemberLeftEvent.class);
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);

            MemberLeftEvent memberLeftEvent = (MemberLeftEvent) event;
            String uids = memberLeftEvent.getLeft().getMembers().stream()
                                         .map(Source::getUserId)
                                         .collect(Collectors.joining(","));
            assertThat(uids).isEqualTo("U111111");
        });
    }

    @Test
    public void testUnsend() throws IOException {
        parse("callback/unsend.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event.getSource()).isInstanceOf(UserSource.class);
            assertThat(event).isInstanceOf(UnsendEvent.class);
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);

            UnsendEvent unsendEvent = (UnsendEvent) event;
            String messageId = unsendEvent.getUnsend().getMessageId();
            assertThat(messageId).isEqualTo("325708");
        });
    }

    @Test
    public void testVideo() throws IOException {
        parse("callback/video.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event.getSource()).isInstanceOf(UserSource.class);
            assertThat(event).isInstanceOf(MessageEvent.class);
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);

            MessageEvent messageEvent = (MessageEvent) event;
            VideoMessageContent videoMessageContent = (VideoMessageContent) messageEvent.getMessage();
            assertThat(videoMessageContent.getDuration()).isEqualTo(60000L);
        });
    }

    @Test
    public void testVideoPlayComplete() throws IOException {
        parse("callback/video-play-complete.json", callbackRequest -> {
            assertDestination(callbackRequest);
            Event event = callbackRequest.getEvents().get(0);
            assertThat(event.getSource()).isInstanceOf(UserSource.class);
            assertThat(event).isInstanceOf(VideoPlayCompleteEvent.class);
            assertThat(event.getMode())
                    .isEqualTo(EventMode.ACTIVE);

            VideoPlayCompleteEvent videoPlayCompleteEvent = (VideoPlayCompleteEvent) event;
            assertThat(videoPlayCompleteEvent.getVideoPlayComplete().getTrackingId())
                    .isEqualTo("track_id");
        });
    }

    // Event, that has brand new eventType
    @Test
    public void testUnknown() throws IOException {
        parse("callback/unknown.json", callbackRequest -> {
            assertDestination(callbackRequest);
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
            assertThat(event1.getMode())
                    .isEqualTo(EventMode.ACTIVE);

            Event event2 = callbackRequest.getEvents().get(1);
            assertThat(event2).isInstanceOf(MessageEvent.class);
            assertThat(event2.getSource()).isInstanceOf(UnknownSource.class);
            assertThat(event2.getMode())
                    .isEqualTo(EventMode.ACTIVE);
            MessageEvent messageEvent = (MessageEvent) event2;
            assertThat(messageEvent.getMessage()).isInstanceOf(UnknownMessageContent.class);
        });
    }
}
