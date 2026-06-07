#!/bin/sh
set -e

MAVEN_PROJECTBASEDIR="$(cd "$(dirname "$0")" && pwd)"
WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
WRAPPER_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"

if [ ! -d "$MAVEN_PROJECTBASEDIR/.mvn/wrapper" ]; then
    mkdir -p "$MAVEN_PROJECTBASEDIR/.mvn/wrapper"
fi

if [ ! -f "$WRAPPER_JAR" ]; then
    echo "Downloading Maven Wrapper..."
    if command -v curl > /dev/null 2>&1; then
        curl -sL -o "$WRAPPER_JAR" "$WRAPPER_URL"
    elif command -v wget > /dev/null 2>&1; then
        wget -q -O "$WRAPPER_JAR" "$WRAPPER_URL"
    else
        echo "Error: curl or wget is required to download Maven Wrapper." >&2
        exit 1
    fi
fi

exec java \
    -Dmaven.multiModuleProjectDirectory="$MAVEN_PROJECTBASEDIR" \
    -cp "$WRAPPER_JAR" \
    org.apache.maven.wrapper.MavenWrapperMain "$@"
