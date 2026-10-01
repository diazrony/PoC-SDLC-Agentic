-- Datos de ejemplo. Solo se cargan cuando la generacion de esquema esta activa
-- (perfiles dev y test). En produccion DB_SCHEMA_GENERATION=none.
INSERT INTO WITHDRAWALS (ID, LABEL, STATUS, AMOUNT, CREATED_AT)
VALUES ('W-0001', 'Withdrawal de ejemplo activo', 'ACTIVE', 125.50, TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00');

INSERT INTO WITHDRAWALS (ID, LABEL, STATUS, AMOUNT, CREATED_AT)
VALUES ('W-0002', 'Withdrawal de ejemplo bloqueado', 'BLOCKED', 0.00, TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00');
