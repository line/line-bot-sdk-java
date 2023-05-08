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

package com.example.bot.spring;

import static java.util.Collections.singletonList;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.linecorp.bot.client.base.BlobContent;
import com.linecorp.bot.client.base.Result;
import com.linecorp.bot.messaging.client.MessagingApiBlobClient;
import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.linecorp.bot.messaging.model.AudioMessage;
import com.linecorp.bot.messaging.model.ButtonsTemplate;
import com.linecorp.bot.messaging.model.CarouselColumn;
import com.linecorp.bot.messaging.model.CarouselTemplate;
import com.linecorp.bot.messaging.model.ConfirmTemplate;
import com.linecorp.bot.messaging.model.DatetimePickerAction;
import com.linecorp.bot.messaging.model.GroupMemberCountResponse;
import com.linecorp.bot.messaging.model.GroupSummaryResponse;
import com.linecorp.bot.messaging.model.GroupUserProfileResponse;
import com.linecorp.bot.messaging.model.ImageCarouselColumn;
import com.linecorp.bot.messaging.model.ImageCarouselTemplate;
import com.linecorp.bot.messaging.model.ImageMessage;
import com.linecorp.bot.messaging.model.ImagemapArea;
import com.linecorp.bot.messaging.model.ImagemapBaseSize;
import com.linecorp.bot.messaging.model.ImagemapExternalLink;
import com.linecorp.bot.messaging.model.ImagemapMessage;
import com.linecorp.bot.messaging.model.ImagemapVideo;
import com.linecorp.bot.messaging.model.LocationMessage;
import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.messaging.model.MessageAction;
import com.linecorp.bot.messaging.model.MessageImagemapAction;
import com.linecorp.bot.messaging.model.PostbackAction;
import com.linecorp.bot.messaging.model.ReplyMessageRequest;
import com.linecorp.bot.messaging.model.RoomMemberCountResponse;
import com.linecorp.bot.messaging.model.Sender;
import com.linecorp.bot.messaging.model.StickerMessage;
import com.linecorp.bot.messaging.model.TemplateMessage;
import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.messaging.model.URIAction;
import com.linecorp.bot.messaging.model.URIImagemapAction;
import com.linecorp.bot.messaging.model.UserProfileResponse;
import com.linecorp.bot.messaging.model.VideoMessage;
import com.linecorp.bot.spring.boot.handler.annotation.EventMapping;
import com.linecorp.bot.spring.boot.handler.annotation.LineMessageHandler;
import com.linecorp.bot.webhook.model.AudioMessageContent;
import com.linecorp.bot.webhook.model.BeaconEvent;
import com.linecorp.bot.webhook.model.ContentProvider;
import com.linecorp.bot.webhook.model.Event;
import com.linecorp.bot.webhook.model.FileMessageContent;
import com.linecorp.bot.webhook.model.FollowEvent;
import com.linecorp.bot.webhook.model.GroupSource;
import com.linecorp.bot.webhook.model.ImageMessageContent;
import com.linecorp.bot.webhook.model.JoinEvent;
import com.linecorp.bot.webhook.model.LocationMessageContent;
import com.linecorp.bot.webhook.model.MemberJoinedEvent;
import com.linecorp.bot.webhook.model.MemberLeftEvent;
import com.linecorp.bot.webhook.model.MessageEvent;
import com.linecorp.bot.webhook.model.PostbackEvent;
import com.linecorp.bot.webhook.model.RoomSource;
import com.linecorp.bot.webhook.model.Source;
import com.linecorp.bot.webhook.model.StickerMessageContent;
import com.linecorp.bot.webhook.model.TextMessageContent;
import com.linecorp.bot.webhook.model.UnfollowEvent;
import com.linecorp.bot.webhook.model.UnknownEvent;
import com.linecorp.bot.webhook.model.UnsendEvent;
import com.linecorp.bot.webhook.model.VideoMessageContent;
import com.linecorp.bot.webhook.model.VideoPlayCompleteEvent;

@LineMessageHandler
public class KitchenSinkController {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(KitchenSinkController.class);

    private final MessagingApiClient messagingApiClient;

