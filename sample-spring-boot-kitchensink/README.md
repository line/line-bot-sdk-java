# The Messaging API SDK for Java

## Deploy on Heroku

Deploy this module on Heroku.

### Step 1

Get the Channel access token and Channel secret from the Channel Console.

<img src="https://github.com/line/line-bot-sdk-java/blob/master/sample-spring-boot-echo/_assets/line-bot-configuration.png?raw=true">

### Step 2

Tap the deploy button.

[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/khwang0/Line-chatbot-for-COMP3111)

### Step 3

Fill in the form and run your instance.

<img src="../docs/img/labs/deploy.png">

### Step 4

Tap the **Manage App** button and copy the name of your Heroku instance.

<img src="https://github.com/line/line-bot-sdk-java/blob/master/sample-spring-boot-echo/_assets/heroku-app-name.png?raw=true">

### Step 5

Set the webhook URL for your Channel on the Channel Console using the following URL:
`https://${YOUR_HEROKU_APP_NAME}.herokuapp.com/callback`

<img src="https://github.com/line/line-bot-sdk-java/blob/master/sample-spring-boot-echo/_assets/put-webhook-url.png?raw=true">
