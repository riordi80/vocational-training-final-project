-- ============================================
-- MIGRACIÓN: modelo dispositivo-árbol → dispositivo-centro
-- + traslado de umbrales de arbol → dispositivo_esp32
-- Ejecutar en local y en Render (producción)
-- ============================================

BEGIN;

-- ── 1. dispositivo_esp32: añadir centro_id ──────────────────────────────────
ALTER TABLE dispositivo_esp32
    ADD COLUMN IF NOT EXISTS centro_id BIGINT;

UPDATE dispositivo_esp32 d
SET centro_id = a.centro_id
FROM arbol a
WHERE a.dispositivo_id = d.id;

ALTER TABLE dispositivo_esp32
    ALTER COLUMN centro_id SET NOT NULL;

ALTER TABLE dispositivo_esp32
    ADD CONSTRAINT fk_dispositivo_centro
        FOREIGN KEY (centro_id) REFERENCES centro_educativo(id) ON DELETE CASCADE;

CREATE INDEX IF NOT EXISTS idx_dispositivo_centro ON dispositivo_esp32(centro_id);

-- ── 2. alerta: dispositivo_id ya existe — poblar nulls y soltar arbol_id ────
UPDATE alerta al
SET dispositivo_id = a.dispositivo_id
FROM arbol a
WHERE a.id = al.arbol_id
  AND al.dispositivo_id IS NULL;

DROP INDEX IF EXISTS idx_alerta_arbol;

ALTER TABLE alerta
    DROP CONSTRAINT IF EXISTS fk4j6cyhhtkkxnk7wi2hxdu61op;   -- FK alerta.arbol_id → arbol

ALTER TABLE alerta
    DROP COLUMN IF EXISTS arbol_id;

-- ── 3. lectura: dispositivo_id ya existe — solo soltar arbol_id ─────────────
DROP INDEX IF EXISTS idx_lectura_arbol_timestamp;

ALTER TABLE lectura
    DROP CONSTRAINT IF EXISTS fkpb4l8cgk437j6cpqnxhb46kpg;   -- FK lectura.arbol_id → arbol

ALTER TABLE lectura
    DROP COLUMN IF EXISTS arbol_id;

-- ── 4. arbol: soltar dispositivo_id ─────────────────────────────────────────
ALTER TABLE arbol
    DROP CONSTRAINT IF EXISTS fk_dispositivo_esp32;

DROP INDEX IF EXISTS idx_arbol_dispositivo_esp32;

ALTER TABLE arbol
    DROP COLUMN IF EXISTS dispositivo_id;

-- ── 5. arbol: soltar columnas de umbrales (pasan al dispositivo) ─────────────
ALTER TABLE arbol
    DROP COLUMN IF EXISTS umbral_temp_min,
    DROP COLUMN IF EXISTS umbral_temp_max,
    DROP COLUMN IF EXISTS umbral_humedad_ambiente_min,
    DROP COLUMN IF EXISTS umbral_humedad_ambiente_max,
    DROP COLUMN IF EXISTS umbral_humedad_suelo_min,
    DROP COLUMN IF EXISTS umbral_co2_max;

-- ── 6. dispositivo_esp32: añadir columnas de umbrales ───────────────────────
ALTER TABLE dispositivo_esp32
    ADD COLUMN IF NOT EXISTS umbral_temp_min             DECIMAL(5,2) DEFAULT 5.00,
    ADD COLUMN IF NOT EXISTS umbral_temp_max             DECIMAL(5,2) DEFAULT 40.00,
    ADD COLUMN IF NOT EXISTS umbral_humedad_ambiente_min DECIMAL(5,2) DEFAULT 30.00,
    ADD COLUMN IF NOT EXISTS umbral_humedad_ambiente_max DECIMAL(5,2) DEFAULT 90.00,
    ADD COLUMN IF NOT EXISTS umbral_humedad_suelo_min    DECIMAL(5,2) DEFAULT 30.00,
    ADD COLUMN IF NOT EXISTS umbral_co2_max              DECIMAL(7,2) DEFAULT 1000.00;

COMMIT;
