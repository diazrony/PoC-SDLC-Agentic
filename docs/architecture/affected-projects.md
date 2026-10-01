# Affected Projects

## El problema

En un monorepo, construir todo en cada Pull Request no escala: el tiempo de
CI crece con el tamanio del repositorio en lugar de con el tamanio del cambio.
La practica estandar (monorepo.tools) es construir solo lo **afectado**.

## El algoritmo

```text
Git Diff
   |
   v
Detect Changed Projects        scripts/affected-projects.sh
   |
   v
Calculate Dependency Graph     Maven Reactor
   |
   v
Determine Affected Projects    -am (upstream) + -amd (downstream)
   |
   v
Build / Test only affected projects
```

La division de responsabilidades es intencionada:

- **git** sabe que ficheros han cambiado. Maven no.
- **Maven** conoce el grafo de dependencias entre modulos. git no.

El script no reimplementa el grafo: traduce ficheros a modulos semilla y deja
que el Reactor calcule el cierre transitivo.

## Las tres banderas

| Bandera | Significado                      | Que anade al conjunto           |
|---------|----------------------------------|---------------------------------|
| `-pl`   | `--projects`: modulos semilla    | Nada, es el punto de partida    |
| `-am`   | `--also-make`                    | Las **dependencias** (upstream) |
| `-amd`  | `--also-make-dependents`         | Los **dependientes** (downstream)|

```bash
# Solo authorization y lo que necesita para compilar
./mvnw -pl apps/authorization -am verify

# exceptions y todo lo que lo consume
./mvnw -pl libs/exceptions -amd verify

# El conjunto completo: imprescindible en CI
./mvnw -pl libs/exceptions -am -amd verify
```

## Ejemplo trabajado

Cambia `libs/exceptions`. El grafo dice:

```text
authorization -> exceptions
transaction   -> exceptions
utils         -> exceptions
configuration -> exceptions
health-clinic -> exceptions
retiros       -> exceptions
exceptions    -> http -> common-core
```

Resultado del calculo:

```text
Semilla (-pl)      : libs/exceptions
Upstream (-am)     : libs/common-core, libs/http
Downstream (-amd)  : las seis aplicaciones
```

Comprobacion real en este repositorio:

```bash
$ ./mvnw -pl libs/exceptions -am -amd validate -q -o
# construye: common-core, http, exceptions + las 6 apps
```

Si en cambio se toca `libs/events`:

```text
Semilla            : libs/events
Upstream           : libs/common-core
Downstream         : apps/transaction, apps/retiros
```

Cuatro modulos en vez de trece. Esa diferencia es el objetivo de todo el
mecanismo.

## Cambios que fuerzan el build completo

Hay ficheros cuyo cambio puede afectar a cualquier modulo y que el grafo de
Maven no modela. El script los trata como "reconstruir todo":

```text
pom.xml            apps/pom.xml       libs/pom.xml
.mvn/              mvnw  mvnw.cmd
scripts/           .github/workflows/
```

Ser conservador aqui es correcto: un falso positivo cuesta minutos de CI, un
falso negativo deja pasar una regresion.

## Uso

```bash
# Que esta afectado
scripts/affected-projects.sh --base origin/main --format list

# Los argumentos para Maven
scripts/affected-projects.sh --base origin/main --format maven
# -> -pl libs/exceptions -am -amd

# Construirlo
scripts/build-affected.sh --base origin/main verify
```

En Windows sin Git Bash: `scripts/affected-projects.ps1 -Format maven`.

## Red de seguridad

El calculo de afectados es una optimizacion, no una garantia. Por eso
`build.yml` ejecuta `./mvnw clean verify` completo en cada push a `main` y
cada noche. Si el calculo se equivoca, se descubre en horas y no en semanas.

## Cuando esto deje de bastar

Para trece modulos, Maven Reactor sobra. Si el monorepo crece a cincuenta o
mas, los siguientes pasos naturales son cache de build remoto y ejecucion
distribuida (Develocity, o migrar el build a Gradle/Bazel/Nx). La estructura
de carpetas y las fronteras definidas aqui no cambian con esa migracion: solo
cambia el motor de build.
