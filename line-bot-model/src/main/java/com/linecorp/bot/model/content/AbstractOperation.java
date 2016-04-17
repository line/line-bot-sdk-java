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

package com.linecorp.bot.model.content;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.ToString;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "opType",
        visible = true,
        defaultImpl = Object.class)
@JsonSubTypes({
                      @JsonSubTypes.Type(value = AddedAsFriendOperation.class, name = "4"),
                      @JsonSubTypes.Type(value = BlockedOperation.class, name = "8")
              })

@ToString
@Getter
public abstract class AbstractOperation implements Content {
    private final long revision;
    private final long opType;
    private final String mid;

    public AbstractOperation(long revision, long opType, String mid) {
        this.revision = revision;
        this.opType = opType;
        this.mid = mid;
    }

    public long getRevision() {
        return revision;
    }

    public long getOpType() {
        return opType;
    }

    public String getMid() {
        return mid;
    }
}
