package com.linecorp.bot.spring.boot;

import com.linecorp.bot.client.LineMessagingServiceBuilder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by pptwe on 2017-04-15.
 */
@Data
public class BotInfo
{
    /**
     * Channel acccess token.
     */
    @Valid
    @NotNull
    private String channelToken;

    /**
     * Channel secret
     */
    @Valid
    @NotNull
    private String channelSecret;

    @Valid
    @NotNull
    private String apiEndPoint = LineMessagingServiceBuilder.DEFAULT_API_END_POINT;

    /**
     * Connection timeout in milliseconds
     */
    @Valid
    @NotNull
    private long connectTimeout = LineMessagingServiceBuilder.DEFAULT_CONNECT_TIMEOUT;

    /**
     * Read timeout in milliseconds
     */
    @Valid
    @NotNull
    private long readTimeout = LineMessagingServiceBuilder.DEFAULT_READ_TIMEOUT;

    /**
     * Write timeout in milliseconds
     */
    @Valid
    @NotNull
    private long writeTimeout = LineMessagingServiceBuilder.DEFAULT_WRITE_TIMEOUT;
}
