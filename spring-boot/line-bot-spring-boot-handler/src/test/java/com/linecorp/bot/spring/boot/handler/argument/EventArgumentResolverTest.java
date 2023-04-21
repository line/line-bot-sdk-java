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

package com.linecorp.bot.spring.boot.handler.argument;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

import com.linecorp.bot.webhook.model.BeaconEvent;
import com.linecorp.bot.webhook.model.Event;
import com.linecorp.bot.webhook.model.MessageEvent;

class EventArgumentResolverTest {
    BeaconEvent beaconEvent = new BeaconEvent(
            null, null,
            null, null,
            null, null,
            null
    );

    @Test
    void isSupported() {
        assertThat(new EventArgumentResolver(Event.class).isSupported(beaconEvent))
                .isTrue();
        assertThat(new EventArgumentResolver(MessageEvent.class).isSupported(beaconEvent))
                .isFalse();
        assertThat(new EventArgumentResolver(BeaconEvent.class).isSupported(beaconEvent))
                .isTrue();
    }

    @Test
    void resolve() {
        assertThat(new EventArgumentResolver(BeaconEvent.class).resolve(null, beaconEvent))
                .isEqualTo(beaconEvent);
    }
}
