/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring.echo;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.FileMessageContent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@LineMessageHandler
public class EchoApplication {
    private final Logger log = LoggerFactory.getLogger(EchoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
    }

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        log.info("event: " + event);
        final String originalMessageText = event.getMessage().getText();
        switch (originalMessageText) {
            case "設定":
                return createSettingsMessage();
            case "いろいろな印刷":
                return createDesignPrintMessage();
            default:
                return createTextMessage("このトーク画面に写真や文書を送ってください。登録したプリンターで印刷します。");
        }
    }

    /**
     * handle image message event
     *
     * @param event image message event
     * @return text message, send to user
     */
    @EventMapping
    public Message handleImageMessageEvent(MessageEvent<ImageMessageContent> event) {
        log.info("event: " + event);
        // TODO: get printer name
        // TODO: get contents
        return createTextMessage("印刷を受け付けました。写真を送信します。");
    }

    /**
     * handle file message event
     *
     * @param event file message event
     * @return text message, sent to user
     */
    @EventMapping
    public Message handleFileMessageEvent(MessageEvent<FileMessageContent> event) {
        log.info("event: " + event);
        // TODO: get printer name
        return createTextMessage("印刷を受け付けました。文書を送信します。");
    }

    @EventMapping
    public Message handleDefaultMessageEvent(Event event) {
        return createTextMessage("このトーク画面に写真や文書を送ってください。登録したプリンターで印刷します。");
    }

    @EventMapping
    public Message handlePostbackEvent(PostbackEvent event) {
        switch (event.getPostbackContent().getData()) {
            case "プリンター削除":
                return createDeletePrinterMessage(event.getSource().getUserId());
            case "プリンター管理":
                return createPrintersMessage(event.getSource().getUserId());
            default:
                return createTextMessage("このトーク画面に写真や文書を送ってください。登録したプリンターで印刷します。");
        }
    }

    private TemplateMessage createPrivacyMessage() {
        final List<Action> actions = Arrays.asList(
                new URIAction("利用規約", URI.create("https://www.google.co.jp"), null),
                new URIAction("プライバシー", URI.create("https://www.google.co.jp"), null),
                new MessageAction("確認しました", "確認しました")
        );
        final Template template = new ButtonsTemplate(null, "", "ご使用前に利用規約とプライバシーポリシーをご確認ください", actions);
        return new TemplateMessage("", template);
    }

    private TemplateMessage createWelcomeMessage() {
        final List<Action> actions = Arrays.asList(
                new URIAction("お持ちでない方はこちら", URI.create("https://www.google.co.jp"), null),
                new URIAction("プリンター登録", URI.create("https://www.google.co.jp"), null)
        );
        final Template template = new ButtonsTemplate(null, "", "お使いのプリンターを登録して、写真や文書を印刷しましょう。プリンター登録には、Epson Connect のプリンターメールアドレスが必要です。", actions);
        return new TemplateMessage("", template);
    }

    private TemplateMessage createSettingsMessage() {
        final List<Action> actions = Arrays.asList(
                new URIAction("写真印刷設定", URI.create("https://www.google.co.jp"), null),
                new URIAction("文書印刷設定", URI.create("https://www.google.co.jp"), null),
                new PostbackAction("プリンター管理", "プリンター管理")
        );
        final Template template = new ButtonsTemplate(null, "", "設定する項目を選択してください。", actions);
        return new TemplateMessage("", template);
    }

    private TemplateMessage createAddPrinterMessage() {
        final List<Action> actions = Collections.singletonList(
                new URIAction("プリンター登録", URI.create("https://www.google.co.jp"), null)
        );
        final Template template = new ButtonsTemplate(null, "", "新しいプリンターを追加する", actions);
        return new TemplateMessage("", template);
    }

    private TemplateMessage createDeletePrinterMessage(String userId) {
        final List<Action> actions = Arrays.asList(
                new MessageAction("はい", "削除しました"),
                new MessageAction("いいえ", "キャンセルしました")
        );
        final String text = String.format("%s を削除しますか？", getCurrentPrinterName(userId));
        final Template template = new ButtonsTemplate(null, "", text, actions);
        return new TemplateMessage("", template);
    }

    private TemplateMessage createDesignPrintMessage() {
        final List<Action> actions = Collections.singletonList(
                new MessageAction("デザインペーパー", "削除しました")
        );
        final Template template = new ButtonsTemplate(URI.create("https://homepages.cae.wisc.edu/~ece533/images/airplane.png"), "", "いろいろなコンテンツを印刷してみましょう。200 種類のデザインペーパーを印刷できます。", actions);
        return new TemplateMessage("", template);
    }

    private TemplateMessage createPrintersMessage(String userId) {
        final List<String> printers = Arrays.asList("Printer1", "Printer2");
        final List<CarouselColumn> columns = new ArrayList<>();
        for (String printer: printers) {
            final CarouselColumn.CarouselColumnBuilder builder = CarouselColumn.builder();
            builder.title(printer);
            builder.text("MFC-JXXXX");
            builder.actions(Arrays.asList(
                    new URIAction("プリンター名変更", URI.create("https://www.google.co.jp"), null),
                    new PostbackAction("プリンター削除", "プリンター削除")
                    )
            );
            columns.add(builder.build());
        }
        final CarouselColumn.CarouselColumnBuilder builder = CarouselColumn.builder();
        builder.title("新しいプリンターを追加する");
        builder.actions(Arrays.asList(
                new URIAction("", null, null),
                new URIAction("プリンター登録", URI.create("https://www.google.co.jp"), null)
                )
        );
        columns.add(builder.build());
        final CarouselTemplate.CarouselTemplateBuilder builder1 = CarouselTemplate.builder();
        builder1.columns(columns);
        return new TemplateMessage("", builder1.build());

    }

    private TextMessage createTextMessage(String message) {
        return new TextMessage(message);
    }

    private void getContents(String messageId) {
        // TODO: get contents, send to printer
    }

    private String getCurrentPrinterName(String userId) {
        return "Printer";
    }
}
