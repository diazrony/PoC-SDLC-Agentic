# C4 - Nivel 1: Contexto

```mermaid
C4Context
    title Contexto del sistema

    Person(cliente, "Cliente", "Opera a traves de los canales digitales")
    Person(operador, "Operador interno", "Gestiona configuracion y soporte")

    System_Boundary(monorepo, "Plataforma (quarkus-enterprise-monorepo)") {
        System(autorizaciones, "Autorizaciones", "Autoriza operaciones")
        System(transacciones, "Transacciones", "Registra movimientos")
        System(retiros, "Retiros", "Gestiona solicitudes de retiro")
        System(clinica, "Health Clinic", "Gestiona pacientes y citas")
        System(configuracion, "Configuracion", "Parametros de plataforma")
        System(utilidades, "Utilidades", "Servicios tecnicos de apoyo")
    }

    System_Ext(core, "Core bancario", "Sistema de registro")
    System_Ext(broker, "Message Broker", "Distribucion de eventos")
    System_Ext(observabilidad, "Plataforma de observabilidad", "Logs, metricas y trazas")

    Rel(cliente, autorizaciones, "Solicita autorizaciones", "HTTPS/JSON")
    Rel(cliente, retiros, "Solicita retiros", "HTTPS/JSON")
    Rel(cliente, clinica, "Consulta su ficha", "HTTPS/JSON")
    Rel(operador, configuracion, "Ajusta parametros", "HTTPS/JSON")

    Rel(transacciones, broker, "Publica transaction.created.v1", "AMQP/Kafka")
    Rel(retiros, broker, "Publica withdrawal.requested.v1", "AMQP/Kafka")
    Rel(transacciones, core, "Consulta saldos", "HTTPS")
    Rel(autorizaciones, observabilidad, "Logs con traceId", "OTLP")
```

Nota importante: **ningun servicio de la plataforma llama directamente a
otro** en este diagrama. La comunicacion, cuando existe, es por eventos a
traves del broker. Eso es lo que hace cierta la regla `apps -> apps
PROHIBIDO` tambien en tiempo de ejecucion, y no solo en el POM.
