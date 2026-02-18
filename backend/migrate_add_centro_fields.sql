-- ============================================
-- MIGRACIÓN: Añadir campos a centro_educativo
-- (isla, poblacion, provincia, codigo_postal, telefono, email)
-- Idempotente: se puede ejecutar múltiples veces
-- ============================================

ALTER TABLE centro_educativo ADD COLUMN IF NOT EXISTS isla VARCHAR(20);
ALTER TABLE centro_educativo ADD COLUMN IF NOT EXISTS poblacion VARCHAR(100);
ALTER TABLE centro_educativo ADD COLUMN IF NOT EXISTS provincia VARCHAR(100);
ALTER TABLE centro_educativo ADD COLUMN IF NOT EXISTS codigo_postal VARCHAR(10);
ALTER TABLE centro_educativo ADD COLUMN IF NOT EXISTS telefono VARCHAR(20);
ALTER TABLE centro_educativo ADD COLUMN IF NOT EXISTS email VARCHAR(150);

-- CHECK constraint para isla (solo si no existe)
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.check_constraints
        WHERE constraint_name = 'chk_centro_isla'
    ) THEN
        ALTER TABLE centro_educativo
        ADD CONSTRAINT chk_centro_isla CHECK (isla IN ('TENERIFE','GRAN_CANARIA','LANZAROTE','FUERTEVENTURA','LA_PALMA','LA_GOMERA','EL_HIERRO'));
    END IF;
END $$;
