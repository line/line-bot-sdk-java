/*
 * Copyright 2020 LINE Corporation
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
 *
 */

package com.linecorp.bot.model.narrowcast.recipient;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

/**
 * By setting the request ID (X-Line-Request-Id) obtained when the narrowcast message was delivered to the
 * requestId property, you can specify the users who received the narrowcast message as the target.
 *
 * @see <a href="https://developers.line.biz/en/docs/messaging-api/sending-messages/#redelivery-object">Redelivery object</a>
 */
@Value
@Builder
@JsonTypeName("redelivery")
@JsonDeserialize(builder = RedeliveryRecipient.RedeliveryRecipientBuilder.class)
public class RedeliveryRecipient implements Recipient {
    String requestId;

    @Override
    public String getType() {
        return "redelivery";
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class RedeliveryRecipientBuilder {
        // Filled by lombok
    }
}
