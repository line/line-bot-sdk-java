/*
 * Copyright 2019 LINE Corporation
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

package com.linecorp.bot.model.message.flex.component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.net.URI;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.linecorp.bot.model.message.flex.component.Icon.IconBuilder;
import com.linecorp.bot.model.message.flex.component.Image.ImageBuilder;

import lombok.AllArgsConstructor;
import lombok.Value;

public class AspectRatioFormatTest {

    @Value
    @AllArgsConstructor
    public static class Fixture {
        double width;
        double height;
        String result;
    }

    @ParameterizedTest
    @MethodSource("testSource")
    public void test(double width, double height, String result) {
        final Image image =
                new ImageBuilder()
                        .aspectRatio(width, height)
                        .build();
        assertThat(image.getAspectRatio()).isEqualTo(result);
    }

    @ParameterizedTest
    @MethodSource("testSource")
    public void video_aspectRatio(double width, double height, String result) throws Exception {
        final Image altImage =
                new ImageBuilder()
                        .aspectRatio(width, height)
                        .build();

        final Video video =
                new Video.VideoBuilder()
                        .aspectRatio(width, height)
                        .url(new URI("https://example.com/video.mp4"))
                        .previewUrl(new URI("https://example.com/picture.png"))
                        .altContent(altImage)
                        .build();
        assertThat(video.getAspectRatio()).isEqualTo(result);
    }

    @ParameterizedTest
    @MethodSource("testSource")
    public void icon(double width, double height, String result) {
        final Icon icon =
                new IconBuilder()
                        .aspectRatio(width, height)
                        .build();
        assertThat(icon.getAspectRatio()).isEqualTo(result);
    }

    public static Stream<Arguments> testSource() {
        return Stream.of(
                arguments(1, 1, "1:1"),
                arguments(1.01, 1.01, "1.01:1.01"),
                arguments(100000, 100000, "100000:100000"),
                arguments(Math.PI, Math.PI, "3.14159:3.14159")
        );
    }
}
