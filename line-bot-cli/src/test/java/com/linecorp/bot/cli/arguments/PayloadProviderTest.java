package com.linecorp.bot.cli.arguments;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

public class PayloadProviderTest {
    private static final TextMessage TEXT_MESSAGE_EXPECTED = new TextMessage("Hello, World!");

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

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
