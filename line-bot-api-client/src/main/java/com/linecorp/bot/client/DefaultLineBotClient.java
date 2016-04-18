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

package com.linecorp.bot.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.client.exception.LineBotAPIException;
import com.linecorp.bot.client.exception.LineBotAPIIOException;
import com.linecorp.bot.client.exception.LineBotAPIJsonProcessingException;
import com.linecorp.bot.client.exception.LineBotAPISignatureException;
import com.linecorp.bot.client.exception.LineBotAPITooManyTargetUsersException;
import com.linecorp.bot.client.exception.LineBotServerErrorStatusException;
import com.linecorp.bot.model.content.AbstractContent;
import com.linecorp.bot.model.content.AudioContent;
import com.linecorp.bot.model.content.ImageContent;
import com.linecorp.bot.model.content.LocationContent;
import com.linecorp.bot.model.content.RichMessageContent;
import com.linecorp.bot.model.content.StickerContent;
import com.linecorp.bot.model.content.TextContent;
import com.linecorp.bot.model.content.VideoContent;
import com.linecorp.bot.model.event.EventRequest;
import com.linecorp.bot.model.event.EventResponse;
import com.linecorp.bot.model.event.SendingMessagesRequest;
import com.linecorp.bot.model.event.SendingMultipleMessagesRequest;
import com.linecorp.bot.model.event.SendingMultipleMessagesRequestContent;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.rich.RichMessage;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Default implementation of LINE bot client.
 */
@Slf4j
public class DefaultLineBotClient implements LineBotClient {

    public static final String DEFAULT_API_END_POINT = "https://trialbot-api.line.me";

    public static final long DEFAULT_SENDING_MESSAGE_CHANNEL_ID = 1383378250L;

    public static final String DEFAULT_SENDING_MESSAGE_EVENT_ID = "138311608800106203";

    public static final String DEFAULT_SENDING_MULTIPLE_MESSAGES_EVENT_ID = "140177271400161403";

    private final String channelId;
    private final String channelSecret;
    private final String channelMid;
    private final String apiEndPoint;

    private final Long sendingMessageChannelId;
    private final String sendingMessageEventId;
    private final String sendingMultipleMessagesEventId;

    private final ObjectMapper objectMapper;
    private final HttpClientBuilder httpClientBuilder;

    // For testing.
    public DefaultLineBotClient(
            String channelId,
            String channelSecret,
            String channelMid,

            String apiEndPoint,

            Long sendingMessageChannelId,
            String sendingMessageEventId,
            String sendingMultipleMessagesEventId,

            ObjectMapper objectMapper,
            HttpClientBuilder httpClientBuilder) {
        this.channelId = channelId;
        this.channelSecret = channelSecret;
        this.channelMid = channelMid;

        this.apiEndPoint = apiEndPoint;

        this.sendingMessageChannelId = sendingMessageChannelId;
        this.sendingMessageEventId = sendingMessageEventId;
        this.sendingMultipleMessagesEventId = sendingMultipleMessagesEventId;

        this.objectMapper = objectMapper;
        this.httpClientBuilder = httpClientBuilder;
    }

    /**
     * Create new instance.
     *
     * @param channelId Channel ID
     * @param channelSecret Channel secret
     * @param channelMid Channel MID
     * @param objectMapper Instance of Jackson
     * @param httpClientBuilder Instance of Apache HttpClient
     */
    public DefaultLineBotClient(
            String channelId,
            String channelSecret,
            String channelMid,
            ObjectMapper objectMapper,
            HttpClientBuilder httpClientBuilder
    ) {
        this(channelId, channelSecret, channelMid,
             DEFAULT_API_END_POINT,
             DEFAULT_SENDING_MESSAGE_CHANNEL_ID,
             DEFAULT_SENDING_MESSAGE_EVENT_ID,
             DEFAULT_SENDING_MULTIPLE_MESSAGES_EVENT_ID,
             objectMapper, httpClientBuilder);
    }

    /**
     * Create new instance
     *
     * @param channelId Channel ID
     * @param channelSecret Channel secret
     * @param channelMid Channel MID
     */
    public DefaultLineBotClient(
            String channelId,
            String channelSecret,
            String channelMid
    ) {
        this(channelId, channelSecret, channelMid, buildObjectMapper(), buildHttpClientBuilder());
    }

    private static ObjectMapper buildObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    private static HttpClientBuilder buildHttpClientBuilder() {
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectTimeout(3000)
                .setConnectionRequestTimeout(3000)
                .setSocketTimeout(3000)
                .build();

        return HttpClientBuilder
                .create()
                .disableAutomaticRetries()
                .setDefaultRequestConfig(requestConfig)
                .setUserAgent(buildUserAgent());
    }

    private static String buildUserAgent() {
        return "line-botsdk-java/" + DefaultLineBotClient.class.getPackage().getImplementationVersion();
    }

