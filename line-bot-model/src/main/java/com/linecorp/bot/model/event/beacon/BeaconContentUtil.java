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

final class BeaconContentUtil {
    private BeaconContentUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static final char[] HEX_CODE =
            { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * Parse hext presentation to byte array.
     *
     * @return byte array or null if input is null.
     *
     * @throws IllegalArgumentException occurred when arguments is not null and illegal hex string.
     */
    static byte[] parseBytesOrNull(final String deviceMessageAsHex) {
        if (deviceMessageAsHex == null) {
            return null;
        }

        final int length = deviceMessageAsHex.length();
        final int resultSize = length / 2;

        if (length % 2 != 0) {
            throw new IllegalArgumentException("hex string needs to be even-length: " + deviceMessageAsHex);
        }

        final byte[] bytes = new byte[resultSize];

        for (int pos = 0; pos < resultSize; pos++) {
            bytes[pos] = (byte) Integer.parseInt(deviceMessageAsHex.substring(pos * 2, pos * 2 + 2), 16);
            // Byte.parseByte doesn't support >= 0x80.
        }

        return bytes;
    }

    static String printHexBinary(final byte[] deviceMessage) {
        if (deviceMessage == null) {
            return null;
        }

        final StringBuilder sb = new StringBuilder(deviceMessage.length * 2);

        for (byte byteValue : deviceMessage) {
            final int intValue = Byte.toUnsignedInt(byteValue);
            sb.append(HEX_CODE[intValue >>> 4]);
            sb.append(HEX_CODE[intValue & 0xf]);
        }

        return sb.toString();
    }
}
