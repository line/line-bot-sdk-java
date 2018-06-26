# line-bot-cli

Command LINE interface based on line-bot-api-client.

## Usage
```
% cat application.yml
line.bot:
  channel-token: 'your token'
  channel-secret: 'your secret'

% ./line-bot-cli.jar --command=liff-list
...
16:40:05  INFO - .b.c.LiffListCommand : Successfully finished.
16:40:05  INFO - .b.c.LiffListCommand : You have 0 LIFF apps.

% cat liff.json
{
    "type": "full",
    "url": "https://example.com"
}

% ./line-bot-cli.jar --command=liff-create --json=liff.json
...
16:37:40  INFO - .c.LiffCreateCommand : Successfully finished. Response : LiffAppAddResponse(liffId=1506753437-Xx5J85Ky)

% ./line-bot-cli.jar --command=liff-list
...
16:40:05  INFO - .b.c.LiffListCommand : Successfully finished.
16:40:05  INFO - .b.c.LiffListCommand : You have 1 LIFF apps.
16:40:05  INFO - .b.c.LiffListCommand : LiffApp(liffId=1506753437-Xx5J85Ky, view=LiffView(type=FULL, url=https://example.com))
```

# Pre-requirement
You need pass `line.bot.channel-token` and `line.bot.channel-secret` to CLI.

CLI supports SpringBoot's [Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html) rule.

## Configuration yml way. (Recommended)
Put `application.yml` into current directory such as.

```yml:application.yml
line.bot:
  channel-token: 'your token'
  channel-secret: 'your secret'
```

Then you can run 

```bash
% ./line-bot-cli-1.19.0-SNAPSHOT.jar --command=liff-list
# configuration loaded from ./application.yml
```

## Environment
```bash
% export LINE_BOT_CHANNEL_TOKEN='your token'
% export LINE_BOT_CHANNEL_SECRET='your secret'
% ./line-bot-cli-1.19.0-SNAPSHOT.jar --command=list-liff
```

# Synopsis

## Common argument

|  Name    |      |
| -------- | ---- |
|  --liff-id| LIFF App ID to DELETE/UPDATE  |
|  --json  |  JSON file path to be sent. <br />Either this, `--data` or `--yaml` is available. |
|  --yaml  |  YAML file path to be sent. |
|  --data  |  Raw json data to be sent. |

## message-push

Push message for specific user(s). (Internally use multicast API)

```
% cat message-push.json
{
  "to": [ "Ue87f273e325cd42ad2dd65946347f07f" ],
  "messages": [
    {
      "type": "text",
      "text": "Hello, Workd!"
    }
  ]
}
% ./line-bot-cli.jar --command=message-push --json=message-push.json
...
18:47:47  INFO - c.l.bot.client.wire  : <-- 200 https://api.line.me/v2/bot/message/multicast (367ms)
```

## liff-create
Create LIFF App.

```
% cat liff.json                                                                                                         [liff-api *+$=] /Users/JP20217/github.com/line/line-bot-sdk-java/line-bot-cli/build
{
    "type": "full",
    "url": "https://example.com"
}
% ./line-bot-cli.jar --command=liff-create --json=liff.json
...
16:37:40  INFO - .c.LiffCreateCommand : Successfully finished. Response : LiffAppAddResponse(liffId=1506753437-Xx5J85Ky)
```

## liff-delete
Delete LIFF App.

```
% ./line-bot-cli.jar --command=liff-delete --liff-id=1506753437-Xx5J85Ky                           [liff-api *+$=] /Users/JP20217/github.com/line/line-bot-sdk-java/line-bot-cli/build
...
16:39:10  INFO - .c.LiffDeleteCommand : Successfully finished.
```


## liff-list
List your LIFF Apps.

```
% ./line-bot-cli.jar --command=liff-list                                                           [liff-api *+$=] /Users/JP20217/github.com/line/line-bot-sdk-java/line-bot-cli/build
...
16:40:05  INFO - .b.c.LiffListCommand : Successfully finished.
16:40:05  INFO - .b.c.LiffListCommand : You have 3 LIFF apps.
16:40:05  INFO - .b.c.LiffListCommand : LiffApp(liffId=1506753437-2BQKOQr0, view=LiffView(type=FULL, url=https://example.com))
...
```

## liff-update
```
% cat liff.json                                                                                                         [liff-api *+$=] /Users/JP20217/github.com/line/line-bot-sdk-java/line-bot-cli/build
{
    "type": "full",
    "url": "https://example.com"
}

% ./line-bot-cli.jar --command=liff-update \
  --liff-id=1506753437-XeLp9LeE --json=update_liff.json
...
16:41:30  INFO - .c.LiffUpdateCommand : Successfully finished.
```

# Tips
## Handling multiple bots.

You can put configurations for multiple bots into one configuration file.

Example:

```yaml:application.yml
line.bot:
  channel-token: 'dev token'
  channel-secret: 'dev secret'

---
spring.profiles: production
line.bot:
  channel-token: 'production token'
  channel-secret: 'production secret'
```

Default target is development environment.

You can switch production configuration by adding `--spring.profiles.active=production`.

For more details. Please visit SpringBoot's [Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html).
