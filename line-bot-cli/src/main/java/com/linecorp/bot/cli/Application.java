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

import java.util.Arrays;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class Application {
    /**
     * Entry point of line-bot-cli.
     */
    public static void main(final String... args) throws Exception {
        try (ConfigurableApplicationContext context = SpringApplication.run(Application.class, args)) {
            log.info("Arguments: {}", Arrays.asList(args));

            try {
                context.getBean(Application.class).run(context);
            } catch (Exception e) {
                log.error("Exception in command execution", e);
            }
        }
    }

    void run(final ConfigurableApplicationContext context) throws Exception {
        final Map<String, CliCommand> commandMap = context.getBeansOfType(CliCommand.class);
        if (commandMap.isEmpty()) {
            log.warn("No command resolved. Available commands are follows.");
            printSupportCommand();
            return;
        }
        if (commandMap.size() > 1) {
            throw new RuntimeException("Multiple command matching. Maybe bug.");
        }

        final CliCommand command = commandMap.values().iterator().next();

        log.info("\"--command\" resolved to > {}", command.getClass());

        command.execute();
    }

    void printSupportCommand() {
        for (Class<?> clazz : new Class<?>[] {
                RichMenuCreateCommand.class,
                RichMenuGetCommand.class,
                RichMenuDeleteCommand.class,
                RichMenuListCommand.class,
                RichMenuImageUploadCommand.class,
                RichMenuImageDownloadCommand.class,
                RichMenuLinkRichMenuIdToUserCommand.class,
                RichMenuUnlinkRichMenuIdFromUserCommand.class,
                RichMenuGetRichMenuIdOfUserCommand.class,
                LiffCreateCommand.class,
                LiffDeleteCommand.class,
                LiffListCommand.class,
                LiffUpdateCommand.class,
                MessagePushCommand.class,
                FriendDemographicsGetCommand.class
        }) {
            final ConditionalOnProperty conditionalOnProperty =
                    clazz.getAnnotation(ConditionalOnProperty.class);

            log.info("     {} > {}",
                     conditionalOnProperty.havingValue(),
                     clazz);
        }
    }
}
