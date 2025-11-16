-- ============================================
-- SCRIPT PARA ELIMINAR TODAS LAS TABLAS
-- Proyecto Árboles (Garden Monitor)
-- ============================================
-- ADVERTENCIA: Este script eliminará TODOS los datos
-- ============================================

-- Eliminar tablas en orden inverso (respetando foreign keys)
DROP TABLE IF EXISTS notificacion CASCADE;
DROP TABLE IF EXISTS usuario_centro CASCADE;
DROP TABLE IF EXISTS alerta CASCADE;
DROP TABLE IF EXISTS lectura CASCADE;
DROP TABLE IF EXISTS arbol CASCADE;
DROP TABLE IF EXISTS dispositivo_esp32 CASCADE;
DROP TABLE IF EXISTS centro_educativo CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;

-- Verificar que no queden tablas
\dt

-- Mensaje
SELECT 'Todas las tablas han sido eliminadas. Ahora ejecuta create_database.sql' AS mensaje;
