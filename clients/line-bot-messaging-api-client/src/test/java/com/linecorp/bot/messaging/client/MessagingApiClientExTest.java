/*
 * Copyright 2024 LINE Corporation
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

package com.linecorp.bot.messaging.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.findAll;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.github.tomakehurst.wiremock.WireMockServer;

import com.linecorp.bot.client.base.HeaderInterceptor;
import com.linecorp.bot.client.base.Result;
import com.linecorp.bot.messaging.model.BroadcastRequest;
import com.linecorp.bot.messaging.model.CouponCreateRequest;
import com.linecorp.bot.messaging.model.CouponCreateResponse;
import com.linecorp.bot.messaging.model.CouponDiscountRewardRequest;
import com.linecorp.bot.messaging.model.CouponDiscountRewardResponse;
import com.linecorp.bot.messaging.model.CouponResponse;
import com.linecorp.bot.messaging.model.DatetimePickerAction;
import com.linecorp.bot.messaging.model.DiscountFixedPriceInfoRequest;
import com.linecorp.bot.messaging.model.DiscountFixedPriceInfoResponse;
import com.linecorp.bot.messaging.model.FlexBox;
import com.linecorp.bot.messaging.model.FlexBoxLinearGradient;
import com.linecorp.bot.messaging.model.FlexBubble;
import com.linecorp.bot.messaging.model.FlexButton;
import com.linecorp.bot.messaging.model.FlexMessage;
import com.linecorp.bot.messaging.model.FlexText;
import com.linecorp.bot.messaging.model.GetAggregationUnitNameListResponse;
import com.linecorp.bot.messaging.model.GetFollowersResponse;
import com.linecorp.bot.messaging.model.ImageCarouselColumn;
import com.linecorp.bot.messaging.model.ImageCarouselTemplate;
import com.linecorp.bot.messaging.model.LotteryAcquisitionConditionRequest;
import com.linecorp.bot.messaging.model.LotteryAcquisitionConditionResponse;
import com.linecorp.bot.messaging.model.NarrowcastProgressResponse;
import com.linecorp.bot.messaging.model.PushMessageRequest;
import com.linecorp.bot.messaging.model.PushMessageResponse;
import com.linecorp.bot.messaging.model.QuickReply;
import com.linecorp.bot.messaging.model.QuickReplyItem;
import com.linecorp.bot.messaging.model.ReplyMessageRequest;
import com.linecorp.bot.messaging.model.ReplyMessageResponse;
import com.linecorp.bot.messaging.model.RichMenuIdResponse;
import com.linecorp.bot.messaging.model.RichMenuSwitchAction;
import com.linecorp.bot.messaging.model.TemplateMessage;
import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.messaging.model.URIAction;
import com.linecorp.bot.messaging.model.UserProfileResponse;

@ExtendWith(MockitoExtension.class)
@Timeout(5)
public class MessagingApiClientExTest {
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private WireMockServer wireMockServer;
    private MessagingApiClient target;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());

        target = MessagingApiClient.builder("MY_OWN_TOKEN")
                .apiEndPoint(URI.create(wireMockServer.baseUrl()))
                .build();
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void getFollowers() throws IOException {
        stubFor(get(urlPathEqualTo("/v2/bot/followers/ids"))
                .withQueryParam("limit", equalTo(String.valueOf(99)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withHeader("x-line-request-id", "ppp")
                        .withBody("{\n" +
                                "  \"userIds\": [\"U4af4980629...\", \"U0c229f96c4...\", \"U95afb1d4df...\"],\n" +
                                "  \"next\": \"yANU9IA...\"\n" +
                                "}")));
        // Do
        Result<GetFollowersResponse> result = target.getFollowers(null, 99)
                .join();

        // Verify
        assertThat(result.requestId()).isEqualTo("ppp");
        GetFollowersResponse responseBody = requireNonNull(result.body());
        assertThat(responseBody.userIds()).containsExactly("U4af4980629...", "U0c229f96c4...", "U95afb1d4df...");
        assertThat(responseBody.next()).isEqualTo("yANU9IA...");

        verify(getRequestedFor(urlPathEqualTo("/v2/bot/followers/ids"))
                .withoutFormParam("start")
                .withQueryParam("limit", equalTo(String.valueOf(99))));
    }

    @Test
    public void listCoupon() {
        stubFor(get(urlPathEqualTo("/v2/bot/coupon"))
                .withQueryParam("status", equalTo("RUNNING"))
                .withQueryParam("status", equalTo("CLOSED"))
                .withQueryParam("start", equalTo("startToken"))
                .withQueryParam("limit", equalTo(String.valueOf(10)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withHeader("x-line-request-id", "ppp")
                        .withBody("{\n" +
                                "  \"items\": [{ \"couponId\": \"abc\", \"title\": \"test\" }],\n" +
                                "  \"next\": \"nextToken\"\n" +
                                "}")));

        Set<String> status = Set.of("RUNNING", "CLOSED");
        final var result =
                target.listCoupon(status, "startToken", 10).join();

        assertThat(result.requestId()).isEqualTo("ppp");

        final var responseBody = requireNonNull(result.body());
        assertThat(responseBody.items()).hasSize(1);
        assertThat(responseBody.items().get(0).couponId()).isEqualTo("abc");
        assertThat(responseBody.next()).isEqualTo("nextToken");

        verify(getRequestedFor(urlPathEqualTo("/v2/bot/coupon"))
                .withQueryParam("status", equalTo("RUNNING"))
                .withQueryParam("status", equalTo("CLOSED"))
                .withQueryParam("start", equalTo("startToken"))
                .withQueryParam("limit", equalTo(String.valueOf(10))));

        final var req = findAll(getRequestedFor(urlPathEqualTo("/v2/bot/coupon"))).get(0);
        Map<String, Set<String>> actual = req.getQueryParams().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> Set.copyOf(e.getValue().values())
                ));

        Map<String, Set<String>> expected = Map.of(
                "status", Set.of("RUNNING", "CLOSED"),
                "start", Set.of("startToken"),
                "limit", Set.of("10")
        );

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getNarrowcastProgress() {
        String requestId = UUID.randomUUID().toString();
        stubFor(get(urlPathEqualTo("/v2/bot/message/progress/narrowcast"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withHeader("x-line-request-id", "ppp")
                        .withBody("""
                                {
                                    "phase": "succeeded",
                                    "successCount": 100,
                                    "failureCount": 10,
                                    "targetCount": 110,
                                    "failedDescription": "description"
                                }
                                """)));

        // Do
        Result<NarrowcastProgressResponse> result = target.getNarrowcastProgress(requestId)
                .join();

        // Verify
        assertThat(result.requestId()).isEqualTo("ppp");
        NarrowcastProgressResponse responseBody = requireNonNull(result.body());
        assertThat(responseBody.phase()).isEqualTo(NarrowcastProgressResponse.Phase.SUCCEEDED);
        assertThat(responseBody.successCount()).isEqualTo(100);
        assertThat(responseBody.failureCount()).isEqualTo(10);
        assertThat(responseBody.targetCount()).isEqualTo(110);
        assertThat(responseBody.failedDescription()).isEqualTo("description");

        verify(getRequestedFor(urlPathEqualTo("/v2/bot/message/progress/narrowcast"))
                .withQueryParam("requestId", equalTo(requestId)));
    }

    @Test
    public void pushMessage() {
        stubFor(post(urlPathTemplate("/v2/bot/message/push"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody(
                                """
                                {
                                    "sentMessages": [
                                        {
                                            "id": "461230966842064897",
                                            "quoteToken": "IStG5h1Tz7b..."
                                        }
                                    ]
                                }
                                """)));

        // Do
        Result<PushMessageResponse> result = target.pushMessage(
                        null,
                        new PushMessageRequest("U4af4980629...",
                                List.of(new TextMessage("Hello, world")), false, null)
                )
                .join();

        // Verify
        PushMessageResponse responseBody = requireNonNull(result.body());
        assertThat(responseBody.sentMessages()).isNotEmpty();
        verify(postRequestedFor(urlPathEqualTo("/v2/bot/message/push"))
                .withRequestBody(containing("\"to\":\"U4af4980629...\""))
                .withRequestBody(containing("\"type\":\"text\""))
                .withRequestBody(containing("\"text\":\"Hello, world\"")));
    }

    @Test
    public void broadcastMessage() {
        stubFor(post(urlPathTemplate("/v2/bot/message/broadcast"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{}")));

        // Do
        Result<Object> result = target.broadcast(
                        null,
                        new BroadcastRequest(List.of(new TextMessage("Hello, world")), false)
                )
                .join();

        // Verify
        assertThat(result.body()).isNotNull();
        verify(postRequestedFor(urlPathEqualTo("/v2/bot/message/broadcast"))
                .withRequestBody(containing("\"type\":\"text\""))
                .withRequestBody(containing("\"text\":\"Hello, world\"")));
    }

    @Test
    public void getAggregationUnitNameList() {
        stubFor(get(urlPathTemplate("/v2/bot/message/aggregation/list"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("""
                                {
                                    "customAggregationUnits": [
                                        "unit1",
                                        "unit2"
                                    ],
                                    "next": "nextToken"
                                }
                                """)));

        // Do
        Result<GetAggregationUnitNameListResponse> result = target.getAggregationUnitNameList(null, null)
                .join();

        // Verify
        GetAggregationUnitNameListResponse responseBody = requireNonNull(result.body());
        assertThat(responseBody.customAggregationUnits()).isNotEmpty();
        assertThat(responseBody.customAggregationUnits()).containsExactly("unit1", "unit2");
        assertThat(responseBody.next()).isEqualTo("nextToken");

        verify(getRequestedFor(urlPathEqualTo("/v2/bot/message/aggregation/list")));
    }

    @Test
    public void getDefaultRichMenu() {
        stubFor(get(urlEqualTo("/v2/bot/user/all/richmenu"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("""
                                {
                                  "richMenuId": "richmenu-1234abcd"
                                }
                                """)));

        // Do
        Result<RichMenuIdResponse> result = target.getDefaultRichMenuId()
                .join();

        // Verify
        RichMenuIdResponse responseBody = requireNonNull(result.body());
        assertThat(responseBody.richMenuId()).isEqualTo("richmenu-1234abcd");

        verify(getRequestedFor(urlEqualTo("/v2/bot/user/all/richmenu")));
    }

    @Test
    public void setDefaultRichMenu() {
        stubFor(post(urlPathTemplate("/v2/bot/user/all/richmenu/{richMenuId}"))
                .willReturn(aResponse()
                        .withStatus(200)));

        // Do
        Result<Void> result = target.setDefaultRichMenu("richmenu-1234abcd")
                .join();

        // Verify
        assertThat(result.body()).isNull();

        verify(postRequestedFor(urlEqualTo("/v2/bot/user/all/richmenu/richmenu-1234abcd")));
    }

    @Test
    public void cancelDefaultRichMenu() {
        stubFor(delete(urlEqualTo("/v2/bot/user/all/richmenu"))
                .willReturn(aResponse()
                        .withStatus(200)));

        // Do
        Result<Void> result = target.cancelDefaultRichMenu()
                .join();

        // Verify
        assertThat(result.body()).isNull();

        verify(deleteRequestedFor(urlEqualTo("/v2/bot/user/all/richmenu")));
    }

    @Test
    public void sendUserAgentHeader() {
        stubFor(get(urlPathTemplate("/v2/bot/profile/{userId}"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("""
                                {
                                    "displayName": "Brown",
                                    "userId": "U4af4980629...",
                                    "pictureUrl": "https://example.com/a.jpg",
                                    "statusMessage": "Hello, world"
                                }""")));

        // Do
        Result<UserProfileResponse> result = target.getProfile("U4af4980629...")
                .join();

        // Verify
        UserProfileResponse responseBody = requireNonNull(result.body());
        assertThat(responseBody.displayName()).isEqualTo("Brown");
        assertThat(responseBody.userId()).isEqualTo("U4af4980629...");
        assertThat(responseBody.pictureUrl().toString()).isEqualTo("https://example.com/a.jpg");
        assertThat(responseBody.statusMessage()).isEqualTo("Hello, world");

        verify(getRequestedFor(urlPathEqualTo("/v2/bot/profile/U4af4980629..."))
                .withHeader("User-Agent", equalTo(
                        "line-botsdk-java/" + HeaderInterceptor.class.getPackage().getImplementationVersion())));
    }

    @Test
    public void ignoreUnknownFieldsInResponse() {
        stubFor(post(urlPathTemplate("/v2/bot/message/reply"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("""
                                {
                                    "sentMessages": [
                                        {
                                            "id": "461230966842064897",
                                            "quoteToken": "IStG5h1Tz7b..."
                                        }
                                    ],
                                    "invalidField": "foobar"
                                }
                                """)));

        // Do
        Result<ReplyMessageResponse> result = target.replyMessage(
                new ReplyMessageRequest(
                        "nHuyWiB7yP5Zw52FIkcQobQuGDXCTA",
                        List.of(new TextMessage("Hello, world")), false
                )).join();

        // Verify
        ReplyMessageResponse responseBody = requireNonNull(result.body());
        assertThat(responseBody.sentMessages()).isNotEmpty();
        verify(postRequestedFor(urlPathEqualTo("/v2/bot/message/reply"))
                .withRequestBody(containing("\"replyToken\":\"nHuyWiB7yP5Zw52FIkcQobQuGDXCTA\""))
                .withRequestBody(containing("\"type\":\"text\""))
                .withRequestBody(containing("\"text\":\"Hello, world\"")));
    }

    @Test
    public void datetimePickerAction() {
        stubFor(post(urlPathTemplate("/v2/bot/message/broadcast"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{}")));

        // Do
        Result<Object> result = target.broadcast(
                null,
                new BroadcastRequest(List.of(
                        new TextMessage.Builder("Please pick a date/time!")
                                .quickReply(
                                        new QuickReply(List.of(
                                                new QuickReplyItem(
                                                        new DatetimePickerAction.Builder()
                                                                .label("Pick")
                                                                .data("some_data")
                                                                .mode(DatetimePickerAction.Mode.DATETIME)
                                                                .build()
                                                )
                                        ))
                                ).build()
                ), false
                )).join();

        String expectedBody = """
            {
              "messages": [
                {
                  "type": "text",
                  "text": "Please pick a date/time!",
                  "quickReply": {
                    "items": [
                      {
                        "type": "action",
                        "action": {
                          "type": "datetimepicker",
                          "data": "some_data",
                          "mode": "datetime",
                          "label": "Pick"
                        }
                      }
                    ]
                  }
                }
              ],
              "notificationDisabled": false
            }
            """;

        // Verify
        assertThat(result.body()).isNotNull();
        verify(postRequestedFor(urlPathEqualTo("/v2/bot/message/broadcast"))
                .withRequestBody(equalToJson(expectedBody, true, true)));
    }

    @Test
    public void richMenuSwitchAction() {
        stubFor(post(urlPathTemplate("/v2/bot/message/broadcast"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{}")));

        // Do
        FlexMessage flexMessage = new FlexMessage(
                "Switch RichMenu",
                new FlexBubble.Builder()
                        .body(
                                new FlexBox.Builder(
                                        FlexBox.Layout.VERTICAL,
                                        List.of(
                                                new FlexButton.Builder(
                                                        new RichMenuSwitchAction(
                                                                "Switch Menu",
                                                                "switch_richmenu",
                                                                "alias_xxx"
                                                        )
                                                ).build()
                                        )
                                ).build()
                        )
                        .build()
        );

        Result<Object> result = target.broadcast(
                null,
                new BroadcastRequest(List.of(flexMessage), false)
        ).join();

        String expectedBody = """
            {
              "messages": [
                {
                  "type": "flex",
                  "altText": "Switch RichMenu",
                  "contents": {
                    "type": "bubble",
                    "body": {
                      "type": "box",
                      "layout": "vertical",
                      "contents": [
                        {
                          "type": "button",
                          "action": {
                            "type": "richmenuswitch",
                            "data": "switch_richmenu",
                            "richMenuAliasId": "alias_xxx",
                            "label": "Switch Menu"
                          }
                        }
                      ]
                    }
                  }
                }
              ],
              "notificationDisabled": false
            }
            """;

        // Verify
        assertThat(result.body()).isNotNull();
        verify(postRequestedFor(urlPathEqualTo("/v2/bot/message/broadcast"))
                .withRequestBody(equalToJson(expectedBody, true, true)));
    }

    @Test
    public void imageCarouselTemplate() {
        stubFor(post(urlPathTemplate("/v2/bot/message/broadcast"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{}")));

        // Do
        Result<Object> result = target.broadcast(
                null,
                new BroadcastRequest(List.of(
                        new TemplateMessage.Builder(
                                "This is an Image Carousel",
                                new ImageCarouselTemplate.Builder(
                                        List.of(
                                                new ImageCarouselColumn(
                                                        URI.create("https://example.com/image1.png"),
                                                        new URIAction.Builder()
                                                                .label("Go")
                                                                .uri(URI.create("https://example.com/"))
                                                                .build()
                                                )
                                        )
                                ).build()
                        ).build()
                ), false
                )).join();

        String expectedBody = """
            {
                "messages": [
                    {
                        "type": "template",
                        "altText": "This is an Image Carousel",
                        "template": {
                            "type": "image_carousel",
                            "columns": [
                                {
                                    "imageUrl": "https://example.com/image1.png",
                                    "action": {
                                    "type": "uri",
                                    "label": "Go",
                                    "uri": "https://example.com/"
                                    }
                                }
                            ]
                        }
                    }
                ],
                "notificationDisabled": false
            }
            """;

        // Verify
        assertThat(result.body()).isNotNull();
        verify(postRequestedFor(urlPathEqualTo("/v2/bot/message/broadcast"))
                .withRequestBody(equalToJson(expectedBody, true, true)));
    }

    @Test
    public void flexBoxLinearGradient() {
        stubFor(post(urlPathTemplate("/v2/bot/message/broadcast"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{}")));

        // Do
        FlexMessage flexMessage = new FlexMessage(
                "Test FlexBox with LinearGradient",
                new FlexBubble.Builder()
                        .body(
                                new FlexBox.Builder(
                                        FlexBox.Layout.VERTICAL,
                                        List.of(
                                                new FlexText.Builder()
                                                        .text("Hello World!")
                                                        .build()
                                        )
                                )
                                        .background(new FlexBoxLinearGradient.Builder()
                                                .angle("90deg")
                                                .startColor("#FF0000")
                                                .endColor("#0000FF")
                                                .build()
                                        )
                                        .build()
                        )
                        .build()
        );

        Result<Object> result = target.broadcast(
                null,
                new BroadcastRequest(List.of(flexMessage), false)
        ).join();

        String expectedBody = """
            {
              "messages": [
                {
                  "type": "flex",
                  "altText": "Test FlexBox with LinearGradient",
                  "contents": {
                    "type": "bubble",
                    "body": {
                      "type": "box",
                      "layout": "vertical",
                      "contents": [
                        {
                          "type": "text",
                          "text": "Hello World!"
                        }
                      ],
                      "background": {
                        "type": "linearGradient",
                        "angle": "90deg",
                        "startColor": "#FF0000",
                        "endColor": "#0000FF"
                      }
                    }
                  }
                }
              ],
              "notificationDisabled": false
            }
            """;
        // Verify
        assertThat(result.body()).isNotNull();
        verify(postRequestedFor(urlPathEqualTo("/v2/bot/message/broadcast"))
                .withRequestBody(equalToJson(expectedBody, true, true)));
    }

    @Test
    public void simpleFlexText() {
        stubFor(post(urlPathTemplate("/v2/bot/message/broadcast"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{}")));

        // Do
        FlexMessage flexMessage = new FlexMessage(
                "Test Alt Text",
                new FlexBubble.Builder()
                        .direction(FlexBubble.Direction.LTR)
                        .body(
                                new FlexBox.Builder(
                                        FlexBox.Layout.VERTICAL,
                                        List.of(
                                                new FlexText.Builder()
                                                        .text("Test Text")
                                                        .weight(FlexText.Weight.BOLD)
                                                        .build()
                                        )
                                ).build()
                        )
                        .build()
        );

        Result<Object> result = target.broadcast(
                null,
                new BroadcastRequest(List.of(flexMessage), false)
        ).join();

        String expectedBody = """
            {
              "messages": [
                {
                  "type": "flex",
                  "altText": "Test Alt Text",
                  "contents": {
                    "type": "bubble",
                    "direction": "ltr",
                    "body": {
                      "type": "box",
                      "layout": "vertical",
                      "contents": [
                        {
                          "type": "text",
                          "text": "Test Text",
                          "weight": "bold"
                        }
                      ]
                    }
                  }
                }
              ],
              "notificationDisabled": false
            }
            """;
        // Verify
        assertThat(result.body()).isNotNull();
        verify(postRequestedFor(urlPathEqualTo("/v2/bot/message/broadcast"))
                .withRequestBody(equalToJson(expectedBody, true, true)));
    }

    @Test
    public void doNotDropEmptyArrays() {
        stubFor(post(urlPathTemplate("/v2/bot/message/broadcast"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{}")));

        // Do
        FlexMessage flexMessage = new FlexMessage(
                "Test Empty Arrays",
                new FlexBubble.Builder()
                        .body(
                                new FlexBox.Builder(
                                        FlexBox.Layout.VERTICAL,
                                        List.of(
                                        )
                                )
                                        .build()
                        )
                        .build()
        );

        Result<Object> result = target.broadcast(
                null,
                new BroadcastRequest(List.of(flexMessage), false)
        ).join();

        String expectedBody = """
            {
              "messages": [
                {
                  "type": "flex",
                  "altText": "Test Empty Arrays",
                  "contents": {
                    "type": "bubble",
                    "body": {
                      "type": "box",
                      "layout": "vertical",
                      "contents": []
                    }
                  }
                }
              ],
              "notificationDisabled": false
            }
            """;
        // Verify
        assertThat(result.body()).isNotNull();
        verify(postRequestedFor(urlPathEqualTo("/v2/bot/message/broadcast"))
                .withRequestBody(equalToJson(expectedBody, true, true)));
    }

    @Test
    public void setPersonalRichMenu() {
        stubFor(post(urlPathTemplate("/v2/bot/user/{userId}/richmenu/{richMenuId}"))
                .willReturn(aResponse()
                        .withStatus(200)));

        final var userId = "U4af4980629...";
        final var richMenuId = "richmenu-1234abcd";
        // Do
        Result<Void> result = target.linkRichMenuIdToUser(userId, richMenuId)
                .join();

        // Verify
        assertThat(result.body()).isNull();

        verify(postRequestedFor(urlPathEqualTo("/v2/bot/user/U4af4980629.../richmenu/richmenu-1234abcd")));
    }

    @Test
    public void createCoupon() {
        stubFor(post(urlPathTemplate("/v2/bot/coupon"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{\n" +
                                "  \"couponId\": \"abc\"\n" +
                                "}")));
        final var request = new CouponCreateRequest.Builder(
                new LotteryAcquisitionConditionRequest(
                        50,
                        1
                ),
                1751619751L,
                1,
                1751619750L,
                "100Yen OFF",
                CouponCreateRequest.Visibility.PUBLIC,
                CouponCreateRequest.Timezone.ASIA_TOKYO
        )
                .reward(
                        new CouponDiscountRewardRequest(
                                new DiscountFixedPriceInfoRequest(100L)
                        )
                )
                .build();

        // Do
        Result<CouponCreateResponse> result = target.createCoupon(request)
                .join();

        String expectedBody = """
            {
              "title": "100Yen OFF",
              "startTimestamp": 1751619750,
              "endTimestamp": 1751619751,
              "visibility": "PUBLIC",
              "maxUseCountPerTicket": 1,
              "timezone": "ASIA_TOKYO",
              "reward": {
                "type": "discount",
                "priceInfo": {
                  "type": "fixed",
                  "fixedAmount": 100
                }
              },
              "acquisitionCondition": {
                "type": "lottery",
                "lotteryProbability": 50,
                "maxAcquireCount": 1
              }
            }
            """;

        // Verify
        assertThat(result.body()).isNotNull();
        assertThat(result.body().couponId()).isEqualTo("abc");
        verify(postRequestedFor(urlPathEqualTo("/v2/bot/coupon"))
                .withRequestBody(equalToJson(expectedBody, true, true)));
    }

    @Test
    public void closeCoupon() {
        stubFor(put(urlPathTemplate("/v2/bot/coupon/{couponId}/close"))
                .willReturn(aResponse()
                        .withStatus(200)));

        final var couponId = "abc";
        // Do
        Result<Void> result = target.closeCoupon(couponId)
                .join();

        // Verify
        assertThat(result.body()).isNull();

        verify(putRequestedFor(urlPathEqualTo("/v2/bot/coupon/abc/close")));
    }

    @Test
    public void getCouponDetail() {
        stubFor(get(urlPathTemplate("/v2/bot/coupon/{couponId}"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("""
                                {
                                  "couponId": "abc",
                                    "title": "100Yen OFF",
                                    "description": "Get 100Yen OFF",
                                    "imageUrl": "https://example.com/coupon.png",
                                    "barcodeImageUrl": "https://example.com/barcode.png",
                                    "couponCode": "1234567890",
                                    "startTimestamp": 1751619750,
                                    "endTimestamp": 1751619751,
                                    "visibility": "PUBLIC",
                                    "maxUseCountPerTicket": 1,
                                    "timezone": "ASIA_TOKYO",
                                    "acquisitionCondition": {
                                        "type": "lottery",
                                        "lotteryProbability": 50,
                                        "maxAcquireCount": 1
                                    },
                                    "usageCondition": "Minimum spend 1000Yen",
                                    "reward": {
                                        "type": "discount",
                                        "priceInfo": {
                                        "type": "fixed",
                                        "fixedAmount": 100
                                        }
                                    },
                                    "status": "RUNNING"
                                }
                                """)
                )
        );

        final var couponId = "abc";
        // Do
        Result<CouponResponse> result = target.getCouponDetail(couponId)
                .join();
        // Verify
        assertThat(result.body()).isNotNull();
        assertThat(result.body().couponId()).isEqualTo("abc");
        assertThat(result.body().title()).isEqualTo("100Yen OFF");
        assertThat(result.body().description()).isEqualTo("Get 100Yen OFF");
        assertThat(result.body().imageUrl().toString()).isEqualTo("https://example.com/coupon.png");
        assertThat(result.body().barcodeImageUrl().toString()).isEqualTo("https://example.com/barcode.png");
        assertThat(result.body().couponCode()).isEqualTo("1234567890");
        assertThat(result.body().startTimestamp()).isEqualTo(1751619750L);
        assertThat(result.body().endTimestamp()).isEqualTo(1751619751L);
        assertThat(result.body().visibility()).isEqualTo(CouponResponse.Visibility.PUBLIC);
        assertThat(result.body().maxUseCountPerTicket()).isEqualTo(1);
        assertThat(result.body().timezone()).isEqualTo(CouponResponse.Timezone.ASIA_TOKYO);
        assertThat(result.body().usageCondition()).isEqualTo("Minimum spend 1000Yen");
        assertThat(result.body().status()).isEqualTo(CouponResponse.Status.RUNNING);
        assertThat(result.body().acquisitionCondition())
                .isInstanceOf(LotteryAcquisitionConditionResponse.class);
        LotteryAcquisitionConditionResponse acqCond =
                (LotteryAcquisitionConditionResponse) result.body().acquisitionCondition();
        assertThat(acqCond.lotteryProbability()).isEqualTo(50);
        assertThat(acqCond.maxAcquireCount()).isEqualTo(1);
        assertThat(result.body().reward()).isInstanceOf(CouponDiscountRewardResponse.class);
        CouponDiscountRewardResponse reward = (CouponDiscountRewardResponse) result.body().reward();
        assertThat(reward.priceInfo()).isInstanceOf(DiscountFixedPriceInfoResponse.class);
        DiscountFixedPriceInfoResponse priceInfo =
                (DiscountFixedPriceInfoResponse) reward.priceInfo();
        assertThat(priceInfo.fixedAmount()).isEqualTo(100L);
        verify(getRequestedFor(urlPathEqualTo("/v2/bot/coupon/abc")));
    }
}
