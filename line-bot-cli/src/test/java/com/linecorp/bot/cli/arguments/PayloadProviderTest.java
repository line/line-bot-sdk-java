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

package com.linecorp.bot.cli.arguments;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

@ExtendWith(MockitoExtension.class)
public class PayloadProviderTest {
    private static final TextMessage TEXT_MESSAGE_EXPECTED = new TextMessage("Hello, World!");

    @InjectMocks
    private PayloadProvider target;

    @Mock
    private PayloadArguments arguments;

    @Test
    public void dataTest() {
        when(arguments.getData())
                .thenReturn("{\"type\":\"text\",\"text\":\"Hello, World!\"}");

        // Verify
        final Message messageRead = target.read(Message.class);
        assertThat(messageRead)
                .isEqualTo(TEXT_MESSAGE_EXPECTED);
    }

    @Test
    public void jsonTest() throws IOException {
        when(arguments.getJson())
                .thenReturn(extractTestResourceAsTempFile("message.json"));

        // Verify
        final Message messageRead = target.read(Message.class);
        assertThat(messageRead)
                .isEqualTo(TEXT_MESSAGE_EXPECTED);
    }

    @Test
    public void yamlTest() throws IOException {
        when(arguments.getYaml())
                .thenReturn(extractTestResourceAsTempFile("message.yaml"));

        // Verify
        final Message messageRead = target.read(Message.class);
        assertThat(messageRead)
                .isEqualTo(TEXT_MESSAGE_EXPECTED);
    }

    @Test
    public void unsetPropertyExceptionTest() {
        assertThatThrownBy(() -> {
            target.read(Message.class);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    private String extractTestResourceAsTempFile(final String resourceName) throws IOException {
        final Path tempFile = Files.createTempFile(resourceName.replaceAll("[^a-z]", ""), ".junit");
        Files.copy(ClassLoader.getSystemResourceAsStream(resourceName), tempFile, REPLACE_EXISTING);
        return tempFile.toString();
    }
}
