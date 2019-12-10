/*
 * Copyright 2018 LINE Corporation
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

package com.linecorp.bot.cli;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.util.concurrent.Futures.getUnchecked;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.linecorp.bot.cli.arguments.Arguments;
import com.linecorp.bot.client.LineBlobClient;
import com.linecorp.bot.client.MessageContentResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "command", havingValue = "richmenu-download")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RichMenuImageDownloadCommand implements CliCommand {
    private LineBlobClient lineBlobClient;
    private Arguments arguments;

    @Override
    public void execute() throws IOException {
        final String richMenuId = checkNotNull(arguments.getRichMenuId(), "--rich-menu-id= is not set.");
        final String out = checkNotNull(arguments.getOut(), "--out= is not set.");
        final MessageContentResponse messageContentResponse =
                getUnchecked(lineBlobClient.getRichMenuImage(richMenuId));

        log.info("Request Successfully finished. {}", messageContentResponse);

        try (OutputStream os = new FileOutputStream(out)) {
            StreamUtils.copy(messageContentResponse.getStream(), os);
        }

        log.info("Successfully finished. Output = {}", out);
    }
}
