# 0004. Jandex en lugar de beans.xml para el descubrimiento CDI

- Estado: Aceptada
- Fecha: 2026-01-20

## Contexto

Quarkus/ArC resuelve CDI en tiempo de build. Para hacerlo necesita un indice
de clases. En el modulo de la propia aplicacion lo genera automaticamente,
pero **las dependencias externas no se escanean salvo que esten indexadas**.

Nuestras librerias contienen beans que deben descubrirse solos:

| Libreria            | Beans                                                  |
|---------------------|--------------------------------------------------------|
| `lib-http`          | `CorrelationContext`, dos filtros JAX-RS                |
| `lib-exceptions`    | `ApplicationExceptionMapper`, `UnhandledExceptionMapper`|
| `lib-observability` | `MdcEnrichmentFilter`, `ObservedInterceptor`            |
| `lib-windows-registry` | `InMemoryWindowsRegistryReader`                      |

Hay tres formas de conseguirlo.

## Opciones

| Opcion | Mecanismo | Valoracion |
|--------|-----------|------------|
| A | `META-INF/beans.xml` vacio en cada libreria | Funciona, pero obliga a Quarkus a escanear el jar en cada build y no aporta metadatos |
| B | `quarkus.index-dependency.*` en el `application.properties` de cada app | Traslada el problema al consumidor: cada aplicacion tiene que saber que librerias indexar, y se olvida al anadir la septima |
| C | Indice Jandex generado al construir la libreria | El jar llega autodescriptivo; el consumidor no hace nada |

## Decision

**Opcion C.** `io.smallrye:jandex-maven-plugin` se aplica en `libs/pom.xml`,
asi que toda libreria presente y futura genera `META-INF/jandex.idx`
automaticamente.

Se aplica a las siete librerias aunque tres no tengan beans: la uniformidad
evita el fallo de "anadi un bean y no se descubre", que es dificil de
diagnosticar. El coste es un fichero de unos pocos KB.

## Consecuencias

- Anadir un bean a una libreria no requiere tocar ninguna aplicacion.
- El arranque es mas rapido: el indice ya existe, no hay que escanear.
- Si alguien crea una libreria nueva con `scripts/new-module.sh`, hereda el
  plugin sin saber que existe.
- Dependencia de terceros (no indexada) que necesite beans: para ese caso si
  hay que recurrir a `quarkus.index-dependency.*` en la aplicacion.
