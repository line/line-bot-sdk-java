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

import java.util.Collection;

import com.linecorp.bot.client.exception.LineBotAPIException;
import com.linecorp.bot.client.exception.LineBotAPIJsonProcessingException;
import com.linecorp.bot.model.deprecated.content.AbstractContent;
import com.linecorp.bot.model.deprecated.event.EventRequest;
import com.linecorp.bot.model.deprecated.event.EventResponse;
import com.linecorp.bot.model.deprecated.profile.UserProfileResponse;
import com.linecorp.bot.model.deprecated.rich.RichMessage;
import com.linecorp.bot.model.v2.event.CallbackRequest;

import lombok.NonNull;

/**
 * The line bot client interface.
 */
public interface LineBotClient {
    /**
     * Send event request to the server.
     *
     * @param eventRequest event request object
     *
     * @return API response bean.
     */
    EventResponse sendEvent(EventRequest eventRequest)
            throws LineBotAPIException;

    /**
     * Send text to the server
     *
     * @param mid Target user's MID.
     * @param message String you want to send. Messages can contain up to 1024 characters.
     */
    void sendText(String mid, String message) throws LineBotAPIException;

    /**
     * Send text to the server
     *  @param mids Array of target user. Max count: 150.
     * @param message String you want to send. Messages can contain up to 1024 characters.
     */
    void sendText(Collection<String> mids, String message) throws LineBotAPIException;

    /**
     * To send an image, place 2 image files (main image and thumbnail image used for preview) on your BOT API server,
     * then relay the image to the LINE platform.
     *
     * @param mid Target user's MID
     * @param originalContentUrl URL of image. Only JPEG format supported. Image size cannot be larger than 1024×1024.
     * @param previewImageUrl URL of thumbnail image. For preview. Only JPEG format supported. Image size cannot be
     * larger than 240×240.
     */
    void sendImage(@NonNull String mid, @NonNull String originalContentUrl,
                   @NonNull String previewImageUrl)
            throws LineBotAPIException;

    /**
     * To send an image, place 2 image files (main image and thumbnail image used for preview) on your BOT API server,
     * then relay the image to the LINE platform.
     *  @param mids Array of target user. Max count: 150.
     * @param originalContentUrl URL of image. Only JPEG format supported. Image size cannot be larger than 1024×1024.
     * @param previewImageUrl URL of thumbnail image. For preview. Only JPEG format supported. Image size cannot be
     */
    void sendImage(@NonNull Collection<String> mids, @NonNull String originalContentUrl,
                   @NonNull String previewImageUrl)
            throws LineBotAPIException;

    /**
     * To send a video, place a video file and a thumbnail image to be used as preview on your BOT API server, then
     * relay the video to the LINE platform.
     *
     * @param mid Target user's MID
     * @param originalContentUrl URL of the movie. The "mp4" format is recommended.
     * @param previewImageUrl URL of thumbnail image used as a preview.
     */
    void sendVideo(String mid, String originalContentUrl, String previewImageUrl) throws LineBotAPIException;

    /**
     * To send a video, place a video file and a thumbnail image to be used as preview on your BOT API server, then
     * relay the video to the LINE platform.
     *  @param mids Array of target user. Max count: 150.
     * @param originalContentUrl URL of the movie. The "mp4" format is recommended.
     * @param previewImageUrl URL of thumbnail image used as a preview.
     */
    void sendVideo(Collection<String> mids, String originalContentUrl, String previewImageUrl)
            throws LineBotAPIException;

    /**
     * To send a voice message, place the audio file on your BOT API server, then relay the audio file to the LINE
     * platform.
     *
     * @param mid Target user's MID.
     * @param originalContentUrl URL of audio file. The "m4a" format is recommended.
     * @param audlen Length of voice message. The unit is given in milliseconds.
     */
    void sendAudio(String mid, String originalContentUrl, String audlen) throws LineBotAPIException;

    /**
     * To send a voice message, place the audio file on your BOT API server, then relay the audio file to the LINE
     * platform.
     *  @param mids Array of target user. Max count: 150.
     * @param originalContentUrl URL of audio file. The "m4a" format is recommended.
     * @param audlen Length of voice message. The unit is given in milliseconds.
     */
    void sendAudio(Collection<String> mids, String originalContentUrl, String audlen)
            throws LineBotAPIException;

    /**
     * To send location information.
     *
     * @param mid Target user's MID.
     * @param text String used to explain the location information (example: name of restaurant, address).
     * @param title Assigned the same string as the text property.
     * @param latitude Latitude.
     * @param longitude Longitude.
     */
    void sendLocation(@NonNull String mid,
                      @NonNull String text,
                      String title,
                      String address,
                      double latitude,
                      double longitude) throws LineBotAPIException;

    /**
     * To send location information.
     *  @param mids Array of target user. Max count: 150.
     * @param text String used to explain the location information (example: name of restaurant, address).
     * @param title Assigned the same string as the text property.
     * @param latitude Latitude.
     * @param longitude Longitude.
     */
    void sendLocation(@NonNull Collection<String> mids, @NonNull String text,
                      String title,
                      String address,
                      double latitude,
                      double longitude) throws LineBotAPIException;

