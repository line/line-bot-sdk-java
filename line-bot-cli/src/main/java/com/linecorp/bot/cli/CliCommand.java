package com.linecorp.bot.cli;

public interface CliCommand {
    /**
     * @throws Exception
     */
    abstract void execute() throws Exception;
}
