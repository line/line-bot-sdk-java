# line-bot-cli

Command LINE interface based on line-bot-api-client.

## Build
```
% ../gradlew clean build

> Task :line-bot-cli:bootRepackage

BUILD SUCCESSFUL in ...
```

The executable file in `./build/libs/line-bot-cli-2.2.0-SNAPSHOT-exec.jar`

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
% ./line-bot-cli-2.2.0-SNAPSHOT.jar --command=liff-list
# configuration loaded from ./application.yml
```

## Environment
```bash
% export LINE_BOT_CHANNEL_TOKEN='your token'
% export LINE_BOT_CHANNEL_SECRET='your secret'
% ./line-bot-cli-2.2.0-SNAPSHOT.jar --command=list-liff
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

Push message for specific user(s).

(Multiple "to" is supported, and multicast API is used in this case)

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
% cat liff.json
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
% ./line-bot-cli.jar --command=liff-delete --liff-id=1506753437-Xx5J85Ky
...
16:39:10  INFO - .c.LiffDeleteCommand : Successfully finished.
```


## liff-list
List your LIFF Apps.

```
% ./line-bot-cli.jar --command=liff-list
...
16:40:05  INFO - .b.c.LiffListCommand : Successfully finished.
16:40:05  INFO - .b.c.LiffListCommand : You have 3 LIFF apps.
16:40:05  INFO - .b.c.LiffListCommand : LiffApp(liffId=1506753437-2BQKOQr0, view=LiffView(type=FULL, url=https://example.com))
...
```

## liff-update
```
% cat liff.json
{
    "type": "full",
    "url": "https://example.com"
}

% ./line-bot-cli.jar --command=liff-update \
  --liff-id=1506753437-XeLp9LeE --json=update_liff.json
...
16:41:30  INFO - .c.LiffUpdateCommand : Successfully finished.
```

##  richmenu-create
```
% cat richmenu-create.yml
size:
  width: 2500
  height: 1686
selected: false
name: From CLI
chatBarText: CHAT
areas:
  - bounds: {x: 0, y: 0, width: 2500, height: 1686}
    action: {type: message, label: LABEL, text: TEXT}
% ./line-bot-cli.jar --command=richmenu-create --yaml=richmenu-create.yml
...
21:06:57  INFO - ichMenuCreateCommand : Successfully finished. RichMenuIdResponse(richMenuId=richmenu-0591a1ce01bda78f85213d347f0a966f) 
```

## richmenu-get
```
% ./line-bot-cli.jar --command=richmenu-get --rich-menu-id=richmenu-0591a1ce01bda78f85213d347f0a966f
...
12:12:22  INFO - c.RichMenuGetCommand : response = RichMenuResponse(richMenuId=richmenu-0591a1ce01bda78f85213d347f0a966f, size=RichMenuSize(width=2500, height=1686), selected=false, name=From CLI, chatBarText=CHAT, areas=[RichMenuArea(bounds=RichMenuBounds(x=0, y=0, width=2500, height=1686), action=MessageAction(label=LABEL, text=TEXT))])
```

## richmenu-delete
```
% ./line-bot-cli.jar --command=richmenu-delete --rich-menu-id=richmenu-0591a1ce01bda78f85213d347f0a966f
...
12:13:35  INFO - ichMenuDeleteCommand : Successfully finished. BotApiResponse(message=, details=[])
```

## richmenu-list
```
% ./line-bot-cli.jar --command=richmenu-list
...
12:17:12  INFO - .RichMenuListCommand : You have 3 RichMenues
12:17:12  INFO - .RichMenuListCommand : RichMenuResponse(richMenuId=richmenu-0591a1ce01bda78f85213d347f0a966f, size=RichMenuSize(width=2500, height=1686), selected=false, name=From CLI, chatBarText=CHAT, areas=[RichMenuArea(bounds=RichMenuBounds(x=0, y=0, width=2500, height=1686), action=MessageAction(label=LABEL, text=TEXT))])
... (+2 rich menues)
```

## richmenu-upload
```
% ./line-bot-cli.jar --command=richmenu-upload --command=richmenu-upload --rich-menu-id=richmenu-00e97da3ae27b54bd603cf42b9fc7672 --image=image.jpeg
...
12:19:43  INFO - c.l.bot.client.wire  : --> POST https://api.line.me/v2/bot/richmenu/richmenu-00e97da3ae27b54bd603cf42b9fc7672/content
...
12:19:43  INFO - c.l.bot.client.wire  : <-- 200 https://api.line.me/v2/bot/richmenu/richmenu-00e97da3ae27b54bd603cf42b9fc7672/content (836ms)
...
12:19:43  INFO - nuImageUploadCommand : Request Successfully finished. BotApiResponse(message=, details=[])
```

## richmenu-download
```
% ./line-bot-cli.jar --command=richmenu-download --rich-menu-id=richmenu-00e97da3ae27b54bd603cf42b9fc7672 --out=out.jpeg
...
12:23:04  INFO - c.l.bot.client.wire  : --> GET https://api.line.me/v2/bot/richmenu/richmenu-00e97da3ae27b54bd603cf42b9fc7672/content
...
12:23:05  INFO - c.l.bot.client.wire  : <-- 200 https://api.line.me/v2/bot/richmenu/richmenu-00e97da3ae27b54bd603cf42b9fc7672/content (892ms)
...
12:23:05  INFO - ImageDownloadCommand : Successfully finished. Output = out.jpeg
```

## richmenu-link
```
% ./line-bot-cli.jar --command=richmenu-link --rich-menu-id=richmenu-00e97da3ae27b54bd603cf42b9fc7672 --user-id=Ue87f273e325cd42ad2dd65946347f07f
...
12:25:14  INFO - hMenuIdToUserCommand : response = BotApiResponse(message=, details=[])
```

## richmenu-getrichmenuidofuser
```
% ./line-bot-cli.jar --command=richmenu-getrichmenuidofuser --user-id=Ue87f273e325cd42ad2dd65946347f07f
...
12:26:20  INFO - hMenuIdOfUserCommand : response = RichMenuIdResponse(richMenuId=richmenu-00e97da3ae27b54bd603cf42b9fc7672)
```

## richmenu-unlink
```
% ./line-bot-cli.jar --command=richmenu-unlink --user-id=Ue87f273e325cd42ad2dd65946347f07f
...
12:27:01  INFO - enuIdFromUserCommand : response = BotApiResponse(message=, details=[])
```

## friend-demographics-get
```
% ./line-bot-cli.jar --command=friend-demographics-get
...
17:33:43  INFO - mographicsGetCommand : Successfully finished:
available: true
genders:
- {gender: female, percentage: 52.1}
- {gender: male, percentage: 41.7}
- {gender: unknown, percentage: 6.3}
ages:
- {age: from50, percentage: 52.1}
- {age: unknown, percentage: 47.9}
...
areas:
- {area: unknown, percentage: 100.0}
- {area: 鳥取, percentage: 0.0}
- {area: 石川, percentage: 0.0}
- {area: 埼玉, percentage: 0.0}
- {area: 岐阜, percentage: 0.0}
....
appTypes:
- {appType: android, percentage: 56.3}
- {appType: ios, percentage: 43.8}
- {appType: others, percentage: 0.0}
subscriptionPeriods:
- {subscriptionPeriod: within180days, percentage: 62.5}
- {subscriptionPeriod: unknown, percentage: 37.5}
- {subscriptionPeriod: over365days, percentage: 0.0}
...
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