    @Override
    public EventResponse sendEvent(EventRequest eventRequest)
            throws LineBotAPIException {
        if (eventRequest.getTo().size() > 150) {
            throw new LineBotAPITooManyTargetUsersException(eventRequest);
        }

        String uriString = apiEndPoint + "/v1/events";
        try {
            HttpPost httpPost = new HttpPost(uriString);
            String json = this.objectMapper.writeValueAsString(eventRequest);
            log.info("Sending message to {}: {}", uriString, json);
            httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
            httpPost.setEntity(new ByteArrayEntity(json.getBytes(StandardCharsets.UTF_8)));
            return request(httpPost);
        } catch (JsonProcessingException e) {
            throw new LineBotAPIJsonProcessingException(e);
        }
    }

    private EventResponse request(HttpUriRequest httpRequest) throws LineBotAPIException {
        try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
            this.addHeaders(httpRequest);

            try (CloseableHttpResponse response = httpClient.execute(httpRequest)) {
                // Check status code
                validateStatusCode(response);
                // Read response content body
                return this.objectMapper.readValue(response.getEntity().getContent(), EventResponse.class);
            }
        } catch (IOException e) {
            throw new LineBotAPIIOException(e);
        }
    }

    private void validateStatusCode(CloseableHttpResponse response)
            throws LineBotServerErrorStatusException, IOException {
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new LineBotServerErrorStatusException(
                    response.getStatusLine().getStatusCode(),
                    response.getStatusLine().getReasonPhrase(),
                    EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8)
            );
        }
    }

    private void addHeaders(HttpUriRequest httpRequest) {
        httpRequest.setHeader("X-Line-ChannelID", this.channelId);
        httpRequest.setHeader("X-Line-ChannelSecret", this.channelSecret);
        httpRequest.setHeader("X-Line-Trusted-User-With-ACL", this.channelMid);
    }

    @Override
    public void sendText(@NonNull String mid, @NonNull String message)
            throws LineBotAPIException {
        this.sendText(Collections.singletonList(mid), message);
    }

    @Override
    public void sendText(@NonNull List<String> mids, @NonNull String message)
            throws LineBotAPIException {
        SendingMessagesRequest<TextContent> textEventSendingMessagesRequest = new SendingMessagesRequest<>(
                mids,
                this.sendingMessageChannelId,
                this.sendingMessageEventId,
                new TextContent(SendingMessagesRequest.TO_TYPE_USER, message)
        );

        this.sendEvent(textEventSendingMessagesRequest);
    }

    @Override
    public void sendImage(@NonNull String mid, @NonNull String originalContentUrl,
                          @NonNull String previewImageUrl)
            throws LineBotAPIException {
        this.sendImage(Collections.singletonList(mid), originalContentUrl, previewImageUrl);
    }

    @Override
    public void sendImage(@NonNull List<String> mids, @NonNull String originalContentUrl,
                          @NonNull String previewImageUrl)
            throws LineBotAPIException {
        SendingMessagesRequest<ImageContent> textEventSendingMessagesRequest = new SendingMessagesRequest<>(
                mids,
                this.sendingMessageChannelId,
                this.sendingMessageEventId,
                new ImageContent(SendingMessagesRequest.TO_TYPE_USER, originalContentUrl, previewImageUrl)
        );

        this.sendEvent(textEventSendingMessagesRequest);
    }

    @Override
    public void sendSticker(@NonNull String mid, @NonNull String stkpkgid,
                            @NonNull String stkid)
            throws LineBotAPIException {
        this.sendSticker(Collections.singletonList(mid), stkpkgid, stkid);
    }

    @Override
    public void sendSticker(@NonNull List<String> mids, @NonNull String stkpkgid,
                            @NonNull String stkid)
            throws LineBotAPIException {
        SendingMessagesRequest<StickerContent> request = new SendingMessagesRequest<>(
                mids,
                this.sendingMessageChannelId,
                this.sendingMessageEventId,
                new StickerContent(SendingMessagesRequest.TO_TYPE_USER,
                                   stkpkgid, stkid, null, "[]")
        );

        this.sendEvent(request);
    }

    @Override
    public void sendLocation(@NonNull String mid,
                             @NonNull String text,
                             String title,
                             String address,
                             double latitude,
                             double longitude) throws LineBotAPIException {
        sendLocation(Collections.singletonList(mid), text, title, address, latitude, longitude);
    }

    @Override
    public void sendLocation(@NonNull List<String> mids, @NonNull String text,
                             String title,
                             String address,
                             double latitude,
                             double longitude) throws LineBotAPIException {
        SendingMessagesRequest<LocationContent> request = new SendingMessagesRequest<>(
                mids,
                this.sendingMessageChannelId,
                this.sendingMessageEventId,
                new LocationContent(
                        SendingMessagesRequest.TO_TYPE_USER,
                        text,
                        title, address, latitude, longitude)
        );

        this.sendEvent(request);
    }

    @Override
    public void sendRichMessage(
            @NonNull String mid,
            @NonNull String downloadUrl,
            @NonNull String altText,
            @NonNull RichMessage richMessage
    ) throws LineBotAPIException {
        this.sendRichMessage(Collections.singletonList(mid), downloadUrl, altText, richMessage);
    }

    @Override
    public void sendRichMessage(
            @NonNull List<String> mids,
            @NonNull String downloadUrl,
            @NonNull String altText,
            @NonNull RichMessage richMessage
    ) throws LineBotAPIException {
        try {
            String json = this.objectMapper.writeValueAsString(richMessage);

            SendingMessagesRequest<RichMessageContent> request = new SendingMessagesRequest<>(
                    mids,
                    this.sendingMessageChannelId,
                    this.sendingMessageEventId,
                    new RichMessageContent(
                            SendingMessagesRequest.TO_TYPE_USER,
                            downloadUrl,
                            altText,
                            json
                    )
            );

            this.sendEvent(request);
        } catch (JsonProcessingException e) {
            throw new LineBotAPIJsonProcessingException(e);
        }
    }

    @Override
    public CloseableMessageContent getMessageContent(String messageId) throws LineBotAPIException {
        String uriString = this.apiEndPoint + "/v1/bot/message/" + messageId + "/content";
        return this.getMessageContentByUri(uriString);
    }

    @Override
    public CloseableMessageContent getPreviewMessageContent(String messageId) throws LineBotAPIException {
        String uriString = this.apiEndPoint + "/v1/bot/message/" + messageId + "/content/preview";
        return this.getMessageContentByUri(uriString);
    }

    private CloseableMessageContent getMessageContentByUri(String uri) throws LineBotAPIException {
        HttpGet httpRequest = new HttpGet(uri);
        try {
            CloseableHttpClient httpClient = httpClientBuilder.build();
            this.addHeaders(httpRequest);

            CloseableHttpResponse response = httpClient.execute(httpRequest);
            validateStatusCode(response);
            return new CloseableMessageContentImpl(httpClient, response);
        } catch (IOException e) {
            throw new LineBotAPIIOException(e);
        }
    }

    @Override
    public UserProfileResponse getUserProfile(Collection<String> mids) throws LineBotAPIException {
        String uriString = this.apiEndPoint + "/v1/profiles?mids=" + mids.stream().collect(
                Collectors.joining(","));

        HttpGet httpRequest = new HttpGet(uriString);
        try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
            this.addHeaders(httpRequest);

            try (CloseableHttpResponse response = httpClient.execute(httpRequest)) {
                validateStatusCode(response);

                return this.objectMapper.readValue(response.getEntity().getContent(),
                                                   UserProfileResponse.class);
            }
        } catch (IOException e) {
            throw new LineBotAPIIOException(e);
        }
    }

    @Override
    public MultipleMessageBuilder createMultipleMessageBuilder() {
        return new DefaultMultipleMessageBuilder(this);
    }

    @Override
    public boolean validateSignature(@NonNull String jsonText, @NonNull String headerSignature)
            throws LineBotAPIException {
        String signature = this.createSignature(jsonText);
        return headerSignature.equals(signature);
    }

    @Override
    public String createSignature(@NonNull String jsonText) throws LineBotAPIException {
        try {
            SecretKeySpec key = new SecretKeySpec(this.channelSecret.getBytes(StandardCharsets.UTF_8),
                                                  "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(key);
            byte[] bytes = mac.doFinal(jsonText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            throw new LineBotAPISignatureException(e);
        }
    }

    @Override
    public void sendMultipleMessages(List<String> mids, List<AbstractContent> contents) throws LineBotAPIException {
        SendingMultipleMessagesRequest sendingMultipleMessagesRequest = new SendingMultipleMessagesRequest(
                mids,
                this.sendingMessageChannelId,
                this.sendingMultipleMessagesEventId,
                new SendingMultipleMessagesRequestContent(contents)
        );
        this.sendEvent(sendingMultipleMessagesRequest);
    }

    @Override
    public void sendVideo(String mid, String originalContentUrl, String previewImageUrl) throws LineBotAPIException {
        this.sendVideo(Collections.singletonList(mid), originalContentUrl, previewImageUrl);
    }

    @Override
    public void sendVideo(List<String> mids, String originalContentUrl, String previewImageUrl)
            throws LineBotAPIException {
        SendingMessagesRequest<VideoContent> messagesRequest = new SendingMessagesRequest<>(
                mids,
                this.sendingMessageChannelId,
                this.sendingMessageEventId,
                new VideoContent(SendingMessagesRequest.TO_TYPE_USER, originalContentUrl, previewImageUrl)
        );

        this.sendEvent(messagesRequest);

    }

    @Override
    public void sendAudio(@NonNull String mid, @NonNull String originalContentUrl, @NonNull String audlen)
            throws LineBotAPIException {
        this.sendAudio(Collections.singletonList(mid), originalContentUrl, audlen);
    }

    @Override
    public void sendAudio(@NonNull List<String> mids, @NonNull String originalContentUrl, @NonNull String audlen)
            throws LineBotAPIException {
        SendingMessagesRequest<AudioContent> messagesRequest = new SendingMessagesRequest<>(
                mids,
                this.sendingMessageChannelId,
                this.sendingMessageEventId,
                new AudioContent(SendingMessagesRequest.TO_TYPE_USER, originalContentUrl, audlen)
        );

        this.sendEvent(messagesRequest);
    }
}
