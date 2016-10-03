package com.linecorp.bot.model.event;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.junit.Test;

import com.linecorp.bot.model.event.message.MessageContent;

public class MessageEventTest {
    @Test
    public void constructor2ndParameterBinaryCompatibilityTest() {
        Constructor<?> constructor = MessageEvent.class.getConstructors()[0];

        Class<?>[] parameterTypes = constructor.getParameterTypes();
        assertThat(parameterTypes).hasSize(4);
        assertThat(parameterTypes[2]).isEqualTo(MessageContent.class)
                                     .isNotEqualTo(Object.class);
    }

    @Test
    public void getMessageBinaryCompatibilityTest() throws NoSuchMethodException {
        Method method = MessageEvent.class.getMethod("getMessage");

        assertThat(method.getReturnType()).isEqualTo(MessageContent.class)
                                          .isNotEqualTo(Object.class);
    }
}
