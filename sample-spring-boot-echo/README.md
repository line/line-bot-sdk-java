# sample-spring-boot-echo

sample-spring-boot-echo is a tiny sample bot application based on Spring boot.

## Usage

You can run this sample bot via gradle.

    ../gradlew bootRun -Dline.bot.channelToken=YOUR_CHANNEL_TOKEN \
                       -Dline.bot.channelSecret=YOUR_CHANNEL_SECRET

You need to pass following 2 options.

  * line.bot.channelToken: Your channel access token
  * line.bot.channelSecret: Your channel secret

## Deploy on heroku

You can deploy this module on heroku.

### Step 1

You need to get channel access token and channel secret from LINE Developers.

<img src="https://github.com/line/line-bot-sdk-java/blob/heroku/sample-spring-boot-echo/_assets/line-bot-configuration.png?raw=true">

### Step 2

Tap deploy button.

[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)

### Step 3

Fill form and run your own instance.

<img src="https://github.com/line/line-bot-sdk-java/blob/heroku/sample-spring-boot-echo/_assets/heroku.png?raw=true">

Then, tap "Manage app" button and take your heroku instance's name.

<img src="https://github.com/line/line-bot-sdk-java/blob/heroku/sample-spring-boot-echo/_assets/heroku-app-name.png?raw=true">

### Step 4

Put webhoook URL on LINE developers.

Your webhook URL is: `https://${YOUR_HEROKU_APP_NAME}.herokuapp.com/callback`

<img src="https://github.com/line/line-bot-sdk-java/blob/heroku/sample-spring-boot-echo/_assets/put-webhook-url.png?raw=true">

