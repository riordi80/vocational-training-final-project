-- ============================================
-- SCRIPT DE CREACIÓN DE BASE DE DATOS
-- Proyecto Árboles (Garden Monitor)
-- PostgreSQL + TimescaleDB
-- ============================================

-- Crear extensión TimescaleDB
CREATE EXTENSION IF NOT EXISTS timescaledb;

-- ============================================
-- 1. TABLA: usuario
-- ============================================
CREATE TABLE usuario (
    id BIGSERIAL,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    fecha_creacion TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_usuario PRIMARY KEY (id),
    CONSTRAINT uq_usuario_email UNIQUE (email),
    CONSTRAINT chk_usuario_rol CHECK (rol IN ('ADMIN', 'COORDINADOR'))
);

CREATE INDEX idx_usuario_rol ON usuario(rol);
CREATE INDEX idx_usuario_activo ON usuario(activo);

-- ============================================
-- 2. TABLA: centro_educativo
-- ============================================
CREATE TABLE centro_educativo (
    id BIGSERIAL,
    nombre VARCHAR(200) NOT NULL,
    direccion VARCHAR(300) NOT NULL,
    latitud DECIMAL(10, 8),
    longitud DECIMAL(11, 8),
    responsable VARCHAR(100),
    isla VARCHAR(20),
    poblacion VARCHAR(100),
    provincia VARCHAR(100),
    codigo_postal VARCHAR(10),
    telefono VARCHAR(20),
    email VARCHAR(150),
    fecha_creacion TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_centro_educativo PRIMARY KEY (id),
    CONSTRAINT chk_centro_isla CHECK (isla IN ('GRAN_CANARIA','TENERIFE','LANZAROTE','FUERTEVENTURA','LA_PALMA','LA_GOMERA','EL_HIERRO'))
);

CREATE INDEX idx_centro_educativo_nombre ON centro_educativo(nombre);

-- ============================================
-- 3. TABLA: dispositivo_esp32
-- ============================================
CREATE TABLE dispositivo_esp32 (
    id BIGSERIAL,
    mac_address VARCHAR(17) NOT NULL,
    arbol_id BIGINT,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    ultima_conexion TIMESTAMPTZ,
    frecuencia_lectura_min INTEGER DEFAULT 15,
    CONSTRAINT pk_dispositivo_esp32 PRIMARY KEY (id),
    CONSTRAINT uq_dispositivo_mac_address UNIQUE (mac_address)
);

CREATE INDEX idx_dispositivo_activo ON dispositivo_esp32(activo);

-- ============================================
-- 4. TABLA: arbol
-- ============================================
CREATE TABLE arbol (
    id BIGSERIAL,
    centro_id BIGINT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    especie VARCHAR(150) NOT NULL,
    fecha_plantacion DATE NOT NULL,
    ubicacion_especifica VARCHAR(200),
    dispositivo_id BIGINT,
    -- Umbrales de temperatura
    umbral_temp_min DECIMAL(5, 2) DEFAULT 5.00,
    umbral_temp_max DECIMAL(5, 2) DEFAULT 40.00,
    -- Umbrales de humedad ambiental
    umbral_humedad_ambiente_min DECIMAL(5, 2) DEFAULT 30.00,
    umbral_humedad_ambiente_max DECIMAL(5, 2) DEFAULT 90.00,
    -- Umbrales de humedad del suelo
    umbral_humedad_suelo_min DECIMAL(5, 2) DEFAULT 30.00,
    -- Umbrales de CO2 (opcional, solo si tiene sensor)
    umbral_co2_max DECIMAL(7, 2) DEFAULT 1000.00,
    -- Absorción de CO2 estimada al año (kg CO2/año)
    absorcion_co2_anual DECIMAL(8, 2),
    CONSTRAINT pk_arbol PRIMARY KEY (id),
    CONSTRAINT fk_arbol_centro FOREIGN KEY (centro_id) REFERENCES centro_educativo(id) ON DELETE CASCADE,
    CONSTRAINT fk_arbol_dispositivo FOREIGN KEY (dispositivo_id) REFERENCES dispositivo_esp32(id) ON DELETE SET NULL,
    CONSTRAINT uq_arbol_dispositivo UNIQUE (dispositivo_id)
);

CREATE INDEX idx_arbol_centro ON arbol(centro_id);
CREATE INDEX idx_arbol_especie ON arbol(especie);

-- Actualizar foreign key en dispositivo_esp32
ALTER TABLE dispositivo_esp32
ADD CONSTRAINT fk_dispositivo_arbol FOREIGN KEY (arbol_id) REFERENCES arbol(id) ON DELETE SET NULL;

