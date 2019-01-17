package com.linecorp.bot.model.message.flex.component;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.linecorp.bot.model.message.flex.component.Image.ImageBuilder;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@RunWith(Parameterized.class)
public class ImageAspectRatioFormatTest {

    @Value
    @AllArgsConstructor
    public static class Fixture {
        private double width;
        private double height;
        private String result;
    }

    private static List<Fixture> VALUES = Arrays.asList(
            new Fixture(1, 1, "1:1"),
            new Fixture(1.01, 1.01, "1.01:1.01"),
            new Fixture(100000, 100000, "100000:100000"),
            new Fixture(Math.PI, Math.PI, "3.14159:3.14159")
    );

    @Parameters(name = "{0}")
    public static Iterable<Fixture[]> testData() {
        return VALUES.stream().map(x -> new Fixture[] { x }).collect(Collectors.toList());
    }

    private final Fixture fixture;

    @Test
    public void test() {
        final Image image =
                new ImageBuilder()
                        .aspectRatio(fixture.getWidth(), fixture.getHeight())
                        .build();
        assertThat(image.getAspectRatio()).isEqualTo(fixture.getResult());
    }
}
