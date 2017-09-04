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

<img src="../docs/img/lab1/deploy.png">

> Contingency
> In case Step 3 does not work, type the following in your terminal instead. You will need to install `git` and `heroku cli` in your machine. In our lab, `git` is installed and `heroku cli` is available at `L:\apps\comp3111`
> 1. git clone https://github.com/khwang0/Line-chatbot-for-COMP3111.git
> 2. heroku create
> 3. heroku heroku config:set LINE\_BOT\_CHANNEL\_TOKEN=1234 LINE\_BOT\_CHANNEL\_SECRET=\4567 ITSC\_LOGIN=kevinw  #assume 1234, 4567, and kevinw are the values you are having from Step 1.
> 4. git push heroku master



### Step 4

Tap the **Manage App** button and copy the name of your Heroku instance.

<img src="https://github.com/line/line-bot-sdk-java/blob/master/sample-spring-boot-echo/_assets/heroku-app-name.png?raw=true">

### Step 5

Set the webhook URL for your Channel on the Channel Console using the following URL:
`https://${YOUR_HEROKU_APP_NAME}.herokuapp.com/callback`

<img src="https://github.com/line/line-bot-sdk-java/blob/master/sample-spring-boot-echo/_assets/put-webhook-url.png?raw=true">
