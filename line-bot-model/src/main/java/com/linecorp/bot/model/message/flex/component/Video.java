/*
 * Copyright 2022 LINE Corporation
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

import java.net.URI;
import java.text.DecimalFormat;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.action.Action;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

/**
 * The following conditions must be met to use the video component.
 *
 * <ul>
 * <li>The video component is specified directly under the hero block</li>
 * <li>kilo, mega or giga is specified as the value of the size property of the bubble</li>
 * <li>The bubble is not the child element of a carousel</li>
 * </ul>
 */
@JsonTypeName("video")
@JsonInclude(Include.NON_NULL)
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = Video.VideoBuilder.class)
public class Video implements FlexComponent {
    @AllArgsConstructor
    @Getter
    public enum VideoAspectRatio {
        R1TO1("1:1"),
        R20TO13("20:13"),
        R1_91TO1("1.91:1"),
        R4TO3("4:3"),
        R16TO9("16:9"),
        R2TO1("2:1"),
        R3TO1("3:1"),
        R3TO4("3:4"),
        R9TO16("9:16"),
        R1TO2("1:2"),
        R1TO3("1:3"),
        R1_51TO1("1.51:1");

        private final String ratio;
    }

    /**
     * Video file URL. (Max character limit: 2000)
     *
     * <ul>
     * <li>Protocol: HTTPS (TLS 1.2 or later)</li>
     * <li>Video format: mp4</li>
     * <li>Max file size: 200 MB</li>
     * </ul>
     */
    @NonNull
    URI url;

    /**
     * Preview image URL. (Max character limit: 2000)
     *
     * <ul>
     * <li>Protocol: HTTPS (TLS 1.2 or later)</li>
     * <li>Image format: JPEG or PNG</li>
     * <li>Maximum file size: 1 MB</li>
     * </ul>
     */
    @NonNull
    URI previewUrl;

    /**
     * Alternative content.
     *
     * <p>The alternative content will be displayed on the screen of a user device that is using a
     * version of LINE that doesn't support the video component. Specify a {@link Box} or an {@link Image}.
     */
    @NonNull
    FlexComponent altContent;

    /**
     * Aspect ratio of the video.
     *
     * <p>{@code width:height} format. Specify the value of {@code width} and {@code height} in the range from
     * 1 to 100000. However, you can't set {@code height} to a value that is more than three times the value of
     * {@code width}. The default value is {@code 1:1}.
     */
    @Nullable
    String aspectRatio;

    /**
     * URI {@link Action}.
     */
    @Nullable
    Action action;

    @JsonPOJOBuilder(withPrefix = "")
    public static class VideoBuilder {
        private static final Supplier<DecimalFormat> RATIO_FORMAT = () -> new DecimalFormat("0.#####");

        /**
         * Specify aspect ratio by keyword.
         */
        public VideoBuilder aspectRatio(VideoAspectRatio aspectRatio) {
            this.aspectRatio = aspectRatio.getRatio();
            return this;
        }

        /**
         * Specify custom aspect ratio.
         */
        public VideoBuilder aspectRatio(String aspectRatio) {
            this.aspectRatio = aspectRatio;
            return this;
        }

        /**
         * Specify custom aspect ratio. The width and height are rounded up to 5 decimal places.
         */
        public VideoBuilder aspectRatio(double width, double height) {
            final DecimalFormat fmt = RATIO_FORMAT.get();
            aspectRatio = fmt.format(width) + ':' + fmt.format(height);
            return this;
        }
    }
}
