#!/usr/bin/env bash
# Script de build para Render

# Instalar Java 21 (Amazon Corretto)
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 21.0.1-amzn

# Compilar con Maven
./mvnw clean install -DskipTests

echo "Build completado correctamente"
