/*
 * Copyright 2023 LINE Corporation
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

package com.example.bot.spring;

import java.util.function.Supplier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.messaging.model.FlexMessage;

public class ExampleFlexMessageSupplier implements Supplier<FlexMessage> {
    @Override
    public FlexMessage get() {
        String json = """
                {
                  "type" : "flex",
                  "altText" : "ALT",
                  "contents" : {
                    "type" : "bubble",
                    "hero" : {
                      "type" : "image",
                      "url" : "https://example.com/cafe.jpg",
                      "size" : "full",
                      "aspectRatio" : "20:13",
                      "aspectMode" : "cover",
                      "action" : {
                        "type" : "uri",
                        "label" : "label",
                        "uri" : "http://example.com"
                      }
                    },
                    "body" : {
                      "type" : "box",
                      "layout" : "vertical",
                      "contents" : [ {
                        "type" : "text",
                        "text" : "Brown Cafe",
                        "size" : "xl",
                        "weight" : "bold"
                      }, {
                        "type" : "box",
                        "layout" : "baseline",
                        "contents" : [ {
                          "type" : "icon",
                          "url" : "https://example.com/gold_star.png",
                          "size" : "sm"
                        }, {
                          "type" : "icon",
                          "url" : "https://example.com/gold_star.png",
                          "size" : "sm"
                        }, {
                          "type" : "icon",
                          "url" : "https://example.com/gold_star.png",
                          "size" : "sm"
                        }, {
                          "type" : "icon",
                          "url" : "https://example.com/gold_star.png",
                          "size" : "sm"
                        }, {
                          "type" : "icon",
                          "url" : "https://example.com/gray_star.png",
                          "size" : "sm"
                        }, {
                          "type" : "text",
                          "flex" : 0,
                          "text" : "4.0",
                          "size" : "sm",
                          "color" : "#999999",
                          "margin" : "md"
                        } ],
                        "margin" : "md"
                      }, {
                        "type" : "box",
                        "layout" : "vertical",
                        "contents" : [ {
                          "type" : "box",
                          "layout" : "baseline",
                          "contents" : [ {
                            "type" : "text",
                            "flex" : 1,
                            "text" : "Place",
                            "size" : "sm",
                            "color" : "#aaaaaa"
                          }, {
                            "type" : "text",
                            "flex" : 5,
                            "text" : "Shinjuku, Tokyo",
                            "size" : "sm",
                            "color" : "#666666",
                            "wrap" : true
                          } ],
                          "spacing" : "sm"
                        }, {
                          "type" : "box",
                          "layout" : "baseline",
                          "contents" : [ {
                            "type" : "text",
                            "flex" : 1,
                            "text" : "Time",
                            "size" : "sm",
                            "color" : "#aaaaaa"
                          }, {
                            "type" : "text",
                            "flex" : 5,
                            "text" : "10:00 - 23:00",
                            "size" : "sm",
                            "color" : "#666666",
                            "wrap" : true
                          } ],
                          "spacing" : "sm"
                        } ],
                        "spacing" : "sm",
                        "margin" : "lg"
                      } ]
                    },
                    "footer" : {
                      "type" : "box",
                      "layout" : "vertical",
                      "contents" : [ {
                        "type" : "spacer",
                        "size" : "sm"
                      }, {
                        "type" : "button",
                        "style" : "link",
                        "action" : {
                          "type" : "uri",
                          "label" : "CALL",
                          "uri" : "tel:000000"
                        },
                        "height" : "sm"
                      }, {
                        "type" : "separator"
                      }, {
                        "type" : "button",
                        "style" : "link",
                        "action" : {
                          "type" : "uri",
                          "label" : "WEBSITE",
                          "uri" : "https://example.com"
                        },
                        "height" : "sm"
                      } ],
                      "spacing" : "sm"
                    }
                  }
                }
                """;

        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(json, FlexMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
