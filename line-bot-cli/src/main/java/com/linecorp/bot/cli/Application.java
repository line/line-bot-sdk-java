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
                LiffCreateCommand.class,
                LiffDeleteCommand.class,
                LiffListCommand.class,
                LiffUpdateCommand.class,
                MessagePushCommand.class
        }) {
            final ConditionalOnProperty conditionalOnProperty =
                    clazz.getAnnotation(ConditionalOnProperty.class);

            log.info("     {} > {}",
                     conditionalOnProperty.havingValue(),
                     clazz);
        }
    }
}
