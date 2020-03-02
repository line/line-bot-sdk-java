/*
 * Copyright 2019 LINE Corporation
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

package com.linecorp.bot.model;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.message.Message;

import lombok.Getter;
import lombok.Value;

/**
 * Sends a push message to multiple users. You can specify recipients using attributes (such as age, gender, OS,
 * and region) or by retargeting (audiences). Messages cannot be sent to groups or rooms.
 */
@Value
public class Narrowcast {

    /**
     * List of Message objects.
     *
     * <p>Max: 5
     */
    private final List<Message> messages;

    /**
     * Recipient objects represent audiences. You can specify recipients based on a combination of criteria
     * using logical operator objects.
     */
    private final Recipient recipient;

    /**
     * Demographic filter object. You can use friends' attributes to filter the list of recipients.
     * If this is omitted, messages will be sent to everyone – including users with attribute values of
     * "unknown".
     */
    private final Filter filter;

    /**
     * The maximum number of narrowcast messages to send. Use this parameter to limit the number of narrowcast
     * messages sent. The recipients will be chosen at random.
     */
    private final Limit limit;

    /**
     * Whether sends a push notification to message receivers or not. If {@literal true}, the user doesn't
     * receive a push notification when the message is sent. And if {@literal false}, the user receives a push
     * notification when the message is sent (unless they have disabled push notifications in LINE and/or their
     * device).
     */
    private final boolean notificationDisabled;

    public interface Recipient {
        String getType();
    }

    @Value
    public static class AudienceRecipient implements Recipient {
        private final String audienceGroupId;

        @Override
        public String getType() {
            return "audience";
        }
    }

    /**
     * Use logical AND, OR, and NOT operators to combine multiple recipient objects together. You can specify
     * up to 10 recipient objects per request.
     */
    @Value
    public static class LogicalOperatorRecipient implements Recipient {
        private List<Recipient> and;
        private List<Recipient> or;
        private Recipient not;

        @Override
        public String getType() {
            return "operator";
        }
    }

    @Value
    public static final class Filter {
        private final DemographicFilter demographic;

        public Filter(@JsonProperty("demographic") DemographicFilter demographic) {
            this.demographic = demographic;
        }
    }

