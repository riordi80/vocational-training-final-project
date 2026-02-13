-- ============================================
-- SCRIPT DE MIGRACIÓN: Roles simplificados
-- Proyecto Árboles (Garden Monitor)
-- Ejecutar sobre BD existentes con el esquema antiguo
-- ============================================

-- 1. Actualizar rol de usuario: USUARIO → COORDINADOR
ALTER TABLE usuario DROP CONSTRAINT chk_usuario_rol;
UPDATE usuario SET rol = 'COORDINADOR' WHERE rol = 'USUARIO';
ALTER TABLE usuario ADD CONSTRAINT chk_usuario_rol CHECK (rol IN ('ADMIN', 'COORDINADOR'));

-- 2. Eliminar columna rol_en_centro de usuario_centro
ALTER TABLE usuario_centro DROP CONSTRAINT chk_usuario_centro_rol;
ALTER TABLE usuario_centro DROP COLUMN rol_en_centro;
