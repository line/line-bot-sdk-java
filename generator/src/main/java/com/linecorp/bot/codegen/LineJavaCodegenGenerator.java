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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
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
        apiTemplateFiles.remove("api.mustache");
        apiTemplateFiles.put("line-java-codegen/api.pebble", ".java");
        apiTestTemplateFiles.remove("api_test.mustache");
        apiTestTemplateFiles.put("line-java-codegen/api_test.pebble", ".java");
        modelTemplateFiles.remove("model.mustache");
        modelTemplateFiles.put("line-java-codegen/model.pebble", ".java");
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
        }

        return modelsMap;
    }

    @Override
    public Map<String, ModelsMap> postProcessAllModels(Map<String, ModelsMap> objs) {
        super.postProcessAllModels(objs);

        Map<String, ModelsMap> additionalModels = new HashMap<>();

        // List of interfaces for which an Unknown class should not be generated
        List<String> excluded_interfaces = List.of(
                "Event", "MessageContent", "Message", "Action", "DemographicFilter",
                "FlexBoxBackground", "FlexComponent", "FlexContainer", "ImagemapAction",
                "Mentionee", "ModuleContent", "Recipient", "RichMenuBatchOperation",
                "Source", "Template", "ThingsContent"
        );

        // Set additional unknown* class for jackson's defaultImpl to have unknwon* class as fallback
        for (ModelsMap modelsAttrs : objs.values()) {
            String packageName = modelsAttrs.containsKey("packageName")
                    ? modelsAttrs.get("packageName").toString()
                    : null;
            String _package = modelsAttrs.containsKey("package")
                    ? modelsAttrs.get("package").toString()
                    : packageName;

            for (ModelMap mo : modelsAttrs.getModels()) {
                CodegenModel codegenModel = mo.getModel();
                if (codegenModel.discriminator != null) {
                    String baseName = codegenModel.classname;

                    // skip for excluded interfaces
                    if (excluded_interfaces.contains(baseName)) {
                        continue;
                    }

                    String fallbackModelName = "Unknown" + baseName;
                    CodegenModel fallbackModel = new CodegenModel();
                    fallbackModel.name = fallbackModelName;
                    fallbackModel.schemaName = fallbackModelName;
                    fallbackModel.parent = baseName;
                    fallbackModel.interfaces = List.of(baseName);
                    fallbackModel.allOf = Set.of(baseName);
                    fallbackModel.classname = fallbackModelName;
                    fallbackModel.classVarName = camelize(fallbackModelName);
                    fallbackModel.dataType = baseName;
                    fallbackModel.classFilename = fallbackModelName;
                    fallbackModel.imports = Set.of(baseName);
                    fallbackModel.setIsAnyType(true);
                    addImplements(fallbackModel, baseName);
                    ModelMap fallbackModelMap = new ModelMap();
                    fallbackModelMap.setModel(fallbackModel);

                    ModelsMap fallbackModelsMap = new ModelsMap();
                    fallbackModelsMap.setModels(List.of(fallbackModelMap));
                    fallbackModelsMap.setImports(List.of(Map.of("import", _package + "." + baseName)));
                    fallbackModelsMap.put("packageName", packageName);
                    fallbackModelsMap.put("package", _package);
                    fallbackModelsMap.put("classname", fallbackModelName);

                    additionalModels.put(fallbackModelName, fallbackModelsMap);
                }
            }
        }
        objs.putAll(additionalModels);

        return objs;
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
}