    private final MessagingApiBlobClient messagingApiBlobClient;

    public KitchenSinkController(
            MessagingApiClient messagingApiClient,
            MessagingApiBlobClient messagingApiBlobClient) {
        this.messagingApiClient = messagingApiClient;
        this.messagingApiBlobClient = messagingApiBlobClient;
    }

    @EventMapping
    public void handleTextMessageEvent(MessageEvent event,
                                       TextMessageContent textMessageContent) throws Exception {
        handleTextContent(event.replyToken(), event, textMessageContent);
    }

    @EventMapping
    public void handleStickerMessageEvent(MessageEvent event,
                                          StickerMessageContent stickerMessageContent) throws IOException {
        // this operation may throw exception if the sticker is not listed on https://developers.line.biz/en/docs/messaging-api/sticker-list/.
        reply(event.replyToken(), new StickerMessage(
                stickerMessageContent.packageId(), stickerMessageContent.stickerId())
        );
    }

    @EventMapping
    public void handleLocationMessageEvent(MessageEvent event,
                                           LocationMessageContent locationMessageContent) throws IOException {
        // title is the optional field in webhook event. but it's required field in reply API.
        String title = locationMessageContent.title();
        if (title == null) {
            title = "Your Location";
        }
        reply(event.replyToken(), new LocationMessage(
                null,
                null,
                title,
                locationMessageContent.address(),
                locationMessageContent.latitude(),
                locationMessageContent.longitude()));
    }

    @EventMapping
    public void handleImageMessageEvent(MessageEvent event,
                                        ImageMessageContent imageMessageContent) throws IOException {
        // You need to install ImageMagick
        handleHeavyContent(
                event.replyToken(),
                imageMessageContent.id(),
                responseBody -> {
                    final ContentProvider provider = imageMessageContent.contentProvider();
                    final DownloadedContent jpg;
                    final DownloadedContent previewImg;
                    if (provider.type() == ContentProvider.Type.EXTERNAL) {
                        jpg = new DownloadedContent(null, provider.originalContentUrl());
                        previewImg = new DownloadedContent(null, provider.previewImageUrl());
                    } else {
                        jpg = saveContent("jpg", responseBody);
                        previewImg = createTempFile("jpg");
                        system(
                                "convert",
                                "-resize", "240x",
                                jpg.path.toString(),
                                previewImg.path.toString());
                    }
                    reply(event.replyToken(),
                            new ImageMessage(jpg.uri(), previewImg.uri()));
                });
    }

    @EventMapping
    public void handleAudioMessageEvent(MessageEvent event,
                                        AudioMessageContent audioMessageContent) throws IOException {
        handleHeavyContent(
                event.replyToken(),
                audioMessageContent.id(),
                responseBody -> {
                    final ContentProvider provider = audioMessageContent.contentProvider();
                    final DownloadedContent mp4;
                    if (provider.type() == ContentProvider.Type.EXTERNAL) {
                        mp4 = new DownloadedContent(null, provider.originalContentUrl());
                    } else {
                        mp4 = saveContent("mp4", responseBody);
                    }
                    reply(event.replyToken(), new AudioMessage(mp4.uri(), 100L));
                });
    }

    @EventMapping
    public void handleVideoMessageEvent(MessageEvent event,
                                        VideoMessageContent videoMessageContent) throws IOException {
        log.info("Got video message: duration={}ms", videoMessageContent.duration());

        // You need to install ffmpeg and ImageMagick.
        handleHeavyContent(
                event.replyToken(),
                videoMessageContent.id(),
                responseBody -> {
                    final ContentProvider provider = videoMessageContent.contentProvider();
                    final DownloadedContent mp4;
                    final DownloadedContent previewImg;
                    if (provider.type() == ContentProvider.Type.EXTERNAL) {
                        mp4 = new DownloadedContent(null, provider.originalContentUrl());
                        previewImg = new DownloadedContent(null, provider.previewImageUrl());
                    } else {
                        mp4 = saveContent("mp4", responseBody);
                        previewImg = createTempFile("jpg");
                        system("convert",
                                mp4.path + "[0]",
                                previewImg.path.toString());
                    }
                    String trackingId = UUID.randomUUID().toString();
                    log.info("Sending video message with trackingId={}", trackingId);
                    reply(event.replyToken(),
                            new VideoMessage(mp4.uri(), previewImg.uri, trackingId));
                });
    }

