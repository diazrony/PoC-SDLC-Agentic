# 0003. Separacion estricta entre apps y libs

- Estado: Aceptada
- Fecha: 2026-01-15

## Contexto

El riesgo numero uno de un monorepo es que, al estar todo el codigo a mano,
alguien resuelva una necesidad importando una clase de otra aplicacion. El
resultado es un monolito distribuido: componentes que ya no se pueden
desplegar por separado aunque sigan siendo seis procesos.

## Decision

Solo se permiten estas direcciones:

```text
apps -> libs       PERMITIDO
libs -> libs       PERMITIDO DE FORMA CONTROLADA
apps -> apps       PROHIBIDO
libs -> apps       PROHIBIDO
```

Se verifica en tres niveles independientes:

1. **Maven Enforcer** (`apps/pom.xml`, `libs/pom.xml`): regla
   `bannedDependencies` con `searchTransitive`, que rechaza la dependencia en
   la fase `validate`.
2. **ArchUnit** (`MonorepoDependencyRules`): rechaza la referencia a nivel de
   paquete, por si alguien copia una clase en vez de declarar la dependencia.
3. **CODEOWNERS**: cambiar `libs/` requiere la revision de arquitectura.

`libs/testing` solo puede declararse en scope `test`; el Enforcer lo
comprueba para que ArchUnit y JUnit no acaben en la imagen de produccion.

## Si dos aplicaciones necesitan hablarse

Opciones aceptables, por orden de preferencia:

1. Extraer el concepto comun a una libreria (si es tecnico y estable).
2. Llamada HTTP o evento entre servicios (si es negocio).
3. Compartir un contrato de evento en `libs/events` (ver ADR 0006).

Lo que nunca es aceptable es una dependencia Maven directa entre aplicaciones.

## Consecuencias

- Cada aplicacion sigue siendo desplegable, escalable y testeable por
  separado.
- Extraer una aplicacion a su propio repositorio sigue siendo una operacion
  mecanica.
- El coste es una conversacion extra cuando alguien quiere reutilizar algo.
  Esa conversacion es precisamente el punto.