    @JsonTypeInfo(use = Id.NAME, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(GenderDemographicFilter.class),
            @JsonSubTypes.Type(AgeDemographicFilter.class),
            @JsonSubTypes.Type(AppTypeDemographicFilter.class),
            @JsonSubTypes.Type(AreaDemographicFilter.class),
            @JsonSubTypes.Type(SubscriptionPeriodDemographicFilter.class),
            @JsonSubTypes.Type(OperatorDemographicFilter.class)
    })
    public interface DemographicFilter {
    }

    @JsonTypeName(GenderDemographicFilter.type)
    @Value
    public static class GenderDemographicFilter implements DemographicFilter {
        private static final String type = "gender";

        private final List<Gender> oneOf;

        public GenderDemographicFilter(Gender gender) {
            this(Collections.singletonList(gender));
        }

        @JsonCreator
        public GenderDemographicFilter(List<Gender> oneOf) {
            this.oneOf = oneOf;
        }

        public enum Gender {
            @JsonProperty("male")
            MALE,
            @JsonProperty("female")
            FEMALE
        }
    }

    @Value
    @JsonTypeName(AgeDemographicFilter.type)
    public static class AgeDemographicFilter implements DemographicFilter {
        private static final String type = "age";

        private final Age gte;
        private final Age lt;

        @JsonCreator
        public AgeDemographicFilter(Age gte, Age lt) {
            this.gte = gte;
            this.lt = lt;
        }

        public enum Age {
            @JsonProperty("age_15")
            AGE_15(15),
            @JsonProperty("age_20")
            AGE_20(20),
            @JsonProperty("age_25")
            AGE_25(25),
            @JsonProperty("age_30")
            AGE_30(30),
            @JsonProperty("age_35")
            AGE_35(35),
            @JsonProperty("age_40")
            AGE_40(40),
            @JsonProperty("age_45")
            AGE_45(45),
            @JsonProperty("age_50")
            AGE_50(50);

            @Getter
            private final int age;

            Age(int age) {
                this.age = age;
            }
        }
    }

    @Value
    @JsonTypeName(AppTypeDemographicFilter.type)
    public static class AppTypeDemographicFilter implements DemographicFilter {
        private static final String type = "appType";

        private final List<AppType> oneOf;

        @JsonCreator
        public AppTypeDemographicFilter(List<AppType> oneOf) {
            this.oneOf = oneOf;
        }

        public AppTypeDemographicFilter(AppType appType) {
            this(Collections.singletonList(appType));
        }

        public enum AppType {
            @JsonProperty("ios")
            IOS("ios"),
            @JsonProperty("android")
            ANDROID("android");

            @Getter
            private final String appType;

            AppType(String appType) {
                this.appType = appType;
            }
        }
    }

    /**
     * Send messages to users in the specified region.
     */
    @Value
    @JsonTypeName(AreaDemographicFilter.type)
    public static class AreaDemographicFilter implements DemographicFilter {
        private static final String type = "area";

        private final List<AreaCode> oneOf;

        @JsonCreator
        public AreaDemographicFilter(List<AreaCode> oneOf) {
            this.oneOf = oneOf;
        }

        public AreaDemographicFilter(AreaCode areaCode) {
            this(Collections.singletonList(areaCode));
        }

        public enum AreaCode {
            @JsonProperty("jp_01")
            JP_HOKKAIDO("jp_01"), // 北海道
            @JsonProperty("jp_02")
            JP_AOMORI("jp_02"), // 青森県
            @JsonProperty("jp_03")
            JP_IWATE("jp_03"), // 岩手県
            @JsonProperty("jp_04")
            JP_MIYAGI("jp_04"), // 宮城県
            @JsonProperty("jp_05")
            JP_AKITA("jp_05"), // 秋田県
            @JsonProperty("jp_06")
            JP_YAMAGATA("jp_06"), // 山形県
            @JsonProperty("jp_07")
            JP_FUKUSHIMA("jp_07"), // 福島県
            @JsonProperty("jp_08")
            JP_IBARAKI("jp_08"), // 茨城県
            @JsonProperty("jp_09")
            JP_TOCHIGI("jp_09"), // 栃木県
            @JsonProperty("jp_10")
            JP_GUNMA("jp_10"), // 群馬県
            @JsonProperty("jp_11")
            JP_SAITAMA("jp_11"), // 埼玉県
            @JsonProperty("jp_12")
            JP_CHIBA("jp_12"), // 千葉県
            @JsonProperty("jp_13")
            JP_TOKYO("jp_13"), // 東京都
            @JsonProperty("jp_14")
            JP_KANAGAWA("jp_14"), // 神奈川県
            @JsonProperty("jp_15")
            JP_NIIGATA("jp_15"), // 新潟県
            @JsonProperty("jp_16")
            JP_TOYAMA("jp_16"), // 富山県
            @JsonProperty("jp_17")
            JP_ISHIKAWA("jp_17"), // 石川県
            @JsonProperty("jp_18")
            JP_FUKUI("jp_18"), // 福井県
            @JsonProperty("jp_19")
            JP_YAMANASHI("jp_19"), // 山梨県
            @JsonProperty("jp_20")
            JP_NAGANO("jp_20"), // 長野県
            @JsonProperty("jp_21")
            JP_GIFU("jp_21"), // 岐阜県
            @JsonProperty("jp_22")
            JP_SHIZUOKA("jp_22"), // 静岡県
            @JsonProperty("jp_23")
            JP_AICHI("jp_23"), // 愛知県
            @JsonProperty("jp_24")
            JP_MIE("jp_24"), // 三重県
            @JsonProperty("jp_25")
            JP_SHIGA("jp_25"), // 滋賀県
            @JsonProperty("jp_26")
            JP_KYOTO("jp_26"), // 京都府
            @JsonProperty("jp_27")
            JP_OSAKA("jp_27"), // 大阪府
            @JsonProperty("jp_28")
            JP_HYOUGO("jp_28"), // 兵庫県
            @JsonProperty("jp_29")
            JP_NARA("jp_29"), // 奈良県
            @JsonProperty("jp_30")
            JP_WAKAYAMA("jp_30"), // 和歌山県
            @JsonProperty("jp_31")
            JP_TOTTORI("jp_31"), // 鳥取県
            @JsonProperty("jp_32")
            JP_SHIMANE("jp_32"), // 島根県
            @JsonProperty("jp_33")
            JP_OKAYAMA("jp_33"), // 岡山県
            @JsonProperty("jp_34")
            JP_HIROSHIMA("jp_34"), // 広島県
            @JsonProperty("jp_35")
            JP_YAMAGUCHI("jp_35"), // 山口県
            @JsonProperty("jp_36")
            JP_TOKUSHIMA("jp_36"), // 徳島県
            @JsonProperty("jp_37")
            JP_KAGAWA("jp_37"), // 香川県
            @JsonProperty("jp_38")
            JP_EHIME("jp_38"), // 愛媛県
            @JsonProperty("jp_39")
            JP_KOUCHI("jp_39"), // 高知県
            @JsonProperty("jp_40")
            JP_FUKUOKA("jp_40"), // 福岡県
            @JsonProperty("jp_41")
            JP_SAGA("jp_41"), // 佐賀県
            @JsonProperty("jp_42")
            JP_NAGASAKI("jp_42"), // 長崎県
            @JsonProperty("jp_43")
            JP_KUMAMOTO("jp_43"), // 熊本県
            @JsonProperty("jp_44")
            JP_OITA("jp_44"), // 大分県
            @JsonProperty("jp_45")
            JP_MIYAZAKI("jp_45"), // 宮崎県
            @JsonProperty("jp_46")
            JP_KAGOSHIMA("jp_46"), // 鹿児島県
            @JsonProperty("jp_47")
            JP_OKINAWA("jp_47"), // 沖縄県
            @JsonProperty("tw_01")
            TW_TAIPEI_CITY("tw_01"), // 台北市
            @JsonProperty("tw_02")
            TW_NEW_TAIPEI_CITY("tw_02"), // 新北市
            @JsonProperty("tw_03")
            TW_TAOYUAN_CITY("tw_03"), // 桃園市
            @JsonProperty("tw_04")
            TW_TAICHUNG_CITY("tw_04"), // 台中市
            @JsonProperty("tw_05")
            TW_TAINAN_CITY("tw_05"), // 台南市
            @JsonProperty("tw_06")
            TW_KAOHSIUNG_CITY("tw_06"), // 高雄市
            @JsonProperty("tw_07")
            TW_KEELUNG_CITY("tw_07"), // 基隆市
            @JsonProperty("tw_08")
            TW_HSINCHU_CITY("tw_08"), // 新竹市
            @JsonProperty("tw_09")
            TW_CHIAYI_CITY("tw_09"), // 嘉義市
            @JsonProperty("tw_10")
            TW_HSINCHU_COUNTY("tw_10"), // 新竹県
            @JsonProperty("tw_11")
            TW_MIAOLI_COUNTY("tw_11"), // 苗栗県
            @JsonProperty("tw_12")
            TW_CHANGHUA_COUNTY("tw_12"), // 彰化県
            @JsonProperty("tw_13")
            TW_NANTOU_COUNTY("tw_13"), // 南投県
            @JsonProperty("tw_14")
            TW_YUNLIN_COUNTY("tw_14"), // 雲林県
            @JsonProperty("tw_15")
            TW_CHIAYI_COUNTY("tw_15"), // 嘉義県
            @JsonProperty("tw_16")
            TW_PINGTUNG_COUNTY("tw_16"), // 屏東県
            @JsonProperty("tw_17")
            TW_YILAN_COUNTY("tw_17"), // 宜蘭県
            @JsonProperty("tw_18")
            TW_HUALIEN_COUNTY("tw_18"), // 花蓮県
            @JsonProperty("tw_19")
            TW_TAITUNG_COUNTY("tw_19"), // 台東県
            @JsonProperty("tw_20")
            TW_PENGHU_COUNTY("tw_20"), // 澎湖県
            @JsonProperty("tw_21")
            TW_KINMEN_COUNTY("tw_21"), // 金門県
            @JsonProperty("tw_22")
            TW_LIENCHIANG_COUNTY("tw_22"), // 連江県
            @JsonProperty("th_01")
            TH_BANGKOK("th_01"), // バンコク
            @JsonProperty("th_02")
            TH_PATTAYA("th_02"), // パタヤ
            @JsonProperty("th_03")
            TH_NORTHERN("th_03"), // 北部
            @JsonProperty("th_04")
            TH_CENTRAL("th_04"), // 中央部
            @JsonProperty("th_05")
            TH_SOUTHERN("th_05"), // 南部
            @JsonProperty("th_06")
            TH_EASTERN("th_06"), // 東部
            @JsonProperty("th_07")
            TH_NORTHEASTERN("th_07"), // 東北部
            @JsonProperty("th_08")
            TH_WESTERN("th_08"), // 西部
            @JsonProperty("id_01")
            ID_BALI("id_01"), // バリ
            @JsonProperty("id_02")
            ID_BANDUNG("id_02"), // バンドン
            @JsonProperty("id_03")
            ID_BANJARMASIN("id_03"), // バンジャルマシン
            @JsonProperty("id_04")
            ID_JABODETABEK("id_04"), // ジャボデタベック（ジャカルタ首都圏）
            @JsonProperty("id_06")
            ID_MAKASSAR("id_06"), // マカッサル
            @JsonProperty("id_07")
            ID_MEDAN("id_07"), // メダン
            @JsonProperty("id_08")
            ID_PALEMBANG("id_08"), // パレンバン
            @JsonProperty("id_09")
            ID_SAMARINDA("id_09"), // サマリンダ
            @JsonProperty("id_10")
            ID_SEMARANG("id_10"), // スマラン
            @JsonProperty("id_11")
            ID_SURABAYA("id_11"), // スラバヤ
            @JsonProperty("id_12")
            ID_YOGYAKARTA("id_12"), // ジョグジャカルタ
            @JsonProperty("id_05")
            ID_LAINNYA("id_05"); // その他のエリア

            private final String code;

            AreaCode(String code) {
                this.code = code;
            }

            public String getCode() {
                return code;
            }
        }
    }

    @Value
    @JsonTypeName(SubscriptionPeriodDemographicFilter.type)
    public static class SubscriptionPeriodDemographicFilter implements DemographicFilter {
        private static final String type = "subscriptionPeriod";

        private final SubscriptionPeriod gte;
        private final SubscriptionPeriod lt;

        public SubscriptionPeriodDemographicFilter(SubscriptionPeriod gte, SubscriptionPeriod lt) {
            this.gte = gte;
            this.lt = lt;
        }

        public enum SubscriptionPeriod {
            @JsonProperty("day_7")
            DAY_7(7),
            @JsonProperty("day_30")
            DAY_30(30),
            @JsonProperty("day_90")
            DAY_90(90),
            @JsonProperty("day_180")
            DAY_180(180),
            @JsonProperty("day_365")
            DAY_365(365);

            private final int days;

            SubscriptionPeriod(int days) {
                this.days = days;
            }

            public int getDays() {
                return days;
            }
        }
    }

    @Value
    public static class OperatorDemographicFilter implements DemographicFilter {
        private final List<DemographicFilter> and;
        private final List<DemographicFilter> or;
        private final DemographicFilter not;
    }

    @Value
    public static class Limit {
        /**
         * The maximum number of narrowcast messages to send. Use this parameter to limit the number of
         * narrowcast messages sent. The recipients will be chosen at random.
         */
        private final Integer max;
    }

    public Narrowcast(Message message, Filter filter) {
        this(Collections.singletonList(message), null, filter, null, false);
    }

    @JsonCreator
    public Narrowcast(
            @JsonProperty("messages") List<Message> messages,
            @JsonProperty("recipient") Recipient recipient,
            @JsonProperty("filter") Filter filter,
            @JsonProperty("limit") Limit limit,
            @JsonProperty("notificationDisabled") boolean notificationDisabled) {
        this.messages = messages;
        this.recipient = recipient;
        this.filter = filter;
        this.limit = limit;
        this.notificationDisabled = notificationDisabled;
    }
}
