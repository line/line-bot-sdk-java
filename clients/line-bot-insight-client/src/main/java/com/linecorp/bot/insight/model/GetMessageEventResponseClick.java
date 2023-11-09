/*
 * Copyright 2023 LINE Corporation
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

/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech Do not edit the class manually.
 */
package com.linecorp.bot.insight.model;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/** GetMessageEventResponseClick */
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record GetMessageEventResponseClick(
    /** The URL&#39;s serial number. */
    @JsonProperty("seq") Integer seq,
    /** URL. */
    @JsonProperty("url") String url,
    /** Number of times the URL was opened. */
    @JsonProperty("click") Long click,
    /** Number of users that opened the URL. */
    @JsonProperty("uniqueClick") Long uniqueClick,
    /**
     * Number of users who opened this url through any link in the message. If a message contains
     * two links to the same URL and a user opens both links, they&#39;re counted only once.
     */
    @JsonProperty("uniqueClickOfRequest") Long uniqueClickOfRequest) {

  public static class Builder {
    private Integer seq;

    private String url;

    private Long click;

    private Long uniqueClick;

    private Long uniqueClickOfRequest;

    public Builder() {}

    public Builder seq(Integer seq) {
      this.seq = seq;

      return this;
    }

    public Builder url(String url) {
      this.url = url;

      return this;
    }

    public Builder click(Long click) {
      this.click = click;

      return this;
    }

    public Builder uniqueClick(Long uniqueClick) {
      this.uniqueClick = uniqueClick;

      return this;
    }

    public Builder uniqueClickOfRequest(Long uniqueClickOfRequest) {
      this.uniqueClickOfRequest = uniqueClickOfRequest;

      return this;
    }

    public GetMessageEventResponseClick build() {

      return new GetMessageEventResponseClick(seq, url, click, uniqueClick, uniqueClickOfRequest);
    }
  }
}
