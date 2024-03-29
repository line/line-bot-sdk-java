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
package com.linecorp.bot.oauth.model;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Issued stateless channel access token
 *
 * @see <a
 *     href="https://developers.line.biz/en/reference/messaging-api/#issue-stateless-channel-access-token">
 *     Documentation</a>
 */
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record IssueStatelessChannelAccessTokenResponse(
    /**
     * A stateless channel access token. The token is an opaque string which means its format is an
     * implementation detail and the consumer of this token should never try to use the data parsed
     * from the token.
     */
    @JsonProperty("access_token") String accessToken,
    /** Duration in seconds after which the issued access token expires */
    @JsonProperty("expires_in") Integer expiresIn,
    /** Token type. The value is always &#x60;Bearer&#x60;. */
    @JsonProperty("token_type") String tokenType) {

  public static class Builder {
    private String accessToken;
    private Integer expiresIn;
    private String tokenType;

    public Builder(String accessToken, Integer expiresIn, String tokenType) {

      this.accessToken = accessToken;

      this.expiresIn = expiresIn;

      this.tokenType = tokenType;
    }

    public IssueStatelessChannelAccessTokenResponse build() {
      return new IssueStatelessChannelAccessTokenResponse(accessToken, expiresIn, tokenType);
    }
  }
}
