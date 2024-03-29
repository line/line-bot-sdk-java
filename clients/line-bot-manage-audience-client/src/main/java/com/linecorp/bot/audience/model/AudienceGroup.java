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
package com.linecorp.bot.audience.model;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

/** Audience group */
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record AudienceGroup(
    /** The audience ID. */
    @JsonProperty("audienceGroupId") Long audienceGroupId,
    /** Get type */
    @JsonProperty("type") AudienceGroupType type,
    /** The audience&#39;s name. */
    @JsonProperty("description") String description,
    /** Get status */
    @JsonProperty("status") AudienceGroupStatus status,
    /** Get failedType */
    @JsonProperty("failedType") AudienceGroupFailedType failedType,
    /** The number of users included in the audience. */
    @JsonProperty("audienceCount") Long audienceCount,
    /** When the audience was created (in UNIX time). */
    @JsonProperty("created") Long created,
    /**
     * The request ID that was specified when the audience was created. This is only included when
     * &#x60;audienceGroup.type&#x60; is CLICK or IMP.
     */
    @JsonProperty("requestId") String requestId,
    /**
     * The URL that was specified when the audience was created. This is only included when
     * &#x60;audienceGroup.type&#x60; is CLICK and link URL is specified.
     */
    @JsonProperty("clickUrl") URI clickUrl,
    /**
     * The value indicating the type of account to be sent, as specified when creating the audience
     * for uploading user IDs.
     */
    @JsonProperty("isIfaAudience") Boolean isIfaAudience,
    /** Get permission */
    @JsonProperty("permission") AudienceGroupPermission permission,
    /** Get createRoute */
    @JsonProperty("createRoute") AudienceGroupCreateRoute createRoute) {

  public static class Builder {
    private Long audienceGroupId;
    private AudienceGroupType type;
    private String description;
    private AudienceGroupStatus status;
    private AudienceGroupFailedType failedType;
    private Long audienceCount;
    private Long created;
    private String requestId;
    private URI clickUrl;
    private Boolean isIfaAudience;
    private AudienceGroupPermission permission;
    private AudienceGroupCreateRoute createRoute;

    public Builder() {}

    public Builder audienceGroupId(Long audienceGroupId) {
      this.audienceGroupId = audienceGroupId;
      return this;
    }

    public Builder type(AudienceGroupType type) {
      this.type = type;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder status(AudienceGroupStatus status) {
      this.status = status;
      return this;
    }

    public Builder failedType(AudienceGroupFailedType failedType) {
      this.failedType = failedType;
      return this;
    }

    public Builder audienceCount(Long audienceCount) {
      this.audienceCount = audienceCount;
      return this;
    }

    public Builder created(Long created) {
      this.created = created;
      return this;
    }

    public Builder requestId(String requestId) {
      this.requestId = requestId;
      return this;
    }

    public Builder clickUrl(URI clickUrl) {
      this.clickUrl = clickUrl;
      return this;
    }

    public Builder isIfaAudience(Boolean isIfaAudience) {
      this.isIfaAudience = isIfaAudience;
      return this;
    }

    public Builder permission(AudienceGroupPermission permission) {
      this.permission = permission;
      return this;
    }

    public Builder createRoute(AudienceGroupCreateRoute createRoute) {
      this.createRoute = createRoute;
      return this;
    }

    public AudienceGroup build() {
      return new AudienceGroup(
          audienceGroupId,
          type,
          description,
          status,
          failedType,
          audienceCount,
          created,
          requestId,
          clickUrl,
          isIfaAudience,
          permission,
          createRoute);
    }
  }
}
