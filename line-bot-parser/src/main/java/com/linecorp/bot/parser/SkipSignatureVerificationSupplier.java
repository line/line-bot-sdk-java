/*
 * Copyright 2025 LINE Corporation
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

package com.linecorp.bot.parser;

import java.util.function.BooleanSupplier;

/**
 * Special {@link BooleanSupplier} for Skip Signature Verification.
 *
 * <p>You can implement it to return whether to skip signature verification.
 *
 * <p>If true is returned, webhook signature verification is skipped.
 * This may be helpful when you update the channel secret and want to skip the verification temporarily.
 */
@FunctionalInterface
public interface SkipSignatureVerificationSupplier extends BooleanSupplier {
}
