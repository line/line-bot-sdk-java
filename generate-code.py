import os
import re
import subprocess
import sys
import urllib.request
from datetime import datetime
from pathlib import Path


def get_generated_java_files():
    for file in Path('.').glob('**/.openapi-generator/FILES'):
        lines = []
        with file.open() as fp:
            for line in fp.readlines():
                line = str(file.parent.parent / line.strip())
                if line.endswith('.java'):
                    lines.append(line)
        yield lines


def download_java_format():
    url = 'https://github.com/google/google-java-format/releases/download/v1.18.1/google-java-format-1.18.1-all-deps.jar'
    filename = url.split('/')[-1]  # Extract the file name
    directory = Path('tools')
    jarfile = directory / filename

    if Path(jarfile).exists():
        pass
    else:
        os.makedirs(directory, exist_ok=True)
        urllib.request.urlretrieve(url, jarfile)

    return jarfile


def run_google_java_format():
    jarfile = download_java_format()
    for files in get_generated_java_files():
        subprocess.run(['java', '-jar', str(jarfile), '--replace'] + files, check=True)


def remove_previous_files(target):
    file = f"{target}/.openapi-generator/FILES"
    with open(file) as fp:
        for line in fp.readlines():
            path = target + "/" + line.strip()
            try:
                os.unlink(path)
            except FileNotFoundError:
                print(f'{path} not found.')


def run_command(command):
    proc = subprocess.run(command, shell=True, text=True, capture_output=True)

    if proc.returncode != 0:
        print("\n\nSTDOUT:\n\n")
        print(proc.stdout)

    if len(proc.stderr) != 0:
        print("\n\nSTDERR:\n\n")
        print(proc.stderr)
        print("\n\n")

    if proc.returncode != 0:
        print(f"\n\nCommand '{command}' returned non-zero exit status {proc.returncode}.")
        sys.exit(1)

    return proc.stdout.strip()

# Get the current year
current_year = datetime.now().year

LICENSE_HEADER = f"""/*
 * Copyright {current_year} LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
"""

CLASS_TEMPLATE = """package {package};

public record {unknown_class_name}() implements {interface_name} {{
}}
"""

def generate_unknown_classes(base_directories):
    interface_pattern = re.compile(
        r'defaultImpl = Unknown(\w+).class'
    )
    # List of interfaces for which an Unknown class should not be generated
    excluded_interfaces = [
        "Event", "MessageContent", "Message", "Action", "DemographicFilter",
        "FlexBoxBackground", "FlexComponent", "FlexContainer", "ImagemapAction",
        "Mentionee", "ModuleContent", "Recipient", "RichMenuBatchOperation",
        "Source", "Template", "ThingsContent"
    ]

    for base_directory in base_directories:
        for root, _, files in os.walk(base_directory):
            for file in files:
                if file.endswith('.java'):
                    file_path = os.path.join(root, file)
                    with open(file_path, 'r', encoding='utf-8') as f:
                        content = f.read()

                    match = interface_pattern.search(content)
                    if match:
                        interface_name = match.group(1)
                        # Skip generation for excluded interfaces
                        if interface_name in excluded_interfaces:
                            continue
                        unknown_class_name = f"Unknown{interface_name}"

                        package_match = re.search(r'package\s+([\w\.]+);', content)
                        package_name = package_match.group(1) if package_match else "default.package"

                        unknown_class_file_path = os.path.join(root, f"{unknown_class_name}.java")
                        with open(unknown_class_file_path, 'w', encoding='utf-8') as f:
                            f.write(LICENSE_HEADER)
                            f.write('\n')
                            f.write(CLASS_TEMPLATE.format(
                                package=package_name,
                                unknown_class_name=unknown_class_name,
                                interface_name=interface_name
                            ))
                        print(f"Generated {unknown_class_file_path}")

