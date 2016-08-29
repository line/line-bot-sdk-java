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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.linecorp.bot.client.CloseableMessageContent;
import com.linecorp.bot.client.LineBotClient;
import com.linecorp.bot.client.exception.LineBotAPIException;
import com.linecorp.bot.client.rich.SimpleRichMessageBuilder;
import com.linecorp.bot.model.deprecated.content.AddedAsFriendOperation;
import com.linecorp.bot.model.deprecated.content.AudioContent;
import com.linecorp.bot.model.deprecated.content.BlockedOperation;
import com.linecorp.bot.model.deprecated.content.ImageContent;
import com.linecorp.bot.model.deprecated.content.VideoContent;
import com.linecorp.bot.model.deprecated.content.metadata.AudioContentMetadata;
import com.linecorp.bot.model.deprecated.profile.UserProfileResponse;
import com.linecorp.bot.model.deprecated.rich.RichMessage;
import com.linecorp.bot.model.v2.event.Event;
import com.linecorp.bot.model.v2.event.MessageEvent;
import com.linecorp.bot.model.v2.event.UnfollowEvent;
import com.linecorp.bot.model.v2.event.message.ContactMessageContent;
import com.linecorp.bot.model.v2.event.message.ImageMessageContent;
import com.linecorp.bot.model.v2.event.message.LocationMessageContent;
import com.linecorp.bot.model.v2.event.message.MessageContent;
import com.linecorp.bot.model.v2.event.message.StickerMessageContent;
import com.linecorp.bot.model.v2.event.message.TextMessageContent;
import com.linecorp.bot.model.v2.event.source.GroupSource;
import com.linecorp.bot.model.v2.event.source.Source;
import com.linecorp.bot.model.v2.message.StickerMessage;
import com.linecorp.bot.model.v2.message.TextMessage;
import com.linecorp.bot.model.v2.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class KitchenSinkController {
    @Autowired
    private LineBotClient lineBotClient;

    @RequestMapping("/callback")
    public void callback(@NonNull @LineBotMessages List<Event> events) {
        log.info("Got request: {}", events);

        for (Event event : events) {
            try {
                this.handleEvent(event);
            } catch (LineBotAPIException e) {
                log.error("LINE server returns '{}'({})",
                          e.getMessage(),
                          event, e);
            }
        }
    }

    private void handleEvent(Event event) throws LineBotAPIException {
        Source source = event.getSource();
        String mid = source instanceof GroupSource
                     ? ((GroupSource) source).getGroupId()
                     : source.getUserId();
        if (event instanceof MessageEvent) {
            MessageContent message = ((MessageEvent) event).getMessage();
            if (message instanceof TextMessageContent) {
                handleTextContent(mid, (TextMessageContent) message);
            } else if (message instanceof StickerMessageContent) {
                handleSticker(mid, (StickerMessageContent) message);
            } else if (message instanceof ContactMessageContent) {
                // TODO... Test this at next week.
                handleContact(mid, (ContactMessageContent) message);
            } else if (message instanceof LocationMessageContent) {
                handleLocation(mid, (LocationMessageContent) message);
            } else if (message instanceof ImageMessageContent) {
//                handleImage(mid, (ImageMessageContent) message);
            }
//        TODO     @JsonSubTypes.Type(ImageMessageContent.class),
        } else if (event instanceof UnfollowEvent) {
            handleUnfollow((UnfollowEvent) event);
        } else {
//         TODO    @JsonSubTypes.Type(PostbackEvent.class)
//        } else if (event instanceof StickerContent) {
//            handleSticker((StickerContent) event);
//        } else if (event instanceof LocationContent) {
//            handleLocation((LocationContent) event);
//        } else if (event instanceof ContactContent) {
//            handleContact((ContactContent) event);
//        } else if (event instanceof AudioContent) {
//            handleAudio((AudioContent) event);
//        } else if (event instanceof ImageContent) {
//            handleImage((ImageContent) event);
//        } else if (event instanceof VideoContent) {
//            handleVideo((VideoContent) event);
//        } else if (event instanceof BlockedOperation) {
//            handleBlocked((BlockedOperation) event);
//        } else if (event instanceof AddedAsFriendOperation) {
//            handleAddedAsFriend((AddedAsFriendOperation) event);
//        } else{
            log.info("Received message(Ignored): {}",
                     event);
        }
    }

    private void handleUnfollow(UnfollowEvent event) {
        log.info("unfollowed this bot: {}", event);
    }

    private void handleLocation(String mid, LocationMessageContent content) {

    }

    private void handleAddedAsFriend(AddedAsFriendOperation content) {
        String mid = content.getMid();
        log.info("User added this account as a friend: {}", mid);
        try {
            this.sendText(mid, "Hi! I'm a bot!");
        } catch (LineBotAPIException e) {
            log.error("LINE server returns '{}'(mid: '{}')",
                      e.getMessage(),
                      mid, e);
        }
    }

    private void sendText(@NonNull String mid, @NonNull String message) throws LineBotAPIException {
        if (mid.isEmpty()) {
            throw new IllegalArgumentException("MID must not be empty");
        }
        if (message.length() > 100) {
            message = message.substring(0, 98) + "……";
        }
        BotApiResponse apiResponse = lineBotClient.push(mid, new TextMessage(message));
        log.info("Sent messages: {}", apiResponse);
    }

    private void handleBlocked(BlockedOperation content) {
        String mid = content.getMid();
        log.info("User blocked this account: {}", mid);
    }

    private void handleVideo(VideoContent content) {
        String mid = content.getFrom();
        String messageId = content.getId();
        try {
            try (CloseableMessageContent messageContent = lineBotClient.getMessageContent(messageId);
                 CloseableMessageContent previewMessageContent = lineBotClient.getPreviewMessageContent(
                         messageId)
            ) {
                String path = saveContent("video", messageContent);
                String previewPath = saveContent("video-preview", previewMessageContent);

                // FIXME
//                lineBotClient.sendVideo(mid, path, previewPath);
            }
        } catch (IOException e) {
            log.error("Cannot save item '{}'(mid: '{}')",
                      e.getMessage(),
                      messageId, e);
        } catch (LineBotAPIException e) {
            log.error("Error in LINE BOT API: '{}'(mid: '{}')",
                      e.getMessage(),
                      messageId, e);
        }
    }

    private void handleImage(ImageContent content) {
        String mid = content.getFrom();
        String messageId = content.getId();
        try {
            try (CloseableMessageContent messageContent = lineBotClient.getMessageContent(messageId);
                 CloseableMessageContent previewMessageContent = lineBotClient.getPreviewMessageContent(
                         messageId)
            ) {
                String path = saveContent("image", messageContent);
                String previewPath = saveContent("image-preview", previewMessageContent);

                // TODO
//                lineBotClient.sendImage(mid, path, previewPath);
            }
        } catch (IOException e) {
            log.error("Cannot save item '{}'(mid: '{}')",
                      e.getMessage(),
                      messageId, e);
        } catch (LineBotAPIException e) {
            log.error("Error in LINE BOT API: '{}'(mid: '{}')",
                      e.getMessage(),
                      messageId, e);
        }
    }

    private void handleAudio(AudioContent content) {
        String mid = content.getFrom();
        AudioContentMetadata contentMetadata = content.getContentMetadata();
        String messageId = content.getId();
        try (CloseableMessageContent messageContent = lineBotClient.getMessageContent(messageId)) {
            String path = saveContent("audio", messageContent);
            // TODO
//            lineBotClient.sendAudio(mid, path, contentMetadata.getAudlen());
        } catch (IOException e) {
            log.error("Cannot save image '{}'(mid: '{}')",
                      e.getMessage(),
                      messageId, e);
        } catch (LineBotAPIException e) {
            log.error("Error in LINE BOT API: '{}'(mid: '{}')",
                      e.getMessage(),
                      messageId, e);
        }
    }

    private void handleContact(@NonNull String mid, @NonNull ContactMessageContent content) {
        try {
            this.sendText(
                    mid, "Received contact info for : " + content.getDisplayName()
            );
        } catch (LineBotAPIException e) {
            log.error("LINE server returns '{}'(mid: '{}')",
                      e.getMessage(),
                      mid, e);
        }
    }

    private void handleSticker(String mid, StickerMessageContent content) {
        // Bot can send some built-in stickers.
        try {
            BotApiResponse apiResponse = lineBotClient.push(mid, new StickerMessage(
                    content.getPackageId(), content.getStickerId())
            );
            log.info("Sent messages: {}", apiResponse);
        } catch (LineBotAPIException e) {
            log.error("LINE server returns '{}'(mid: '{}')",
                      e.getMessage(),
                      mid, e);
        }
    }

    private void handleTextContent(String mid, TextMessageContent content) {
        String text = content.getText();

        try {
            log.info("Got text message from {}: {}", mid, text);
            switch (text) {
            case "profile":
                UserProfileResponse userProfile = lineBotClient.getUserProfile(Collections.singletonList(mid));
//                lineBotClient.sendText(mid, userProfile.toString());
                break;
            case "rich":
                final RichMessage richMessage =
                        SimpleRichMessageBuilder.create(1040, 1040)
                        .addWebAction(0, 0, 520, 520, "manga", "https://store.line.me/family/manga/en")
                        .addWebAction(520, 0, 520, 520, "music", "https://store.line.me/family/music/en")
                        .addWebAction(0, 520, 520, 520, "play", "https://store.line.me/family/play/en")
                        .addWebAction(520, 520, 520, 520, "fortune", "https://store.line.me/family/uranai/en")
                        .build();

//                lineBotClient.sendRichMessage(
//                        mid,
//                        createUri("/static/rich"),
//                        "This is alt text.",
//                        richMessage
//                );
                break;
                // TODO html messages
            default:
                log.info("Returns echo message {}: {}", mid, text);
                this.sendText(
                        mid,
                        text
                );
                break;
            }
        } catch (LineBotAPIException e) {
            log.error("LINE server returns '{}'(mid: '{}', text: '{}')",
                      e.getMessage(),
                      mid,
                      text, e);
        }
    }

    private String createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                                          .path(path).build()
                                          .toUriString();
    }

    private String saveContent(String type, CloseableMessageContent messageContent) throws IOException {
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
