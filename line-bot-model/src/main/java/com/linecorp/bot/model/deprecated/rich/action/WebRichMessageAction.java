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

package com.linecorp.bot.model.deprecated.rich.action;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class WebRichMessageAction extends AbstractRichMessageAction {
    private final String text;
    private final WebRichMessageActionParams params;

    public WebRichMessageAction(@NonNull String text, @NonNull String linkUri) {
        super("web");
        this.text = text;
        this.params = new WebRichMessageActionParams(linkUri);
    }

    public String getText() {return this.text;}

    public WebRichMessageActionParams getParams() {return this.params;}

    @Getter
    @ToString
    @EqualsAndHashCode
    public static class WebRichMessageActionParams {
        private final String linkUri;

        public WebRichMessageActionParams(@NonNull String linkUri) {
            this.linkUri = linkUri;
        }
    }
}
