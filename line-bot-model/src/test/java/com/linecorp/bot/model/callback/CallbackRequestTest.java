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

package com.linecorp.bot.model.callback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.content.AddedAsFriendOperation;
import com.linecorp.bot.model.content.BlockedOperation;
import com.linecorp.bot.model.content.Content;
import com.linecorp.bot.model.content.ContentType;
import com.linecorp.bot.model.content.LocationContent;
import com.linecorp.bot.model.content.OpType;
import com.linecorp.bot.model.content.TextContent;

public class CallbackRequestTest {
    @Test
    public void test() throws IOException {
        InputStream resource = getClass().getClassLoader().getResourceAsStream("callback-request.json");
        String json = IOUtils.toString(resource);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CallbackRequest callbackRequest = objectMapper.readValue(json, CallbackRequest.class);
        assertEquals(1, callbackRequest.getResult().size());
        Event event = callbackRequest.getResult().get(0);
        assertThat(event.getId())
                .isEqualTo("ABCDEF-12345678901");

        Content content = event.getContent();
        assertThat(content).isInstanceOf(TextContent.class);
        TextContent textContent = (TextContent) content;
        assertThat(textContent.getId())
                .isEqualTo("325708");
        assertThat(textContent.getContentType())
                .isEqualTo(ContentType.TEXT);
    }

    @Test
    public void testLocation() throws IOException {
        InputStream resource = getClass().getClassLoader().getResourceAsStream("location-content.json");
        String json = IOUtils.toString(resource);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CallbackRequest callbackRequest = objectMapper.readValue(json, CallbackRequest.class);
        assertEquals(1, callbackRequest.getResult().size());
        Event event = callbackRequest.getResult().get(0);
        assertThat(event.getId())
                .isEqualTo("ID");

        LocationContent content = (LocationContent) event.getContent();
        assertThat(content.getContentType())
                .isEqualTo(ContentType.LOCATION);
        assertThat(content.getLocation().getTitle())
                .isEqualTo("Location");
    }

    @Test
    public void testAddedAsFriend() throws IOException {
        InputStream resource = getClass().getClassLoader().getResourceAsStream("added_as_friend.json");
        String json = IOUtils.toString(resource);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CallbackRequest callbackRequest = objectMapper.readValue(json, CallbackRequest.class);

        Event event = callbackRequest.getResult().get(0);
        AddedAsFriendOperation content = (AddedAsFriendOperation) event.getContent();

        assertThat(content.getOpType()).isEqualTo(OpType.ADDED_AS_FRIEND);
        assertThat(content.getMid()).isEqualTo("u464471c59f5eefe815a19be11f210147");
        assertThat(content.getRevision()).isEqualTo(2469);
    }

    @Test
    public void testBlocked() throws IOException {
        InputStream resource = getClass().getClassLoader().getResourceAsStream("blocked.json");
        String json = IOUtils.toString(resource);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CallbackRequest callbackRequest = objectMapper.readValue(json, CallbackRequest.class);

        Event event = callbackRequest.getResult().get(0);
        BlockedOperation content = (BlockedOperation) event.getContent();

        assertThat(content.getOpType()).isEqualTo(OpType.BLOCKED);
        assertThat(content.getMid()).isEqualTo("u464471c59f5eefe815a19be11f210147");
        assertThat(content.getRevision()).isEqualTo(56801);
    }
}
