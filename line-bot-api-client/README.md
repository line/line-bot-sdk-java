# line-bot-api-client

LINE bot API client for Java 8.

## SYNOPSIS

        LineBotClient client = LineBotClientBuilder
                .create("YOUR_CHANNEL_ID", "YOUR_CHANNEL_SECRET", "YOUR_CHANNEL_MID")
                .build();
        client.sendText("USER_MID", "Hello");

## DESCRIPTION

This library provides API client for LINE bot API.

