-- ============================================
-- SCRIPT DE MIGRACIÓN: Roles simplificados
-- Proyecto Árboles
-- Ejecutar sobre BD existentes con el esquema antiguo
-- ============================================

-- 1. Limpiar CHECK constraints de la tabla usuario
ALTER TABLE usuario DROP CONSTRAINT IF EXISTS chk_usuario_rol;
ALTER TABLE usuario DROP CONSTRAINT IF EXISTS usuario_rol_check;

-- 2. Actualizar rol de usuario: USUARIO → COORDINADOR
UPDATE usuario SET rol = 'COORDINADOR' WHERE rol = 'USUARIO';
ALTER TABLE usuario ADD CONSTRAINT chk_usuario_rol CHECK (rol IN ('ADMIN', 'COORDINADOR'));

-- 3. Eliminar columna rol_en_centro de usuario_centro
ALTER TABLE usuario_centro DROP CONSTRAINT IF EXISTS chk_usuario_centro_rol;
ALTER TABLE usuario_centro DROP COLUMN IF EXISTS rol_en_centro;
