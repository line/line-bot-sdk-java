import os
import subprocess
import sys

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

def main():

    os.chdir("generator")
    run_command('mvn package -DskipTests=true')
    os.chdir("..")

    components = [
        {"sourceYaml": "channel-access-token.yml", "package": "com.linecorp.bot.oauth", "outdir": "clients/line-channel-access-token-client"},
        {"sourceYaml": "insight.yml", "package": "com.linecorp.bot.insight", "outdir": "clients/line-bot-insight-client"},
        {"sourceYaml": "liff.yml", "package": "com.linecorp.bot.liff", "outdir": "clients/line-liff-client"},
        {"sourceYaml": "manage-audience.yml", "package": "com.linecorp.bot.audience", "outdir": "clients/line-bot-manage-audience-client"},
        {"sourceYaml": "messaging-api.yml", "package": "com.linecorp.bot.messaging", "outdir": "clients/line-bot-messaging-api-client"},
        {"sourceYaml": "module-attach.yml", "package": "com.linecorp.bot.moduleattach", "outdir": "clients/line-bot-module-attach-client"},
        {"sourceYaml": "module.yml", "package": "com.linecorp.bot.module", "outdir": "clients/line-bot-module-client"},
        {"sourceYaml": "shop.yml", "package": "com.linecorp.bot.shop", "outdir": "clients/line-bot-shop-client"},

    ]

    for component in components:
        sourceYaml = component['sourceYaml']
        package = component['package']
        packagePath = package.replace(".", "/")

        remove_previous_files(component['outdir'])

        command = f'''java \\
                    -cp ./tools/openapi-generator-cli.jar:./generator/target/line-java-codegen-1.0.0.jar \\
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
                -cp ./tools/openapi-generator-cli.jar:./generator/target/line-java-codegen-1.0.0.jar \\
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


if __name__ == "__main__":
    main()

