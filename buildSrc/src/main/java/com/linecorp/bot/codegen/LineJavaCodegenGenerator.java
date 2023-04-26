package com.linecorp.bot.codegen;

import static org.openapitools.codegen.utils.StringUtils.camelize;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

                String name = codegenModel.name.replace(codegenModel.parent, "");
                if (codegenModel.parent.equals("FlexComponent") || codegenModel.parent.equals("FlexContainer")) {
                    // Flex related components are prefixed.
                    name = name.replace("Flex", "");
                }
                name = name.equals("URI") ? "uri" : lowerFirst(name);
                codegenModel.getVendorExtensions().put("x-javatypename", name);
            }

            // Implements ReplyEvent if the class has "replyToken" field.
            if (codegenModel.parent != null && codegenModel.parent.equals("Event")) {
                if (codegenModel.vars.stream().anyMatch(codegenProperty -> codegenProperty.name.equals("replyToken"))) {
                    addImplements(codegenModel, "ReplyEvent");
                }
            }

            // fill src/main/resources/body/*.java to the body of the class.
            try (InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream("body/" + codegenModel.name + ".java")) {
                if (inputStream != null) {
                    String src = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    codegenModel.vendorExtensions.put("x-body", indent4(src));
                }
            } catch (IOException e) {
                // do nothing.
            }
        }

        return modelsMap;
    }

    private String lowerFirst(String name) {
        String lower = name.toLowerCase();
        return lower.charAt(0) + name.substring(1);
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
