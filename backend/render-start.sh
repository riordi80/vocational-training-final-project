#!/usr/bin/env bash
# Script de start para Render

# Cargar SDKMAN y Java
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Ejecutar Spring Boot
java -jar target/gardenmonitor-0.0.1-SNAPSHOT.jar
