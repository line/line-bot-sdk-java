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

package com.linecorp.bot.client;

/**
 * Defines http headers for LINE Bot API
 */
public final class LineBotAPIHeaders {

    public static final String X_LINE_CHANNEL_ID = "X-Line-ChannelID";

    public static final String X_LINE_CHANNEL_SECRET = "X-Line-ChannelSecret";

    public static final String X_LINE_TRUSTED_USER_WITH_ACL = "X-Line-Trusted-User-With-ACL";

    public static final String X_LINE_CHANNEL_SIGNATURE = "X-Line-ChannelSignature";

    private LineBotAPIHeaders() {}
}
