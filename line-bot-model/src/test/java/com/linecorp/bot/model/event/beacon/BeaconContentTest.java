/*
 * Copyright 2017 LINE Corporation
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

package com.linecorp.bot.model.event.beacon;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class BeaconContentTest {
    @Test
    public void beaconContentEqualityTest() {
        BeaconContent upperCase = new BeaconContent("hwid", "enter", "01ab");
        BeaconContent lowerCase = new BeaconContent("hwid", "enter", "01AB");

        // Verify
        assertThat(upperCase).isEqualTo(lowerCase);
    }

    @Test
    public void beaconContentArrayProtectedTest() {
        BeaconContent target = new BeaconContent("hwid", "enter", "01");

        // Precondition
        assertThat(target.getDeviceMessage())
                .hasSize(1)
                .containsExactly(0x01);

        // Do
        target.getDeviceMessage()[0] = (byte) 0xcd;

        // Verify
        assertThat(target.getDeviceMessage()[0])
                .isEqualTo((byte) 0x01)
                .isNotEqualTo((byte) 0xcd);
    }
}
