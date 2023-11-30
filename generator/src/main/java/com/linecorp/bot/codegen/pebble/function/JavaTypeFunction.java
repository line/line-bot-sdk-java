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

package com.linecorp.bot.codegen.pebble.function;


import io.pebbletemplates.pebble.extension.Function;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import org.openapitools.codegen.CodegenDiscriminator;
import org.openapitools.codegen.CodegenModel;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class JavaTypeFunction implements Function {
    @Override
    public List<String> getArgumentNames() {
        return Collections.singletonList("model");
    }

    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
        CodegenModel model = (CodegenModel) args.get("model");
        return mappingName(model.name, model.parentModel.getDiscriminator());
    }

    private String mappingName(String modelName, CodegenDiscriminator discriminator) {
        String valueToSearch = "#/components/schemas/" + modelName;
        return discriminator.getMapping().entrySet().stream()
                .filter(entry -> valueToSearch.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Key not found (" + modelName + ") mapping (" + discriminator.getMapping() + ")"));
    }
}