-- ============================================
-- 5. TABLA: lectura (HYPERTABLE)
-- ============================================
CREATE TABLE lectura (
    id BIGSERIAL NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL,
    arbol_id BIGINT NOT NULL,
    dispositivo_id BIGINT NOT NULL,
    -- Sensores obligatorios (configuración básica)
    temperatura DECIMAL(5, 2) NOT NULL,
    humedad_ambiente DECIMAL(5, 2) NOT NULL,
    humedad_suelo DECIMAL(5, 2) NOT NULL,
    -- Sensores opcionales (configuración avanzada)
    co2 DECIMAL(7, 2),                          -- NULL si no tiene sensor CO2
    diametro_tronco DECIMAL(6, 2),              -- NULL si no tiene dendómetro
    CONSTRAINT pk_lectura PRIMARY KEY (id, timestamp),
    CONSTRAINT fk_lectura_arbol FOREIGN KEY (arbol_id) REFERENCES arbol(id) ON DELETE CASCADE,
    CONSTRAINT fk_lectura_dispositivo FOREIGN KEY (dispositivo_id) REFERENCES dispositivo_esp32(id) ON DELETE CASCADE,
    -- Validaciones de rangos razonables
    CONSTRAINT chk_temperatura CHECK (temperatura BETWEEN -50.00 AND 80.00),
    CONSTRAINT chk_humedad_ambiente CHECK (humedad_ambiente BETWEEN 0.00 AND 100.00),
    CONSTRAINT chk_humedad_suelo CHECK (humedad_suelo BETWEEN 0.00 AND 100.00),
    CONSTRAINT chk_co2 CHECK (co2 IS NULL OR co2 BETWEEN 0.00 AND 10000.00),
    CONSTRAINT chk_diametro_tronco CHECK (diametro_tronco IS NULL OR diametro_tronco BETWEEN 0.00 AND 5000.00)
);

-- Convertir a hypertable (TimescaleDB)
SELECT create_hypertable('lectura', 'timestamp');

CREATE INDEX idx_lectura_arbol_timestamp ON lectura(arbol_id, timestamp DESC);
CREATE INDEX idx_lectura_dispositivo_timestamp ON lectura(dispositivo_id, timestamp DESC);

-- ============================================
-- 6. TABLA: alerta
-- ============================================
CREATE TABLE alerta (
    id BIGSERIAL,
    arbol_id BIGINT NOT NULL,
    tipo_alerta VARCHAR(50) NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    mensaje TEXT NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVA',
    fecha_resolucion TIMESTAMPTZ,
    CONSTRAINT pk_alerta PRIMARY KEY (id),
    CONSTRAINT fk_alerta_arbol FOREIGN KEY (arbol_id) REFERENCES arbol(id) ON DELETE CASCADE,
    CONSTRAINT chk_alerta_tipo CHECK (tipo_alerta IN (
        'TEMPERATURA_ALTA',
        'TEMPERATURA_BAJA',
        'HUMEDAD_AMBIENTE_BAJA',
        'HUMEDAD_AMBIENTE_ALTA',
        'HUMEDAD_SUELO_BAJA',
        'CO2_ALTO',
        'DISPOSITIVO_DESCONECTADO'
    )),
    CONSTRAINT chk_alerta_estado CHECK (estado IN ('ACTIVA', 'RESUELTA', 'IGNORADA'))
);

CREATE INDEX idx_alerta_arbol ON alerta(arbol_id);
CREATE INDEX idx_alerta_estado ON alerta(estado);
CREATE INDEX idx_alerta_timestamp ON alerta(timestamp DESC);

-- ============================================
-- 7. TABLA: usuario_centro (N:M)
-- ============================================
CREATE TABLE usuario_centro (
    id BIGSERIAL,
    usuario_id BIGINT NOT NULL,
    centro_id BIGINT NOT NULL,
    fecha_asignacion TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_usuario_centro PRIMARY KEY (id),
    CONSTRAINT fk_usuario_centro_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    CONSTRAINT fk_usuario_centro_centro FOREIGN KEY (centro_id) REFERENCES centro_educativo(id) ON DELETE CASCADE,
    CONSTRAINT uq_usuario_centro UNIQUE (usuario_id, centro_id)
);

CREATE INDEX idx_usuario_centro_usuario ON usuario_centro(usuario_id);
CREATE INDEX idx_usuario_centro_centro ON usuario_centro(centro_id);

-- ============================================
-- 8. TABLA: notificacion
-- ============================================
CREATE TABLE notificacion (
    id BIGSERIAL,
    usuario_id BIGINT NOT NULL,
    alerta_id BIGINT NOT NULL,
    leida BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_envio TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_notificacion PRIMARY KEY (id),
    CONSTRAINT fk_notificacion_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    CONSTRAINT fk_notificacion_alerta FOREIGN KEY (alerta_id) REFERENCES alerta(id) ON DELETE CASCADE
);

CREATE INDEX idx_notificacion_usuario_leida ON notificacion(usuario_id, leida);
CREATE INDEX idx_notificacion_fecha ON notificacion(fecha_envio DESC);

-- ============================================
-- FIN DEL SCRIPT
-- ============================================
