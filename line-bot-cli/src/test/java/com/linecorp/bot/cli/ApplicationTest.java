package com.linecorp.bot.cli;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

public class ApplicationTest {
    @Rule
    public final SystemOutRule systemOut = new SystemOutRule().enableLog();

    @Test
    public void contextStartupTest() throws Exception {
        Application.main("--line.bot.channel-secret=xxx", "--line.bot.channel-token=token");

        assertThat(systemOut.getLogWithNormalizedLineSeparator())
                .contains("No command resolved.");
    }
}
