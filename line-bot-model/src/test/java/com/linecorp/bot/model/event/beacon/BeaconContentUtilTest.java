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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;

import org.junit.Test;

public class BeaconContentUtilTest {
    @Test
    public void parseBytesOrNullTestForValidValue() throws Exception {
        // Do
        byte[] bytes = BeaconContentUtil.parseBytesOrNull("0123");

        // Verify
        assertThat(bytes).containsExactly(0x01, 0x23);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseBytesOrNullTestForInvalidValue() throws Exception {
        // Do
        BeaconContentUtil.parseBytesOrNull("invalid");

        fail("Exception is not occurred");
    }

    @Test
    public void parseBytesOrNullTestForNull() throws Exception {
        // Do
        byte[] bytes = BeaconContentUtil.parseBytesOrNull(null);

        // Verify
        assertThat(bytes).isNull();
    }

    @Test
    public void printHexBinaryForActualValue() throws Exception {
        // Do
        String result = BeaconContentUtil.printHexBinary(new byte[] { 0x01, (byte) 0xab });

        // Verify
        assertThat(result).isEqualTo("01ab");
    }

    @Test
    public void printHexBinaryForNull() throws Exception {
        // Do
        String result = BeaconContentUtil.printHexBinary(null);

        // Verify
        assertThat(result).isNull();
    }
}
