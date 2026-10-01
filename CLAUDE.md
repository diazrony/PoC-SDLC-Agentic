# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Qué es este repositorio

PoC de monorepositorio empresarial **Maven + Quarkus 3.20.6 (LTS) + Java 21**: 6 aplicaciones
desplegables (`apps/`) + 7 librerías internas (`libs/`), con arquitectura hexagonal y fronteras
arquitectónicas verificadas automáticamente. El código de negocio es deliberadamente trivial —
lo que se demuestra es la **organización**. Cualquier cambio debe preservar esa cualidad
didáctica: la estructura y los comentarios explicativos son el producto, no el andamio.

Documentación de referencia: `README.md` (completo), `docs/architecture/`, `docs/adr/` (7 ADR).

## Comandos

```bash
./mvnw clean verify                              # todo: 13 módulos, ~127 tests + JaCoCo
./mvnw -pl apps/authorization -am verify         # una app + sus librerías
./mvnw -pl libs/exceptions -am -amd verify       # una librería + todo lo que la consume
./mvnw -pl apps/authorization -am quarkus:dev    # dev mode con hot reload
./mvnw test -Dtest=ArchitectureTest              # solo reglas de arquitectura
./mvnw -pl apps/transaction test -Dtest=TransactionServiceTest#metodo   # un test suelto
scripts/verify-architecture.sh                   # POM grep + Enforcer + ArchUnit
scripts/affected-projects.sh --base origin/main --format maven   # -> "-pl ... -am -amd"
scripts/build-affected.sh --base origin/main verify
scripts/new-module.sh app billing com.example.billing            # scaffolding hexagonal
```

Las tres banderas del Reactor: `-pl` módulos semilla, `-am` sus dependencias (upstream),
`-amd` sus dependientes (downstream). Trabajar siempre con `-pl ... -am` en vez de construir
el reactor completo mientras se itera.

Solo las `apps` llevan `quarkus-maven-plugin`; las `libs` son JAR planos indexados con Jandex.
El Enforcer rechaza el build con cualquier Java distinto de 21 — usar `./mvnw`, nunca un `mvn`
del sistema.

`scripts/` requiere Bash (Git Bash en Windows). `affected-projects.ps1` es el equivalente
PowerShell del cálculo de afectados. Si la ref base no existe, el script avisa y construye todo
(el repositorio actual aún no tiene commits en `main`).

## Las cuatro reglas que definen el repositorio

```
apps -> libs   PERMITIDO        apps -> apps   PROHIBIDO
libs -> libs   PERMITIDO        libs -> apps   PROHIBIDO
```

Y dentro de cada app: `infrastructure -> application -> domain`, nunca al revés.
`domain` y `application` son **Java puro**: ni Quarkus, ni JAX-RS, ni JPA, ni CDI, ni Jackson.

Esto no es convención: está verificado en tres niveles simultáneos, y romper cualquiera rompe
el build.

| Nivel  | Mecanismo      | Dónde vive                                             |
|--------|----------------|--------------------------------------------------------|
| POM    | Maven Enforcer | `apps/pom.xml`, `libs/pom.xml` (`bannedDependencies`)  |
| Código | ArchUnit       | `libs/testing` + un `ArchitectureTest` por app         |
| Review | CODEOWNERS     | `.github/CODEOWNERS` (propiedad por ruta)              |

`lib-testing` y ArchUnit solo en scope `test`; el Enforcer de `apps/pom.xml` lo verifica para
que no se filtren al runtime del contenedor.

## Estructura interna de una aplicación

```
src/main/java/com/example/<app>/
  domain/         model/ (agregados, VO)  service/ (políticas)  exception/ (+ enum ErrorCode)
  application/    port/in/  port/out/  service/  dto/  mapper/
  infrastructure/ adapter/in/rest/  adapter/out/{persistence,messaging}/
                  persistence/{entity,repository,mapper}/  config/
```

