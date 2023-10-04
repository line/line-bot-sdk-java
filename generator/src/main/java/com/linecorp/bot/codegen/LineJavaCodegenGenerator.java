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

import static org.openapitools.codegen.utils.StringUtils.camelize;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openapitools.codegen.CodegenDiscriminator;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.CodegenType;
import org.openapitools.codegen.languages.AbstractJavaCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;

import com.google.common.collect.ImmutableMap;
import com.samskivert.mustache.Mustache;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.servers.Server;

public class LineJavaCodegenGenerator extends AbstractJavaCodegen {
    public LineJavaCodegenGenerator() {
        super();
        embeddedTemplateDir = "line-java-codegen";
    }

    public CodegenType getTag() {
        return CodegenType.OTHER;
    }

    public String getName() {
        return "line-java-codegen";
    }


    /**
     * ApiModelProperty and ApiModels are not required for this product.
     */
    @Override
    public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
        super.postProcessModelProperty(model, property);
        model.imports.remove("ApiModelProperty");
        model.imports.remove("ApiModel");
    }

    @Override
    public CodegenModel fromModel(String name, Schema model) {
        CodegenModel codegenModel = super.fromModel(name, model);
        codegenModel.imports.remove("ApiModel");
        codegenModel.imports.remove("ApiModelProperty");
        return codegenModel;
    }

    public String getHelp() {
        return "Generates a line-java-codegen client library.";
    }

    /**
     * Tweaks for class name.
     */
    @Override
    public String toApiName(String name) {
        if (name.equals("LineOauth")) {
            // Oauth looks strange. Adjust the name.
            return super.toApiName("LineOAuth");
        } else {
            return super.toApiName(name);
        }
    }

    // Do not generate /.*AllOf.java./
    @Override
    public boolean getUseInlineModelResolver() {
        return false;
    }

    @Override
    public ModelsMap postProcessModels(ModelsMap objs) {
        ModelsMap modelsMap = super.postProcessModels(objs);
        for (ModelMap model : modelsMap.getModels()) {
            CodegenModel codegenModel = model.getModel();

            // remove the `type` field from child classes due to remove duplicated type field with `@JsonTypeName`.
            if (codegenModel.parent != null) {
                codegenModel.setAllVars(codegenModel.getAllVars().stream()
                        .filter(codegenProperty -> !codegenProperty.name.equals("type"))
                        .collect(Collectors.toList()));
            }

            // if the class have a parent, set ` implements ${parent}`.
            // and put @JsonTypeName annotation.
            if (codegenModel.parent != null) {
                addImplements(codegenModel, codegenModel.parent);
            }

            // Implements ReplyEvent if the class has "replyToken" field.
            if (codegenModel.parent != null && codegenModel.parent.equals("Event")) {
                if (codegenModel.vars.stream().anyMatch(codegenProperty -> codegenProperty.name.equals("replyToken"))) {
                    addImplements(codegenModel, "ReplyEvent");
                }
            }

            // fill src/main/resources/body/*.java to the body of the class.
            String body = readPartialBody("body/model/" + codegenModel.name + ".java");
            codegenModel.vendorExtensions.put("x-body", body);
        }

        return modelsMap;
    }

    private String readPartialBody(String path) {
        // fill src/main/resources/body/*.java to the body of the class.
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream(path)) {
            if (inputStream != null) {
                System.out.println("Partial body file found: " + path);
                String src = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                return indent4(src);
            }
        } catch (IOException e) {
            // do nothing.
        }
        System.out.println("Partial body file NOT found: " + path);
        return null;
    }

    @Override
    public Map<String, ModelsMap> postProcessAllModels(Map<String, ModelsMap> objs) {
        final Map<String, ModelsMap> processed = super.postProcessAllModels(objs);

        for (Map.Entry<String, ModelsMap> entry : processed.entrySet()) {
            entry.setValue(postProcessModelsMap(entry.getValue()));
        }

        return processed;
    }

    private ModelsMap postProcessModelsMap(ModelsMap objs) {
        for (ModelMap m : objs.getModels()) {
            CodegenModel codegenModel = m.getModel();
            if (codegenModel.parentModel != null) {
                String name = mappingName(codegenModel.name, codegenModel.parentModel.getDiscriminator());
                codegenModel.getVendorExtensions().put("x-javatypename", name);
            }
        }

        return objs;
    }

    private String mappingName(String modelName, CodegenDiscriminator discriminator) {
        String valueToSearch = "#/components/schemas/" + modelName;
        return discriminator.getMapping().entrySet().stream()
                .filter(entry -> valueToSearch.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Key not found (" + modelName + ") mapping (" + discriminator.getMapping() + ")"));
    }

    private String indent4(String s) {
        String[] split = s.split("\n");
        return Stream.of(split).map(s1 -> "    " + s1).collect(Collectors.joining("\n"));
    }

    @SuppressWarnings("unchecked")
    private static void addImplements(CodegenModel codegenModel, String additional) {
        List<String> p = (List<String>) codegenModel.getVendorExtensions().getOrDefault("x-implements", new ArrayList<>());
        p.add(additional);
        codegenModel.getVendorExtensions().put("x-implements", p);
    }

    // AbstractJavaCodegen adds "Enum" suffix.
    // We don't need it... So, remove the suffix.
    @Override
    public String toEnumName(CodegenProperty property) {
        return sanitizeName(camelize(property.name));
    }

    // Sort parameters...
    // I want to put header parameters to the head of the list.
    @Override
    public CodegenOperation fromOperation(String path, String httpMethod, Operation operation, List<Server> servers) {
        CodegenOperation op = super.fromOperation(path, httpMethod, operation, servers);

        Stream<CodegenParameter> headerParams = op.allParams.stream().filter(codegenParameter -> codegenParameter.isHeaderParam && !codegenParameter.isFile);
        Stream<CodegenParameter> nonHeaderParams = op.allParams.stream().filter(codegenParameter -> !codegenParameter.isHeaderParam && !codegenParameter.isFile);
        Stream<CodegenParameter> fileParams = op.allParams.stream().filter(codegenParameter -> codegenParameter.isFile);
        op.allParams = Stream.of(headerParams, nonHeaderParams, fileParams)
                .reduce(Stream::concat)
                .orElseGet(Stream::empty)
                .collect(Collectors.toList());

        return op;
    }

    @Override
    protected ImmutableMap.Builder<String, Mustache.Lambda> addMustacheLambdas() {
        return super.addMustacheLambdas()
                .put("endpoint", (fragment, writer) -> {
                    String text = fragment.execute();
                    writer.write(this.getEndpointFromClassName(text));
                })
                .put("exceptionbuilderclassname", (fragment, writer) -> {
                    String text = fragment.execute();
                    writer.write(
                            text.replace("BlobClient", "")
                                    .replace("Client", "")
                                    + "ExceptionBuilder"
                    );
                })
                .put("importTextMessage", ((fragment, writer) -> {
                    // TODO: I know. this is silly hack. But it works.
                    String text = fragment.execute();
                    if (text.contains("MessagingApi")) {
                        writer.write("import com.linecorp.bot.messaging.model.TextMessage;");
                    }
                }))
                .put("messageMock", ((fragment, writer) -> {
                    // TODO: Ditto.
                    String text = fragment.execute();
                    if (text.contains("MessagingApi")) {
                        String src = ", Map.of(\"message\", () -> new TextMessage(\"hello\"), \"recipient\", () -> null, \"filter\", () -> null)";
                        writer.write(src);
                    }
                }))
                .put("injectbody", (fragment, writer) -> {
                    String text = fragment.execute();
                    String body = readPartialBody("body/api/" + text + ".java");
                    if (body != null) {
                        writer.write(body);
                    }
                });
    }

    private String getEndpointFromClassName(String className) {
        if (className.equals("LineModuleAttachClient")) {
            return "https://manager.line.biz";
        } else if (className.contains("Blob")) {
            return "https://api-data.line.me";
        } else {
            return "https://api.line.me";
        }
    }
}
