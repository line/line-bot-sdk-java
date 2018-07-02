package com.example.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

public class SpringBoot2CompatibilityTestPrecondition {
    @Test
    public void springBootVersionSuccessfullyOverwrittenTest() {
        final String springBootVersion =
                SpringBootApplication.class.getPackage().getImplementationVersion();

        assertThat(springBootVersion)
                .isEqualTo("2.0.3.RELEASE");
    }

    @Test
    public void springVersionSuccessfullyOverwrittenTest() {
        final String springVersion =
                Controller.class.getPackage().getImplementationVersion();

        assertThat(springVersion)
                .matches("5.0.7.RELEASE");
    }
}
