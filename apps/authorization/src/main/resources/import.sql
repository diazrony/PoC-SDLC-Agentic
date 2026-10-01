-- Datos de ejemplo. Solo se cargan cuando la generacion de esquema esta activa
-- (perfiles dev y test). En produccion DB_SCHEMA_GENERATION=none.
INSERT INTO AUTHORIZATIONS (ID, LABEL, STATUS, LIMIT_AMOUNT, CREATED_AT)
VALUES ('A-0001', 'Authorization de ejemplo activo', 'ACTIVE', 125.50, TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00');

INSERT INTO AUTHORIZATIONS (ID, LABEL, STATUS, LIMIT_AMOUNT, CREATED_AT)
VALUES ('A-0002', 'Authorization de ejemplo bloqueado', 'BLOCKED', 0.00, TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00');
