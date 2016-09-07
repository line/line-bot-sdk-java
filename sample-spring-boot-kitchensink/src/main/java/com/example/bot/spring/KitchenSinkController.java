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

package com.example.bot.spring;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.linecorp.bot.client.CloseableMessageContent;
import com.linecorp.bot.client.LineMessagingService;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.LeaveEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.ImageMapMessage;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.imagemap.ImageMapArea;
import com.linecorp.bot.model.message.imagemap.ImageMapBaseSize;
import com.linecorp.bot.model.message.imagemap.MessageImageMapAction;
import com.linecorp.bot.model.message.imagemap.URIImageMapAction;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.profile.Profile;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Response;

@RestController
@Slf4j
public class KitchenSinkController {
    @Autowired
    private LineMessagingService lineMessagingService;

    @RequestMapping("/callback")
    public void callback(@NonNull @LineBotMessages List<Event> events) {
        log.info("Got request: {}", events);

        for (Event event : events) {
            try {
                this.handleEvent(event);
            } catch (IOException e) {
                log.error("LINE server returns '{}'({})",
                          e.getMessage(),
                          event, e);
            }
        }
    }

    private void handleEvent(Event event) throws IOException {
        if (event instanceof MessageEvent) {
            String replyToken = ((MessageEvent) event).getReplyToken();
            MessageContent message = ((MessageEvent) event).getMessage();
            if (message instanceof TextMessageContent) {
                handleTextContent(replyToken, event, (TextMessageContent) message);
            } else if (message instanceof StickerMessageContent) {
                handleSticker(replyToken, (StickerMessageContent) message);
            } else if (message instanceof LocationMessageContent) {
                LocationMessageContent locationMessage = (LocationMessageContent) message;
                reply(replyToken, new LocationMessage(
                        locationMessage.getTitle(),
                        locationMessage.getAddress(),
                        locationMessage.getLatitude(),
                        locationMessage.getLongitude()
                ));
            } else if (message instanceof ImageMessageContent) {
                // TODO
//                handleImage(replyToken, (ImageMessageContent) message);
            }
//        TODO     @JsonSubTypes.Type(ImageMessageContent.class),
        } else if (event instanceof UnfollowEvent) {
            log.info("unfollowed this bot: {}", event);
        } else if (event instanceof FollowEvent) {
            String replyToken = ((FollowEvent) event).getReplyToken();
            this.replyText(replyToken, "Got followed event");
        } else if (event instanceof JoinEvent) {
            String replyToken = ((JoinEvent) event).getReplyToken();
            this.replyText(replyToken, "Joined " + event.getSource());
        } else if (event instanceof LeaveEvent) {
            String replyToken = ((LeaveEvent) event).getReplyToken();
            this.replyText(replyToken, "Leaved " + event.getSource());
        } else if (event instanceof PostbackEvent) {
            String replyToken = ((PostbackEvent) event).getReplyToken();
            this.replyText(replyToken,
                           "Got postback " + ((PostbackEvent) event).getPostbackContent().getData());
        } else {
            // TODO BeaconEvent
//         TODO    @JsonSubTypes.Type(PostbackEvent.class)
            log.info("Received message(Ignored): {}",
                     event);
        }
    }

    private void reply(@NonNull String replyToken, @NonNull Message message) throws IOException {
        reply(replyToken, Collections.singletonList(message));
    }

    private void reply(@NonNull String replyToken, @NonNull List<Message> messages) throws IOException {
        Response<BotApiResponse> apiResponse = lineMessagingService
                .reply(new ReplyMessage(replyToken, messages))
                .execute();
        log.info("Sent messages: {}", apiResponse);
    }

    private void replyText(@NonNull String replyToken, @NonNull String message)
            throws IOException {
        if (replyToken.isEmpty()) {
            throw new IllegalArgumentException("replyToken must not be empty");
        }
        if (message.length() > 1000) {
            message = message.substring(0, 1000 - 2) + "……";
        }
        this.reply(replyToken, new TextMessage(message));
    }

