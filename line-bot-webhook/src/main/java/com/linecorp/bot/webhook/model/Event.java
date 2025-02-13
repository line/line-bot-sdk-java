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
package com.linecorp.bot.webhook.model;



import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/** Webhook event */
@JsonSubTypes({
  @JsonSubTypes.Type(value = AccountLinkEvent.class, name = "accountLink"),
  @JsonSubTypes.Type(value = ActivatedEvent.class, name = "activated"),
  @JsonSubTypes.Type(value = BeaconEvent.class, name = "beacon"),
  @JsonSubTypes.Type(value = BotResumedEvent.class, name = "botResumed"),
  @JsonSubTypes.Type(value = BotSuspendedEvent.class, name = "botSuspended"),
  @JsonSubTypes.Type(value = DeactivatedEvent.class, name = "deactivated"),
  @JsonSubTypes.Type(value = PnpDeliveryCompletionEvent.class, name = "delivery"),
  @JsonSubTypes.Type(value = FollowEvent.class, name = "follow"),
  @JsonSubTypes.Type(value = JoinEvent.class, name = "join"),
  @JsonSubTypes.Type(value = LeaveEvent.class, name = "leave"),
  @JsonSubTypes.Type(value = MemberJoinedEvent.class, name = "memberJoined"),
  @JsonSubTypes.Type(value = MemberLeftEvent.class, name = "memberLeft"),
  @JsonSubTypes.Type(value = MembershipEvent.class, name = "membership"),
  @JsonSubTypes.Type(value = MessageEvent.class, name = "message"),
  @JsonSubTypes.Type(value = ModuleEvent.class, name = "module"),
  @JsonSubTypes.Type(value = PostbackEvent.class, name = "postback"),
  @JsonSubTypes.Type(value = ThingsEvent.class, name = "things"),
  @JsonSubTypes.Type(value = UnfollowEvent.class, name = "unfollow"),
  @JsonSubTypes.Type(value = UnsendEvent.class, name = "unsend"),
  @JsonSubTypes.Type(value = VideoPlayCompleteEvent.class, name = "videoPlayComplete"),
})
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
    defaultImpl = UnknownEvent.class,
    visible = true)
public interface Event {

  Source source();

  /** Time of the event in milliseconds. */
  Long timestamp();

  EventMode mode();

  /**
   * Webhook Event ID. An ID that uniquely identifies a webhook event. This is a string in ULID
   * format.
   */
  String webhookEventId();

  DeliveryContext deliveryContext();
}
