-- Migration: Add absorcion_co2_anual column to arbol table
-- Idempotent: safe to run multiple times

ALTER TABLE arbol
ADD COLUMN IF NOT EXISTS absorcion_co2_anual DECIMAL(8, 2);