    //    private void handleVideo(VideoContent content) {
//        String mid = content.getFrom();
//        String messageId = content.getId();
//        try {
//            try (CloseableMessageContent messageContent = lineBotClient.getMessageContent(messageId);
//                 CloseableMessageContent previewMessageContent = lineBotClient.getPreviewMessageContent(
//                         messageId)
//            ) {
//                String path = saveContent("video", messageContent);
//                String previewPath = saveContent("video-preview", previewMessageContent);
//
//                // FIXME
////                lineBotClient.sendVideo(mid, path, previewPath);
//            }
//        } catch (IOException e) {
//            log.error("Cannot save item '{}'(mid: '{}')",
//                      e.getMessage(),
//                      messageId, e);
//        } catch (LineBotAPIException e) {
//            log.error("Error in LINE BOT API: '{}'(mid: '{}')",
//                      e.getMessage(),
//                      messageId, e);
//        }
//    }
//
//    private void handleImage(String replyToken, ImageMessageContent content) throws LineBotAPIException {
//        String messageId = content.getId();
//        try {
//            try (CloseableMessageContent messageContent = lineBotClient.getMessageContent(messageId);
//                 CloseableMessageContent previewMessageContent = lineBotClient.getPreviewMessageContent(
//                         messageId)
//            ) {
//                String path = saveContent("image", messageContent);
//                String previewPath = saveContent("image-preview", previewMessageContent);
//
//                // TODO
////                lineBotClient.sendImage(mid, path, previewPath);
//            }
//        } catch (IOException e) {
//            log.error("Cannot save item '{}'(mid: '{}')", e.getMessage(), messageId, e);
//        }
//    }
//
//    private void handleAudio(AudioContent content) {
//        String mid = content.getFrom();
//        AudioContentMetadata contentMetadata = content.getContentMetadata();
//        String messageId = content.getId();
//        try (CloseableMessageContent messageContent = lineBotClient.getMessageContent(messageId)) {
//            String path = saveContent("audio", messageContent);
//            // TODO
////            lineBotClient.sendAudio(mid, path, contentMetadata.getAudlen());
//        } catch (IOException e) {
//            log.error("Cannot save image '{}'(mid: '{}')",
//                      e.getMessage(),
//                      messageId, e);
//        } catch (LineBotAPIException e) {
//            log.error("Error in LINE BOT API: '{}'(mid: '{}')",
//                      e.getMessage(),
//                      messageId, e);
//        }
//    }

    private void handleSticker(String replyToken, StickerMessageContent content) throws IOException {
        reply(replyToken, new StickerMessage(
                content.getPackageId(), content.getStickerId())
        );
    }