    /**
     * To send a sticker, the required values are as follows. You can use the stickers shown in
     * <a href="https://developers.line.me/wp-content/uploads/2016/04/sticker_list.xlsx">the sticker list</a>.
     *
     * @param mid Target user's MID.
     * @param stkpkgid Package ID of the sticker.
     * @param stkid Package ID of the sticker.
     */
    void sendSticker(@NonNull String mid, @NonNull String stkpkgid,
                     @NonNull String stkid)
            throws LineBotAPIException;

    /**
     * To send a sticker, the required values are as follows. You can use the stickers shown in
     * <a href="https://developers.line.me/wp-content/uploads/2016/04/sticker_list.xlsx">the sticker list</a>.
     *  @param mids Array of target user. Max count: 150.
     * @param stkpkgid Package ID of the sticker.
     * @param stkid Package ID of the sticker.
     */
    void sendSticker(@NonNull Collection<String> mids,
                     @NonNull String stkpkgid,
                     @NonNull String stkid)
            throws LineBotAPIException;

    /**
     * Your BOT API server can send rich messages, messages that contain one or more pictures and/or text strings in
     * one message. Rich messages are interactive. By using rich messages, users can tap on the message to go to
     * different URLs. You can have multiple URLs on one rich message.
     * <p>
     * You can send clickable images with rich messages.
     *
     * @param mid Target user's MID.
     * @param downloadUrl URL of image which is on your server.
     * @param altText Alternative string displayed on low-level devices.
     * @param richMessage Rich message.
     */
    void sendRichMessage(
            @NonNull String mid,
            @NonNull String downloadUrl,
            @NonNull String altText,
            @NonNull RichMessage richMessage
    ) throws LineBotAPIException;

    /**
     * Your BOT API server can send rich messages, messages that contain one or more pictures and/or text strings in
     * one message. Rich messages are interactive. By using rich messages, users can tap on the message to go to
     * different URLs. You can have multiple URLs on one rich message.
     * <p>
     * You can send clickable images with rich messages.
     *  @param mids Array of target user. Max count: 150.
     * @param downloadUrl URL of image which is on your server.
     * @param altText Alternative string displayed on low-level devices.
     * @param richMessage Rich message.
     */
    void sendRichMessage(
            @NonNull Collection<String> mids,
            @NonNull String downloadUrl,
            @NonNull String altText,
            @NonNull RichMessage richMessage
    ) throws LineBotAPIException;

    /**
     * You can use the following API to send multiple messages to users in a single request. You can also determine the order of the messages that you want to send.
     *
     * @return Multiple message builder object.
     */
    MultipleMessageBuilder createMultipleMessageBuilder();

    /**
     * Use the following API to retrieve the content of a user's message which is an image or video file.
     * <p>
     * Note: Contents are only stored on our servers for a certain period of time, after which they can no longer be
     * received. We are unable to provide details about how long contents are stored.
     *
     * @param messageId The message ID
     */
    CloseableMessageContent getMessageContent(String messageId) throws LineBotAPIException;

    /**
     * When a user sends a message to the BOT API server via the LINE platform using "Receiving messages", you will
     * receive a thumbnail preview of the message if the message is an image or video file.
     * <p>
     * Note: Content is only stored on our servers for a certain period of time, after which they can no longer be
     * received. We are unable to provide details about how long content is stored.
     *
     * @param messageId The message ID
     */
    CloseableMessageContent getPreviewMessageContent(String messageId) throws LineBotAPIException;

    /**
     * The profile information of any specified user can be obtained.
     *
     * @param mids Required. Lists the MIDs of the users whose information is to be retrieved, separated by commas.
     */
    UserProfileResponse getUserProfile(Collection<String> mids) throws LineBotAPIException;

    boolean validateSignature(@NonNull String jsonText, @NonNull String headerSignature) throws LineBotAPIException;

    boolean validateSignature(@NonNull byte[] jsonText, @NonNull String headerSignature) throws LineBotAPIException;

    byte[] createSignature(@NonNull byte[] jsonText) throws LineBotAPIException;

    void sendMultipleMessages(Collection<String> mids, Collection<AbstractContent> contents) throws LineBotAPIException;

    /**
     * Reads {@link CallbackRequest} from the given json.
     *
     * @param jsonText The byte array to be parsed.
     * @return parsed {@link CallbackRequest} object.
     */
    CallbackRequest readCallbackRequest(@NonNull byte[] jsonText) throws LineBotAPIJsonProcessingException;

    /**
     * Reads {@link CallbackRequest} from the given json.
     *
     * @param jsonText The text to be parsed.
     * @return parsed {@link CallbackRequest} object.
     */
    CallbackRequest readCallbackRequest(@NonNull String jsonText) throws LineBotAPIJsonProcessingException;

}
