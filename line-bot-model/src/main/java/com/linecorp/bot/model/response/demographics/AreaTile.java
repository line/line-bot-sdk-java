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

package com.linecorp.bot.model.response.demographics;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.response.demographics.AreaTile.AreaTileBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = AreaTileBuilder.class)
class AreaTile {
    /**
     * Area. The value returned depends on the country.
     *
     * <p>Possible values</p>
     * <dl>
     *     <dt>Japan</dt>
     *     <dd>
     *         <p>Prefecture name. without "県", "都" or "府" prefix.</p>
     *         <p>Possible values: "北海道", "青森", "東京", "大阪", "京都", etc or {@code "unknown"}.</p>
     *     </dd>
     *
     *     <dt>Taiwan</dt>
     *     <dd>
     *         <p>City name in Simplified Chinese characters.<p>
     *         <p>Possible values: "台北市", "新北市", "桃園市", "台中市", etc or {@code "unknown"}.</p>
     *     </dd>
     *
     *     <dt>Thailand</dt>
     *     <dd>
     *         <p>"Bangkok", "Pattaya" or region name based on a
     *         <a href="https://en.wikipedia.org/wiki/Regions_of_Thailand">six-region system</a>.</p>
     *         <p>Possible values: "Bangkok", "Pattaya", "Northern", "Central", "Southern", "Eastern",
     *         "NorthEastern", "Western" or {@code "unknown"}.</p>
     *     </dd>
     * </dl>:
     */
    String area;

    /**
     * Percentage. Possible values: [0.0,100.0] e.g. {@code 0}, {@code 2.9}, {@code 37.6}.
     */
    double percentage;

    @JsonPOJOBuilder(withPrefix = "")
    public static class AreaTileBuilder {
        // Filled by lombok
    }
}
