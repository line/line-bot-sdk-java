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

package com.linecorp.bot.codegen;

import org.junit.Test;
import org.openapitools.codegen.ClientOptInput;
import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.config.CodegenConfigurator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/***
 * This test allows you to easily launch your code generation software under a debugger.
 * Then run this test under debug mode.  You will be able to step through your java code
 * and then see the results in the out directory.
 *
 * To experiment with debugging your code generator:
 * 1) Set a break point in PythonNextgenCustomClientGenerator.java in the postProcessOperationsWithModels() method.
 * 2) To launch this test in Eclipse: right-click | Debug As | JUnit Test
 *
 */
public class LineJavaCodegenGeneratorTest {

    @Test
    public void shop() throws IOException {
        this.generate("shop");
    }

    @Test
    public void channel() throws IOException {
        this.generate("channel-access-token");
    }

    private void generate(String target) throws IOException {
        Path outPath = Paths.get("out/" + target);
        if (outPath.toFile().exists()) {
            try (Stream<Path> stream = Files.walk(outPath)) {
                //noinspection ResultOfMethodCallIgnored
                stream.map(Path::toFile)
                        .forEach(File::delete);
            }
        }

        // to understand how the 'openapi-generator-cli' module is using 'CodegenConfigurator', have a look at the 'Generate' class:
        // https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator-cli/src/main/java/org/openapitools/codegen/cmd/Generate.java
        final CodegenConfigurator configurator = new CodegenConfigurator()
                .addGlobalProperty("modelDocs", "false")
                .addGlobalProperty("apiDocs", "false")
                .addGlobalProperty("apiTests", "false")
                .setTemplatingEngineName("pebble")
                .setTemplateDir("src/main/resources/line-java-codegen")
                .setGeneratorName("line-java-codegen") // use this codegen library
                .setInputSpec("../line-openapi/" + target + ".yml") // sample OpenAPI file
                .setOutputDir("out/" + target); // output directory

        final ClientOptInput clientOptInput = configurator.toClientOptInput();
        DefaultGenerator generator = new DefaultGenerator();
        generator.opts(clientOptInput).generate();
    }
}
