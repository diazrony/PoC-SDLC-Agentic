-- Datos de ejemplo. Solo se cargan cuando la generacion de esquema esta activa
-- (perfiles dev y test). En produccion DB_SCHEMA_GENERATION=none.
INSERT INTO CONFIGURATION_SETTINGS (ID, LABEL, STATUS, NUMERIC_VALUE, CREATED_AT)
VALUES ('C-0001', 'ConfigurationSetting de ejemplo activo', 'ACTIVE', 125.50, TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00');

INSERT INTO CONFIGURATION_SETTINGS (ID, LABEL, STATUS, NUMERIC_VALUE, CREATED_AT)
VALUES ('C-0002', 'ConfigurationSetting de ejemplo bloqueado', 'BLOCKED', 0.00, TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00');