    private void handleTextContent(String replyToken, Event event, TextMessageContent content)
            throws IOException {
        String text = content.getText();

        log.info("Got text message from {}: {}", replyToken, text);
        switch (text) {
            case "profile": {
                String userId = event.getSource().getUserId();
                if (userId != null) {
                    Response<UserProfileResponse> response = lineMessagingService
                            .getProfile(Collections.singletonList(userId))
                            .execute();
                    if (response.isSuccessful()) {
                        List<Profile> profiles = response.body().getProfiles();
                        if (!profiles.isEmpty()) {
                            this.reply(
                                    replyToken,
                                    Arrays.asList(new TextMessage(
                                                          "Display name: " + profiles.get(0).getDisplayName()),
                                                  new TextMessage("Status message: " + profiles.get(0)
                                                                                               .getStatusMessage()))
                            );
                        } else {
                            this.replyText(replyToken, "Can't get profile");
                        }
                    } else {
                        this.replyText(replyToken, response.errorBody().string());
                    }
                } else {
                    this.replyText(replyToken, "Bot can't use profile API without user ID");
                }
                break;
            }
            case "bye": {
                Source source = event.getSource();
                if (source instanceof GroupSource) {
                    this.replyText(replyToken, "Leaving group");
                    lineMessagingService.leaveGroup(((GroupSource) source).getGroupId())
                                        .execute();
                } else if (source instanceof RoomSource) {
                    this.replyText(replyToken, "Leaving room");
                    lineMessagingService.leaveRoom(((RoomSource) source).getRoomId())
                                        .execute();
                } else {
                    this.replyText(replyToken, "Bot can't leave from 1:1 chat");
                }
                break;
            }
            case "confirm": {
                ConfirmTemplate confirmTemplate = new ConfirmTemplate(
                        "Do it?",
                        Arrays.asList(
                                new MessageAction("Yes", "Yes!"),
                                new MessageAction("No", "No!")
                        ));
                TemplateMessage templateMessage = new TemplateMessage("Confirm alt text", confirmTemplate);
                this.reply(replyToken, templateMessage);
                break;
            }
            case "buttons": {
                String imageUrl = createUri("/static/buttons/1040.jpg");
                ButtonsTemplate buttonsTemplate = new ButtonsTemplate(
                        imageUrl,
                        "My button sample",
                        "Hello, my button",
                        Arrays.asList(
                                new URIAction("Go to line.me",
                                              "https://line.me"),
                                new PostbackAction("Say hello1",
                                                   "hello こんにちは"),
                                new PostbackAction("言 hello2",
                                                   "hello こんにちは",
                                                   "hello こんにちは"),
                                new MessageAction("Say message",
                                                  "Rice=米")
                        ));
                TemplateMessage templateMessage = new TemplateMessage("Button alt text", buttonsTemplate);
                this.reply(replyToken, templateMessage);
                break;
            }
            case "carousel": {
                String imageUrl = createUri("/static/buttons/1040.jpg");
                CarouselTemplate buttonsTemplate = new CarouselTemplate(
                        Arrays.asList(
                                new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
                                        new URIAction("Go to line.me",
                                                      "https://line.me"),
                                        new PostbackAction("Say hello1",
                                                           "hello こんにちは")
                                )),
                                new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
                                        new PostbackAction("言 hello2",
                                                           "hello こんにちは",
                                                           "hello こんにちは"),
                                        new MessageAction("Say message",
                                                          "Rice=米")
                                ))
                        ));
                TemplateMessage templateMessage = new TemplateMessage("Button alt text", buttonsTemplate);
                this.reply(replyToken, templateMessage);
                break;
            }
            case "imagemap":
                this.reply(replyToken, new ImageMapMessage(
                        createUri("/static/rich"),
                        "This is alt text",
                        new ImageMapBaseSize(1040, 1040),
                        Arrays.asList(
                                new URIImageMapAction(
                                        "https://store.line.me/family/manga/en",
                                        new ImageMapArea(
                                                0, 0, 520, 520
                                        )
                                ),
                                new URIImageMapAction(
                                        "https://store.line.me/family/music/en",
                                        new ImageMapArea(
                                                520, 0, 520, 520
                                        )
                                ),
                                new URIImageMapAction(
                                        "https://store.line.me/family/play/en",
                                        new ImageMapArea(
                                                0, 520, 520, 520
                                        )
                                ),
                                new MessageImageMapAction(
                                        "URANAI!",
                                        new ImageMapArea(
                                                520, 520, 520, 520
                                        )
                                )
                        )
                ));
                break;
            default:
                log.info("Returns echo message {}: {}", replyToken, text);
                this.replyText(
                        replyToken,
                        text
                );
                break;
        }
    }

    private static String createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                                          .path(path).build()
                                          .toUriString();
    }

    private static String saveContent(String type, CloseableMessageContent messageContent) throws IOException {
        log.info("Got filename: {}", messageContent.getFileName());
        String path = LocalDateTime.now().toString() + '-' + UUID.randomUUID().toString();
        Path tempFile = KitchenSinkApplication.downloadedContentDir.resolve(path);
        tempFile.toFile().deleteOnExit();
        OutputStream outputStream = Files.newOutputStream(tempFile);
        IOUtils.copy(messageContent.getContent(),
                     outputStream);
        log.info("Saved {}: {}", type, tempFile);
        return createUri("/downloaded/" + path);
    }
}
