-- Datos de ejemplo. Solo se cargan cuando la generacion de esquema esta activa
-- (perfiles dev y test). En produccion DB_SCHEMA_GENERATION=none.
INSERT INTO UTILITIES (ID, LABEL, STATUS, THRESHOLD, CREATED_AT)
VALUES ('U-0001', 'Utility de ejemplo activo', 'ACTIVE', 125.50, TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00');

INSERT INTO UTILITIES (ID, LABEL, STATUS, THRESHOLD, CREATED_AT)
VALUES ('U-0002', 'Utility de ejemplo bloqueado', 'BLOCKED', 0.00, TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00');
