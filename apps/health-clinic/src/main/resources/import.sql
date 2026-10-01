-- Datos de ejemplo. Solo se cargan cuando la generacion de esquema esta activa
-- (perfiles dev y test). En produccion DB_SCHEMA_GENERATION=none.
INSERT INTO PATIENTS (ID, LABEL, STATUS, COPAYMENT_AMOUNT, CREATED_AT)
VALUES ('P-0001', 'Patient de ejemplo activo', 'ACTIVE', 125.50, TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00');

INSERT INTO PATIENTS (ID, LABEL, STATUS, COPAYMENT_AMOUNT, CREATED_AT)
VALUES ('P-0002', 'Patient de ejemplo bloqueado', 'BLOCKED', 0.00, TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00');