    @EventMapping
    public void handleVideoPlayCompleteEvent(VideoPlayCompleteEvent event) {
        log.info("Got video play complete: tracking id={}", event.videoPlayComplete().trackingId());
        this.replyText(event.replyToken(),
                "You played " + event.videoPlayComplete().trackingId());
    }

    @EventMapping
    public void handleFileMessageEvent(MessageEvent event, FileMessageContent fileMessageContent) {
        this.reply(event.replyToken(),
                new TextMessage(String.format("Received '%s'(%d bytes)",
                        fileMessageContent.fileName(),
                        fileMessageContent.fileSize())));
    }

    @EventMapping
    public void handleUnfollowEvent(UnfollowEvent event) {
        log.info("unfollowed this bot: {}", event);
    }

    @EventMapping
    public void handleUnknownEvent(UnknownEvent event) {
        log.info("Got an unknown event!!!!! : {}", event);
    }

    @EventMapping
    public void handleFollowEvent(FollowEvent event) {
        String replyToken = event.replyToken();
        this.replyText(replyToken, "Got followed event");
    }

    @EventMapping
    public void handleJoinEvent(JoinEvent event) {
        String replyToken = event.replyToken();
        this.replyText(replyToken, "Joined " + event.source());
    }

    @EventMapping
    public void handlePostbackEvent(PostbackEvent event) {
        String replyToken = event.replyToken();
        this.replyText(replyToken,
                "Got postback data " + event.postback().data() + ", param " + event
                        .postback().params());
    }

    @EventMapping
    public void handleBeaconEvent(BeaconEvent event) {
        String replyToken = event.replyToken();
        this.replyText(replyToken, "Got beacon message " + event.beacon().hwid());
    }

    @EventMapping
    public void handleMemberJoined(MemberJoinedEvent event) {
        String replyToken = event.replyToken();
        this.replyText(replyToken, "Got memberJoined message " + event.joined().members()
                .stream().map(Source::userId)
                .collect(Collectors.joining(",")));
    }

    @EventMapping
    public void handleMemberLeft(MemberLeftEvent event) {
        log.info("Got memberLeft message: {}", event.left().members()
                .stream().map(Source::userId)
                .collect(Collectors.joining(",")));
    }

    @EventMapping
    public void handleMemberLeft(UnsendEvent event) {
        log.info("Got unsend event: {}", event);
    }

    @EventMapping
    public void handleOtherEvent(Event event) {
        log.info("Received message(Ignored): {}", event);
    }

    private void reply(String replyToken, Message message) {
        Objects.requireNonNull(replyToken, "replyToken");
        Objects.requireNonNull(message, "message");
        reply(replyToken, singletonList(message));
    }

    private void reply(String replyToken, List<Message> messages) {
        Objects.requireNonNull(replyToken, "replyToken");
        Objects.requireNonNull(messages, "messages");
        reply(replyToken, messages, false);
    }

