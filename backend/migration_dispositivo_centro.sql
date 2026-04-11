-- ============================================
-- MIGRACIÓN: modelo dispositivo-árbol → dispositivo-centro
-- Ejecutar en local y en Render (producción)
-- ============================================

BEGIN;

-- 1. Añadir centro_id en dispositivo_esp32 (nullable primero)
ALTER TABLE dispositivo_esp32
    ADD COLUMN centro_id BIGINT;

-- 2. Poblar centro_id a partir del árbol vinculado
UPDATE dispositivo_esp32 d
SET centro_id = a.centro_id
FROM arbol a
WHERE a.dispositivo_id = d.id;

-- 3. Hacer centro_id NOT NULL y añadir FK
ALTER TABLE dispositivo_esp32
    ALTER COLUMN centro_id SET NOT NULL;

ALTER TABLE dispositivo_esp32
    ADD CONSTRAINT fk_dispositivo_centro
        FOREIGN KEY (centro_id) REFERENCES centro_educativo(id) ON DELETE CASCADE;

CREATE INDEX idx_dispositivo_centro ON dispositivo_esp32(centro_id);

-- 4. Eliminar arbol_id de dispositivo_esp32
ALTER TABLE dispositivo_esp32
    DROP CONSTRAINT IF EXISTS fk_dispositivo_arbol;

ALTER TABLE dispositivo_esp32
    DROP COLUMN IF EXISTS arbol_id;

-- 5. Añadir dispositivo_id en alerta (nullable — para conservar alertas históricas)
ALTER TABLE alerta
    ADD COLUMN dispositivo_id BIGINT;

-- 6. Poblar dispositivo_id en alerta a partir del árbol vinculado
UPDATE alerta al
SET dispositivo_id = a.dispositivo_id
FROM arbol a
WHERE a.id = al.arbol_id;

ALTER TABLE alerta
    ADD CONSTRAINT fk_alerta_dispositivo
        FOREIGN KEY (dispositivo_id) REFERENCES dispositivo_esp32(id) ON DELETE CASCADE;

CREATE INDEX idx_alerta_dispositivo ON alerta(dispositivo_id);

-- 7. Eliminar arbol_id de alerta
DROP INDEX IF EXISTS idx_alerta_arbol;

ALTER TABLE alerta
    DROP CONSTRAINT IF EXISTS fk_alerta_arbol;

ALTER TABLE alerta
    DROP COLUMN IF EXISTS arbol_id;

-- 8. Eliminar arbol_id de lectura (hypertable TimescaleDB)
DROP INDEX IF EXISTS idx_lectura_arbol_timestamp;

ALTER TABLE lectura
    DROP CONSTRAINT IF EXISTS fk_lectura_arbol;

ALTER TABLE lectura
    DROP COLUMN IF EXISTS arbol_id;

-- 9. Eliminar dispositivo_id y FK de arbol
ALTER TABLE arbol
    DROP CONSTRAINT IF EXISTS uq_arbol_dispositivo;

ALTER TABLE arbol
    DROP CONSTRAINT IF EXISTS fk_arbol_dispositivo;

DROP INDEX IF EXISTS idx_arbol_dispositivo_esp32;

ALTER TABLE arbol
    DROP COLUMN IF EXISTS dispositivo_id;

COMMIT;
