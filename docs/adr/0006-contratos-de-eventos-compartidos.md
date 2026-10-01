# 0006. Cuando compartir un contrato de evento y cuando no

- Estado: Aceptada
- Fecha: 2026-01-25

## Contexto

`libs/events` es la libreria mas peligrosa del monorepo. Un contrato de
evento compartido es una dependencia entre el productor y **todos** sus
consumidores. Si se usa mal, sustituye una dependencia explicita entre
aplicaciones (prohibida) por una implicita a traves de una libreria
(permitida pero igual de daniina).

## Decision

Compartir un contrato de evento **solo** cuando se cumplen las cuatro
condiciones:

1. El evento ya cruza la frontera del servicio: hay al menos un consumidor
   real, no un consumidor hipotetico.
2. El contrato es estable. Si el payload cambia cada sprint, compartirlo
   convierte cada cambio en un despliegue coordinado.
3. Contiene **solo estructura**: identificadores, tipos primitivos, fechas.
   Cero reglas de negocio, cero comportamiento.
4. Esta versionado en el propio tipo (`transaction.created.v1`), de modo que
   una version nueva pueda convivir con la anterior.

**No compartir** cuando: el evento es interno, cuando solo lo usa el
productor, o cuando se comparte "por si acaso". Lo correcto entonces es
dejarlo dentro de la aplicacion y promoverlo el dia que aparezca el segundo
consumidor.

## Donde se usa el contrato

Esta es la parte que mas se olvida: el contrato compartido aparece
**unicamente en el adaptador de salida**.

```text
application/port/out/TransactionEventPublisherPort   habla de DOMINIO
        ^
        | implementa
infrastructure/adapter/out/messaging/
        TransactionEventPublisherAdapter             habla de lib-events
```

El servicio de aplicacion publica `Transaction` (su propio modelo); el
adaptador lo traduce a `TransactionCreatedEvent`. Es exactamente el mismo
patron que con JPA: el contrato externo nunca entra en el hexagono.

## Estado actual

| Aplicacion    | Depende de `lib-events` | Por que                                  |
|---------------|-------------------------|-------------------------------------------|
| transaction   | Si                      | `transaction.created.v1` tiene consumidores |
| retiros       | Si                      | `withdrawal.requested.v1` tiene consumidores|
| authorization | No                      | Sus eventos son internos                  |
| utils         | No                      | Idem                                      |
| configuration | No                      | Idem                                      |
| health-clinic | No                      | Idem                                      |

Las cuatro aplicaciones sin `lib-events` tienen igualmente su puerto de
salida y un adaptador que registra el evento en el log. El dia que uno de
esos eventos tenga un consumidor externo, se promueve el contrato a la
libreria y **solo cambia el adaptador**.

## Efecto secundario util

Esta disciplina mejora el calculo de Affected Projects: un cambio en
`libs/events` afecta a dos aplicaciones, no a seis.