Las reglas ArchUnit dependen de los **sufijos**: `Resource` → `infrastructure.adapter.in.rest`,
`Entity` → `infrastructure.persistence.entity`, `UseCase` → `application.port.in`,
`Port` → `application.port.out`, `Adapter` → `infrastructure.adapter.out.*` (y todo adaptador de
salida debe implementar al menos un puerto de salida). Renombrar fuera de ese esquema rompe los
tests de arquitectura.

El patrón clave, que se repite en las 6 apps: **`XService` no lleva anotaciones**. Es una clase
con constructor que se publica como bean desde `infrastructure/config/XBeanConfiguration` con
métodos `@Produces`. CDI expone automáticamente todos sus tipos, así que los puertos de entrada
quedan inyectables en el `Resource` sin declarar nada más. El coste son cinco líneas de
configuración; el beneficio es `XServiceTest`, que prueba el caso de uso con un `new` y puertos
implementados con lambdas, sin arrancar Quarkus.

Tres tests por aplicación: `XServiceTest` (hexágono puro, milisegundos), `ArchitectureTest`
(las 8 reglas, sin Quarkus), `XResourceTest` (`@QuarkusTest` + RestAssured, extremo a extremo).

## Lo que una app recibe sin escribir código

`apps/pom.xml` declara **una sola vez** el stack Quarkus (rest, jackson, panache, h2, health) y
`lib-exceptions` + `lib-http` + `lib-observability`. Por eso el `pom.xml` de cada app cabe en
veinte líneas: solo declara lo que la diferencia (`lib-events` en `transaction` y `retiros`,
`lib-windows-registry` en `utils` y `configuration`).

Gracias al índice Jandex de las librerías (ADR 0004), estos componentes se autorregistran:

- Problem Details RFC 7807 en todos los errores — **ninguna app escribe un `ExceptionMapper`**
- `X-Correlation-Id` / request id (filtros JAX-RS de `lib-http`)
- `traceId` en todos los logs (filtro MDC de `lib-observability`)
- `@Observed("nombre")` para instrumentar un método
- `WindowsRegistryReader` inyectable

Al añadir una librería nueva, declarar su versión en el `dependencyManagement` del POM raíz.
Antes de crearla: ¿la necesitan dos o más apps **hoy**, es técnica y no de negocio, es estable,
y no cabe en una librería existente? (ADR 0003, ADR 0006).

## Al añadir una aplicación nueva

`scripts/new-module.sh app <nombre> <paquete>` crea el esqueleto y la registra en `apps/pom.xml`.
Los cuatro pasos que el script **no** hace y hay que completar a mano:

1. Añadir el paquete a `MonorepoPackages.APPLICATIONS` en `libs/testing` — sin esto, la regla
   `apps → apps` ignora la nueva aplicación.
2. Asignar propietario en `.github/CODEOWNERS`.
3. Crear `infrastructure/kubernetes/base/<nombre>.yaml`.
4. Añadirla a la matriz de `.github/workflows/build.yml`.

Puertos asignados: authorization 8081, transaction 8082, utils 8083, configuration 8084,
health-clinic 8085, retiros 8086. Toda la configuración se resuelve desde variables de entorno
con un valor por defecto apto para desarrollo local (`${AUTHORIZATION_HTTP_PORT:8081}`); los
catálogos de `ErrorCode` son **locales a cada app** — se comparte el contrato, no los valores.

## Convenciones de escritura

- Versión única del monorepo (`1.0.0-SNAPSHOT`, ADR 0007): nunca versionar un módulo aparte.
  Ninguna versión en los POM hijos — todo vive en el `dependencyManagement` y `pluginManagement`
  del POM raíz.
- **Documentación, Javadoc y comentarios en español.** Los comentarios de código, POM y scripts
  van sin acentos (`configuracion`, `libreria`); los `.md` sí los llevan.
- Los comentarios de bloque en los POM y los scripts explican el *por qué* de la decisión, no el
  *qué*. Al editarlos, mantener ese registro.
- `.editorconfig`: 4 espacios Java, 2 para XML/YAML/JSON/shell, LF, máx. 120 columnas.
