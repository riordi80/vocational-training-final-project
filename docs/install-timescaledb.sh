#!/bin/bash

# Script para instalar TimescaleDB en Ubuntu 24.04 con PostgreSQL 16

echo "=== Instalando TimescaleDB ==="

# Añadir repositorio de TimescaleDB
echo "1. Añadiendo repositorio de TimescaleDB..."
sudo sh -c "echo 'deb [signed-by=/usr/share/keyrings/timescaledb.keyring] https://packagecloud.io/timescale/timescaledb/ubuntu/ $(lsb_release -c -s) main' > /etc/apt/sources.list.d/timescaledb.list"

# Importar clave GPG
echo "2. Importando clave GPG..."
wget --quiet -O - https://packagecloud.io/timescale/timescaledb/gpgkey | sudo gpg --dearmor -o /usr/share/keyrings/timescaledb.keyring

# Actualizar repositorios
echo "3. Actualizando repositorios..."
sudo apt update

# Instalar TimescaleDB para PostgreSQL 16
echo "4. Instalando timescaledb-2-postgresql-16..."
sudo apt install -y timescaledb-2-postgresql-16

# Configurar TimescaleDB
echo "5. Configurando TimescaleDB..."
sudo timescaledb-tune --quiet --yes

# Reiniciar PostgreSQL
echo "6. Reiniciando PostgreSQL..."
sudo systemctl restart postgresql

echo ""
echo "=== Instalación completada ==="
echo ""
echo "Ahora puedes crear la extensión en tu base de datos:"
echo "  psql -U arboles_user -d proyecto_arboles -h localhost"
echo "  CREATE EXTENSION IF NOT EXISTS timescaledb;"
echo ""