    private void reply(String replyToken,
                       List<Message> messages,
                       boolean notificationDisabled) {
        try {
            Objects.requireNonNull(replyToken, "replyToken");
            Objects.requireNonNull(messages, "messages");
            Result<Void> apiResponse = messagingApiClient
                    .replyMessage(new ReplyMessageRequest(replyToken, messages, notificationDisabled))
                    .get();
            log.info("Sent messages: {}", apiResponse);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void replyText(String replyToken, String message) {
        Objects.requireNonNull(replyToken, "replyToken");
        Objects.requireNonNull(message, "message");
        if (replyToken.isEmpty()) {
            throw new IllegalArgumentException("replyToken must not be empty");
        }
        if (message.length() > 1000) {
            message = message.substring(0, 1000 - 2) + "……";
        }
        this.reply(replyToken, new TextMessage(message));
    }

    private void handleHeavyContent(String replyToken, String messageId,
                                    Consumer<BlobContent> messageConsumer) {
        try {
            BlobContent response = messagingApiBlobClient.getMessageContent(messageId)
                    .get().body();
            messageConsumer.accept(response);
        } catch (InterruptedException | ExecutionException e) {
            reply(replyToken, new TextMessage("Cannot get image: " + e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    private void handleTextContent(String replyToken, MessageEvent event, TextMessageContent content)
            throws Exception {
        final String text = content.text();

        log.info("Got text message from replyToken:{}: text:{} emojis:{}", replyToken, text,
                content.emojis());
        switch (text) {
            case "profile" -> {
                log.info("Invoking 'profile' command: source:{}",
                        event.source());
                final String userId = event.source().userId();
                if (userId != null) {
                    if (event.source() instanceof GroupSource) {
                        messagingApiClient
                                .getGroupMemberProfile(((GroupSource) event.source()).groupId(), userId)
                                .whenComplete((result, throwable) -> {
                                    if (throwable != null) {
                                        this.replyText(replyToken, throwable.getMessage());
                                        return;
                                    }
                                    GroupUserProfileResponse profile = result.body();

                                    this.reply(
                                            replyToken,
                                            Arrays.asList(new TextMessage("(from group)"),
                                                    new TextMessage(
                                                            "Display name: " + profile.displayName()),
                                                    new ImageMessage(profile.pictureUrl(),
                                                            profile.pictureUrl()))
                                    );
                                });
                    } else {
                        messagingApiClient
                                .getProfile(userId)
                                .whenComplete((result, throwable) -> {
                                    if (throwable != null) {
                                        this.replyText(replyToken, throwable.getMessage());
                                        return;
                                    }
                                    UserProfileResponse profile = result.body();

                                    this.reply(
                                            replyToken,
                                            Arrays.asList(new TextMessage(
                                                            "Display name: " + profile.displayName()),
                                                    new TextMessage("Status message: "
                                                            + profile.statusMessage()))
                                    );

                                });
                    }
                } else {
                    this.replyText(replyToken, "Bot can't use profile API without user ID");
                }
            }
            case "bye" -> {
                Source source = event.source();
                if (source instanceof GroupSource) {
                    this.replyText(replyToken, "Leaving group");
                    messagingApiClient.leaveGroup(((GroupSource) source).groupId()).get();
                } else if (source instanceof RoomSource) {
                    this.replyText(replyToken, "Leaving room");
                    messagingApiClient.leaveRoom(((RoomSource) source).roomId()).get();
                } else {
                    this.replyText(replyToken, "Bot can't leave from 1:1 chat");
                }
            }
            case "group_summary" -> {
                Source source = event.source();
                if (source instanceof GroupSource) {
                    GroupSummaryResponse groupSummary = messagingApiClient.getGroupSummary(
                            ((GroupSource) source).groupId()).get().body();
                    this.replyText(replyToken, "Group summary: " + groupSummary);
                } else {
                    this.replyText(replyToken, "You can't use 'group_summary' command for "
                            + source);
                }
            }
            case "group_member_count" -> {
                Source source = event.source();
                if (source instanceof GroupSource) {
                    GroupMemberCountResponse groupMemberCountResponse = messagingApiClient.getGroupMemberCount(
                            ((GroupSource) source).groupId()).get().body();
                    this.replyText(replyToken, "Group member count: "
                            + groupMemberCountResponse.count());
                } else {
                    this.replyText(replyToken, "You can't use 'group_member_count' command  for "
                            + source);
                }
            }
            case "room_member_count" -> {
                Source source = event.source();
                if (source instanceof RoomSource) {
                    RoomMemberCountResponse roomMemberCountResponse = messagingApiClient.getRoomMemberCount(
                            ((RoomSource) source).roomId()).get().body();
                    this.replyText(replyToken, "Room member count: "
                            + roomMemberCountResponse.count());
                } else {
                    this.replyText(replyToken, "You can't use 'room_member_count' command  for "
                            + source);
                }
            }
            case "confirm" -> {
                ConfirmTemplate confirmTemplate = new ConfirmTemplate(
                        "Do it?",
                        List.of(
                                new MessageAction("Yes", "Yes!"),
                                new MessageAction("No", "No!")
                        )
                );
                TemplateMessage templateMessage = new TemplateMessage("Confirm alt text", confirmTemplate);
                this.reply(replyToken, templateMessage);
            }
            case "buttons" -> {
                URI imageUrl = createUri("/static/buttons/1040.jpg");
                ButtonsTemplate buttonsTemplate = new ButtonsTemplate(
                        imageUrl,
                        null,
                        null,
                        null,
                        "My button sample", // title
                        "Hello, my button", // text
                        null,
                        Arrays.asList(
                                new URIAction("Go to line.me",
                                        URI.create("https://line.me"), null),
                                new PostbackAction("Say hello1",
                                        "hello こんにちは",
                                        null,
                                        null,
                                        null,
                                        null),
                                new PostbackAction("言 hello2",
                                        "hello こんにちは",
                                        null,
                                        "hello こんにちは",
                                        null,
                                        null),
                                new MessageAction("Say message",
                                        "Rice=米")
                        ));
                TemplateMessage templateMessage = new TemplateMessage("Button alt text", buttonsTemplate);
                this.reply(replyToken, templateMessage);
            }
            case "carousel" -> {
                URI imageUrl = createUri("/static/buttons/1040.jpg");
                CarouselTemplate carouselTemplate = new CarouselTemplate(
                        Arrays.asList(
                                new CarouselColumn(imageUrl, null, "hoge", "fuga", null, List.of(
                                        new URIAction("Go to line.me",
                                                URI.create("https://line.me"), null),
                                        new URIAction("Go to line.me",
                                                URI.create("https://line.me"), null),
                                        new PostbackAction("Say hello1",
                                                "hello こんにちは",
                                                null,
                                                null,
                                                null,
                                                null)
                                )),
                                new CarouselColumn(imageUrl, null, "hoge", "fuga", null, List.of(
                                        new PostbackAction("言 hello2",
                                                "hello こんにちは",
                                                null,
                                                "hello こんにちは",
                                                null,
                                                null
                                        ),
                                        new PostbackAction("言 hello2",
                                                "hello こんにちは",
                                                null,
                                                "hello こんにちは",
                                                null,
                                                null),
                                        new MessageAction("Say message",
                                                "Rice=米")
                                )),
                                new CarouselColumn(imageUrl, null,
                                        "Datetime Picker",
                                        "Please select a date, time or datetime", null,
                                        Arrays.asList(
                                                new DatetimePickerAction(
                                                        "Datetime",
                                                        "action=sel",
                                                        DatetimePickerAction.Mode.DATETIME,
                                                        "2017-06-18T06:15",
                                                        "2100-12-31T23:59",
                                                        "1900-01-01T00:00"
                                                ),
                                                new DatetimePickerAction(
                                                        "Date",
                                                        "action=sel&only=date",
                                                        DatetimePickerAction.Mode.DATE,
                                                        "2017-06-18",
                                                        "2100-12-31",
                                                        "1900-01-01"
                                                ),
                                                new DatetimePickerAction(
                                                        "Time",
                                                        "action=sel&only=time",
                                                        DatetimePickerAction.Mode.TIME,
                                                        "06:15",
                                                        "23:59",
                                                        "00:00"
                                                )
                                        ))
                        ), null, null);
                TemplateMessage templateMessage = new TemplateMessage("Carousel alt text", carouselTemplate);
                this.reply(replyToken, templateMessage);
            }
            case "image_carousel" -> {
                URI imageUrl = createUri("/static/buttons/1040.jpg");
                ImageCarouselTemplate imageCarouselTemplate = new ImageCarouselTemplate(
                        Arrays.asList(
                                new ImageCarouselColumn(imageUrl,
                                        new URIAction("Goto line.me",
                                                URI.create("https://line.me"), null)
                                ),
                                new ImageCarouselColumn(imageUrl,
                                        new MessageAction("Say message",
                                                "Rice=米")
                                ),
                                new ImageCarouselColumn(imageUrl,
                                        new PostbackAction("言 hello3",
                                                "hello こんにちは",
                                                "hello こんにちは",
                                                null,
                                                null,
                                                null)
                                )
                        ));
                TemplateMessage templateMessage = new TemplateMessage("ImageCarousel alt text",
                        imageCarouselTemplate);
                this.reply(replyToken, templateMessage);
            }
            case "imagemap" -> this.reply(replyToken, new ImagemapMessage(
                    null,
                    null,
                    createUri("/static/rich"),
                    "This is alt text",
                    new ImagemapBaseSize(1040, 1040),
                    Arrays.asList(
                            new URIImagemapAction(new ImagemapArea(0, 0, 520, 520), "https://store.line.me/family/manga/en", null),
                            new URIImagemapAction(new ImagemapArea(520, 0, 520, 520), "https://store.line.me/family/music/en", null),
                            new URIImagemapAction(new ImagemapArea(0, 520, 520, 520), "https://store.line.me/family/play/en", null),
                            new MessageImagemapAction(new ImagemapArea(520, 520, 520, 520), "URANAI!", null)
                    ),
                    null
            ));
            case "imagemap_video" -> this.reply(replyToken, new ImagemapMessage(
                    null,
                    null,
                    createUri("/static/imagemap_video"),
                    "This is an imagemap with video",
                    new ImagemapBaseSize(722, 1040),
                    List.of(
                            new MessageImagemapAction(
                                    new ImagemapArea(260, 600, 450, 86),
                                    "NIXIE CLOCK",
                                    null
                            )
                    ),
                    new ImagemapVideo(
                            createUri("/static/imagemap_video/originalContent.mp4"),
                            createUri("/static/imagemap_video/previewImage.jpg"),
                            new ImagemapArea(40, 46, 952, 536),
                            new ImagemapExternalLink(
                                    URI.create("https://example.com/see_more.html"),
                                    "See More")
                    )
            ));
            case "flex" -> this.reply(replyToken, new ExampleFlexMessageSupplier().get());
            case "quickreply" -> this.reply(replyToken, new MessageWithQuickReplySupplier().get());
            case "no_notify" -> this.reply(replyToken,
                    singletonList(new TextMessage("This message is send without a push notification")),
                    true);
            case "redelivery" -> this.reply(replyToken,
                    singletonList(new TextMessage("webhookEventId=" + event.webhookEventId()
                            + " deliveryContext.isRedelivery=" + event.deliveryContext().isRedelivery())
                    ));
            case "icon" -> this.reply(replyToken,
                    new TextMessage(
                            null,
                            new Sender("Cat", createUri("/static/icon/cat.png")),
                            "Hello, I'm cat! Meow~",
                            null));
            default -> {
                log.info("Returns echo message {}: {}", replyToken, text);
                this.replyText(
                        replyToken,
                        text
                );
            }
        }
    }

    private static URI createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .scheme("https")
                .path(path).build()
                .toUri();
    }

    private void system(String... args) {
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        try {
            Process start = processBuilder.start();
            int i = start.waitFor();
            log.info("result: {} =>  {}", Arrays.toString(args), i);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InterruptedException e) {
            log.info("Interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    private static DownloadedContent saveContent(String ext, BlobContent blobContent) {
        log.info("Got content-type: {}", blobContent);

        DownloadedContent tempFile = createTempFile(ext);
        try (OutputStream outputStream = Files.newOutputStream(tempFile.path)) {
            try (InputStream byteStream = blobContent.byteStream()) {
                byteStream.transferTo(outputStream);
            }
            log.info("Saved {}: {}", ext, tempFile);
            return tempFile;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static DownloadedContent createTempFile(String ext) {
        String fileName = LocalDateTime.now().toString() + '-' + UUID.randomUUID() + '.' + ext;
        Path tempFile = KitchenSinkApplication.downloadedContentDir.resolve(fileName);
        tempFile.toFile().deleteOnExit();
        return new DownloadedContent(
                tempFile,
                createUri("/downloaded/" + tempFile.getFileName()));
    }

    private record DownloadedContent(
            Path path,
            URI uri
    ) {
    }
}