def main():
    os.chdir("generator")
    run_command('mvn package -DskipTests=true')
    os.chdir("..")

    components = [
        {"sourceYaml": "channel-access-token.yml", "package": "com.linecorp.bot.oauth",
         "outdir": "clients/line-channel-access-token-client"},
        {"sourceYaml": "insight.yml", "package": "com.linecorp.bot.insight",
         "outdir": "clients/line-bot-insight-client"},
        {"sourceYaml": "liff.yml", "package": "com.linecorp.bot.liff", "outdir": "clients/line-liff-client"},
        {"sourceYaml": "manage-audience.yml", "package": "com.linecorp.bot.audience",
         "outdir": "clients/line-bot-manage-audience-client"},
        {"sourceYaml": "messaging-api.yml", "package": "com.linecorp.bot.messaging",
         "outdir": "clients/line-bot-messaging-api-client"},
        {"sourceYaml": "module-attach.yml", "package": "com.linecorp.bot.moduleattach",
         "outdir": "clients/line-bot-module-attach-client"},
        {"sourceYaml": "module.yml", "package": "com.linecorp.bot.module", "outdir": "clients/line-bot-module-client"},
        {"sourceYaml": "shop.yml", "package": "com.linecorp.bot.shop", "outdir": "clients/line-bot-shop-client"},

    ]

    for component in components:
        sourceYaml = component['sourceYaml']
        package = component['package']
        packagePath = package.replace(".", "/")

        remove_previous_files(component['outdir'])

        command = f'''java \\
                    -cp ./generator/target/line-java-codegen-1.0.0.jar \\
                    org.openapitools.codegen.OpenAPIGenerator \\
                    generate \\
                    -e pebble \\
                    -g line-java-codegen \\
                    -o {component['outdir']} \\
                    --global-property modelDocs=false \\
                    --global-property apiDocs=false \\
                    --additional-properties=excludeText=true \\
                    --additional-properties=generateSourceCodeOnly=true \\
                    --additional-properties=modelPackage={package}.model \\
                    --additional-properties=apiPackage={package}.client \\
                    --package-name {package} \\
                    -i line-openapi/{sourceYaml} \\
                    --additional-properties=dateLibrary=java8 \\
                    --additional-properties=openApiNullable=false \\
                    --additional-properties=removeEnumValuePrefix=false \\
                    --additional-properties=apiNameSuffix=Client \\
                    --additional-properties=authenticated=true'''
        run_command(command)

    ## webhook requires only models.
    sourceYaml = "webhook.yml"
    package = "com.linecorp.bot.webhook"
    packagePath = package.replace(".", "/")

    remove_previous_files('line-bot-webhook')

    # run_command(f'rm -rf line-bot-webhook/src/main/java/{modelPackagePath}/')

    command = f'''java \\
                -cp ./generator/target/line-java-codegen-1.0.0.jar \\
                org.openapitools.codegen.OpenAPIGenerator \\
                generate \\
                -e pebble \\
                -g line-java-codegen \\
                -o line-bot-webhook/ \\
                --global-property modelDocs=false,modelTests=false,apiDocs=false,api=false \\
                --additional-properties=modelPackage={package}.model \\
                --additional-properties=apiPackage={package}.client \\
                --package-name {package} \\
                -i line-openapi/{sourceYaml} \\
                --additional-properties=dateLibrary=java8 \\
                --additional-properties=openApiNullable=false
              '''
    run_command(command)

    generate_unknown_classes([
        './line-bot-webhook/src/main/java/com/linecorp/bot/webhook/model',
        './clients/line-channel-access-token-client/src/main/java/com/linecorp/bot/oauth/model',
        './clients/line-bot-insight-client/src/main/java/com/linecorp/bot/insight/model',
        './clients/line-liff-client/src/main/java/com/linecorp/bot/liff/model',
        './clients/line-bot-manage-audience-client/src/main/java/com/linecorp/bot/audience/model',
        './clients/line-bot-messaging-api-client/src/main/java/com/linecorp/bot/messaging/model',
        './clients/line-bot-module-attach-client/src/main/java/com/linecorp/bot/moduleattach/model',
        './clients/line-bot-module-client/src/main/java/com/linecorp/bot/module/model',
        './clients/line-bot-shop-client/src/main/java/com/linecorp/bot/shop/model',
    ])

    run_google_java_format()


if __name__ == "__main__":
    main()
