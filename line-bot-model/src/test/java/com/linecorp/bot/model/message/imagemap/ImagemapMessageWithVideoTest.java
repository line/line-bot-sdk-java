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

package com.linecorp.bot.model.message.imagemap;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import com.linecorp.bot.model.message.ImagemapMessage;
import com.linecorp.bot.model.testutil.TestUtil;

public class ImagemapMessageWithVideoTest {
    private static final ObjectMapper OBJECT_MAPPER = TestUtil.objectMapperWithProductionConfiguration(false);

    @Test
    public void imagemapWithVideo_withoutExternalLink() throws IOException {

        ImagemapMessage message = new ImagemapMessage(
                "https://example.com/path/to/baseUrl",
                "altText",
                new ImagemapBaseSize(578, 1040),
                singletonList(new MessageImagemapAction("text", new ImagemapArea(10, 20, 30, 40))),
                ImagemapVideo.builder()
                        .originalContentUrl(URI.create("https://example.com/path/to/originalContentUrl"))
                        .previewImageUrl(URI.create("https://example.com/path/to/previewImageUrl"))
                        .area(new ImagemapArea(0, 0, 1040, 578))
                        .build()
        );

        String json = OBJECT_MAPPER.writeValueAsString(message);
        DocumentContext documentContext = JsonPath.parse(json);

        assertThat(documentContext.read("$.type", String.class)).isEqualTo("imagemap");
        assertThat(documentContext.read("$.video.externalLink", String.class)).isNull();

        ImagemapMessage fromJson = OBJECT_MAPPER.readValue(json, ImagemapMessage.class);
        assertThat(fromJson).isEqualTo(message);
    }

    @Test
    public void imagemapWithVideo() throws IOException {

        String json = "{\n"
                + "  \"type\": \"imagemap\",\n"
                + "  \"baseUrl\": \"https://example.com/bot/images/rm001\",\n"
                + "  \"altText\": \"This is an imagemap\",\n"
                + "  \"baseSize\": {\n"
                + "      \"width\": 1040,\n"
                + "      \"height\": 1040\n"
                + "  },\n"
                + "  \"video\": {\n"
                + "      \"originalContentUrl\": \"https://example.com/video.mp4\",\n"
                + "      \"previewImageUrl\": \"https://example.com/video_preview.jpg\",\n"
                + "      \"area\": {\n"
                + "          \"x\": 0,\n"
                + "          \"y\": 0,\n"
                + "          \"width\": 1040,\n"
                + "          \"height\": 585\n"
                + "      },\n"
                + "      \"externalLink\": {\n"
                + "          \"linkUri\": \"https://example.com/see_more.html\",\n"
                + "          \"label\": \"See More\"\n"
                + "      }\n"
                + "  },\n"
                + "  \"actions\": [\n"
                + "      {\n"
                + "          \"type\": \"uri\",\n"
                + "          \"linkUri\": \"https://example.com/\",\n"
                + "          \"area\": {\n"
                + "              \"x\": 0,\n"
                + "              \"y\": 586,\n"
                + "              \"width\": 520,\n"
                + "              \"height\": 454\n"
                + "          }\n"
                + "      },\n"
                + "      {\n"
                + "          \"type\": \"message\",\n"
                + "          \"text\": \"Hello\",\n"
                + "          \"area\": {\n"
                + "              \"x\": 520,\n"
                + "              \"y\": 586,\n"
                + "              \"width\": 520,\n"
                + "              \"height\": 454\n"
                + "          }\n"
                + "      }\n"
                + "  ]\n"
                + "}";

        ImagemapMessage message = ImagemapMessage
                .builder()
                .baseUrl("https://example.com/bot/images/rm001")
                .altText("This is an imagemap")
                .baseSize(new ImagemapBaseSize(1040, 1040))
                .video(
                        ImagemapVideo.builder()
                                .originalContentUrl(URI.create("https://example.com/video.mp4"))
                                .previewImageUrl(URI.create("https://example.com/video_preview.jpg"))
                                .area(new ImagemapArea(0, 0, 1040, 585))
                                .externalLink(
                                        new ImagemapExternalLink(
                                                URI.create("https://example.com/see_more.html"), "See More")
                                )
                                .build()
                )
                .actions(Arrays.asList(
                        new URIImagemapAction("https://example.com/", new ImagemapArea(0, 586, 520, 454)),
                        new MessageImagemapAction("Hello", new ImagemapArea(520, 586, 520, 454))
                ))
                .build();

        ImagemapMessage parsed = OBJECT_MAPPER.readValue(json, ImagemapMessage.class);
        assertThat(parsed).isEqualTo(message);

        String recreateJson = OBJECT_MAPPER.writeValueAsString(parsed);
        DocumentContext documentContext = JsonPath.parse(recreateJson);

        assertThat(documentContext.read("$.type", String.class)).isEqualTo("imagemap");
        assertThat(documentContext.read("$.baseUrl", String.class)).isEqualTo("https://example.com/bot/images/rm001");
        assertThat(documentContext.read("$.altText", String.class)).isEqualTo("This is an imagemap");
        assertThat(documentContext.read("$.baseSize.width", Integer.class)).isEqualTo(1040);
        assertThat(documentContext.read("$.baseSize.height", Integer.class)).isEqualTo(1040);

        assertThat(documentContext.read("$.video.originalContentUrl", String.class)).isEqualTo("https://example.com/video.mp4");
        assertThat(documentContext.read("$.video.previewImageUrl", String.class)).isEqualTo("https://example.com/video_preview.jpg");
        assertThat(documentContext.read("$.video.area.x", Integer.class)).isEqualTo(0);
        assertThat(documentContext.read("$.video.area.y", Integer.class)).isEqualTo(0);
        assertThat(documentContext.read("$.video.area.width", Integer.class)).isEqualTo(1040);
        assertThat(documentContext.read("$.video.area.height", Integer.class)).isEqualTo(585);

        assertThat(documentContext.read("$.video.externalLink.linkUri", String.class)).isEqualTo("https://example.com/see_more.html");
        assertThat(documentContext.read("$.video.externalLink.label", String.class)).isEqualTo("See More");

        assertThat(documentContext.read("$.actions.length()", Integer.class)).isEqualTo(2);

        assertThat(documentContext.read("$.actions[0].type", String.class)).isEqualTo("uri");
        assertThat(documentContext.read("$.actions[0].linkUri", String.class)).isEqualTo("https://example.com/");
        assertThat(documentContext.read("$.actions[0].area.x", Integer.class)).isEqualTo(0);
        assertThat(documentContext.read("$.actions[0].area.y", Integer.class)).isEqualTo(586);
        assertThat(documentContext.read("$.actions[0].area.width", Integer.class)).isEqualTo(520);
        assertThat(documentContext.read("$.actions[0].area.height", Integer.class)).isEqualTo(454);

        assertThat(documentContext.read("$.actions[1].type", String.class)).isEqualTo("message");
        assertThat(documentContext.read("$.actions[1].text", String.class)).isEqualTo("Hello");
        assertThat(documentContext.read("$.actions[1].area.x", Integer.class)).isEqualTo(520);
        assertThat(documentContext.read("$.actions[1].area.y", Integer.class)).isEqualTo(586);
        assertThat(documentContext.read("$.actions[1].area.width", Integer.class)).isEqualTo(520);
        assertThat(documentContext.read("$.actions[1].area.height", Integer.class)).isEqualTo(454);
    }
}
