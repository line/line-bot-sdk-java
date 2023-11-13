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

package com.linecorp.bot.codegen.pebble;
import com.linecorp.bot.codegen.pebble.function.EndpointFunction;
import com.linecorp.bot.codegen.pebble.function.JavaTypeFunction;
import com.linecorp.bot.codegen.pebble.function.RequiredVarsFunction;
import io.pebbletemplates.pebble.extension.AbstractExtension;
import io.pebbletemplates.pebble.extension.Function;

import java.util.HashMap;
import java.util.Map;


public class MyPebbleExtension extends AbstractExtension {
    @Override
    public Map<String, Function> getFunctions() {
        HashMap<String, Function> map = new HashMap<>();
        map.put("endpoint", new EndpointFunction());
        map.put("javatype", new JavaTypeFunction());
        map.put("requiredVars", new RequiredVarsFunction());
        return map;
    }
}
