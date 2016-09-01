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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.client.exception.LineBotAPIException;
import com.linecorp.bot.client.exception.LineBotAPIIOException;
import com.linecorp.bot.client.exception.LineBotAPIJsonProcessingException;
import com.linecorp.bot.client.exception.LineBotServerErrorStatusException;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Default implementation of LINE bot client.
 */
@Slf4j
public class DefaultLineBotClient implements LineBotClient {

    public static final String DEFAULT_API_END_POINT = "https://api.line.me";

    private static final String DEFAULT_USER_AGENT =
            "line-botsdk-java/" + DefaultLineBotClient.class.getPackage().getImplementationVersion();

    private static HttpClientBuilder buildDfaultHttpClientBuilder() {
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
                .setUserAgent(DEFAULT_USER_AGENT);
    }

    private final String channelToken;
    private final String apiEndPoint;

    private final ObjectMapper objectMapper;
    private final HttpClientBuilder httpClientBuilder;


    /**
     * Create new instance.
     *
     * @param channelToken Channel token
     * @param apiEndPoint LINE Bot API endpoint URI
     * @param httpClientBuilder Instance of Apache HttpClient
     */
    DefaultLineBotClient(
            String channelToken,
            String apiEndPoint,
            HttpClientBuilder httpClientBuilder) {
        this.channelToken = channelToken;
        this.apiEndPoint = apiEndPoint;
        this.httpClientBuilder = httpClientBuilder != null ? httpClientBuilder : buildDfaultHttpClientBuilder();

        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
    }

    @Override
    public BotApiResponse reply(String replyToken, List<Message> messages)
            throws LineBotAPIException {
        String uriString = apiEndPoint + "/v2/bot/message/reply";
        ReplyMessage replyMessage = new ReplyMessage(replyToken, messages);
        try {
            HttpPost httpPost = new HttpPost(uriString);
            String json = this.objectMapper.writeValueAsString(replyMessage);
            log.info("Sending message to {}: {}", uriString, json);
            httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
            httpPost.setEntity(new ByteArrayEntity(json.getBytes(StandardCharsets.UTF_8)));
            return request(httpPost, BotApiResponse.class);
        } catch (JsonProcessingException e) {
            throw new LineBotAPIJsonProcessingException(e);
        }
    }

    @Override
    public BotApiResponse push(List<String> to, List<Message> messages)
            throws LineBotAPIException {
        String uriString = apiEndPoint + "/v2/bot/message/push";
        HttpPost httpPost = new HttpPost(uriString);
        PushMessage pushMessage = new PushMessage(to, messages);
        try {
            String json = this.objectMapper.writeValueAsString(pushMessage);
            log.info("Sending push message to {}: {}", uriString, json);
            httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
            httpPost.setEntity(new ByteArrayEntity(json.getBytes(StandardCharsets.UTF_8)));
            return request(httpPost, BotApiResponse.class);
        } catch (JsonProcessingException e) {
            throw new LineBotAPIJsonProcessingException(e);
        }
    }

    private <T> T request(HttpUriRequest httpRequest, Class<T> klass) throws LineBotAPIException {
        try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
            this.addHeaders(httpRequest);

            try (CloseableHttpResponse response = httpClient.execute(httpRequest)) {
                // Check status code
                validateStatusCode(response);
                // Read response content body
                return this.objectMapper.readValue(response.getEntity().getContent(), klass);
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
        httpRequest.setHeader("X-LINE-ChannelToken", this.channelToken); // will be deprecate
        httpRequest.setHeader("Authorization", "Bearer " + this.channelToken);
    }

    @Override
    public CloseableMessageContent getMessageContent(String messageId) throws LineBotAPIException {
        String uriString = this.apiEndPoint + "/v2/bot/message/" + messageId + "/content";
        return this.getMessageContentByUri(uriString);
    }

    @Override
    public CloseableMessageContent getPreviewMessageContent(String messageId) throws LineBotAPIException {
        String uriString = this.apiEndPoint + "/v2/bot/message/" + messageId + "/previewcontent";
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
    public UserProfileResponse getUserProfile(@NonNull Collection<String> userIds) throws LineBotAPIException {
        String uriString = this.apiEndPoint + "/v2/bot/profile?userId=" + userIds.stream().collect(
                Collectors.joining(","));

        return this.request(new HttpGet(uriString), UserProfileResponse.class);
    }

    @Override
    public BotApiResponse leaveGroup(@NonNull String groupId) throws LineBotAPIException {
        String uriString = this.apiEndPoint + "/v2/bot/group/" + groupId + "/leave";
        return this.request(new HttpPost(uriString), BotApiResponse.class);
    }

    @Override
    public BotApiResponse leaveRoom(@NonNull String roomId) throws LineBotAPIException {
        String uriString = this.apiEndPoint + "/v2/bot/room/" + roomId + "/leave";
        return this.request(new HttpPost(uriString), BotApiResponse.class);
    }
}
