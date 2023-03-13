#!/usr/bin/env python3

#  Copyright 2023 LINE Corporation
#
#  LINE Corporation licenses this file to you under the Apache License,
#  version 2.0 (the "License"); you may not use this file except in compliance
#  with the License. You may obtain a copy of the License at:
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
#  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
#  License for the specific language governing permissions and limitations
#  under the License.

import os.path
import re
import shutil
import subprocess
import sys
from pathlib import Path

# remove old files
shutil.rmtree(os.path.expanduser("~/.m2/repository/com/linecorp/bot/"), ignore_errors=True)

# publish artifacts to the local maven directory
subprocess.run("./gradlew publishToMavenLocal", shell=True, check=True)

# checking pom files
fails = 0

pattern = re.compile("""<dependency>.*?</dependency>""", re.DOTALL)
root = Path(os.path.expanduser("~/.m2/repository/com/linecorp/bot/"))
for fname in root.glob("**/*.pom"):
    if fname.name.startswith("line-bot-spring-boot"):
        # line-bot-spring-boot can depends on spring-boot bom.
        continue
    if fname.name.startswith("line-bot-cli"):
        # we don't upload line-bot-cli to the maven central.
        continue

    print(f"======= {fname}")
    with open(fname, "r") as fp:
        content = fp.read()
        for dependency in re.findall(pattern, content):
            if "<version>" not in dependency:
                print(dependency)
                fails += 1

if fails != 0:
    print("FAILED...")
    sys.exit(1)

