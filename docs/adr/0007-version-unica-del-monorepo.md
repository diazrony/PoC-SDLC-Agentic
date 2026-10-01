# 0007. Version unica (lockstep) para todo el monorepo

- Estado: Aceptada
- Fecha: 2026-02-02

## Contexto

Trece modulos en un repositorio. Hay dos modelos de versionado posibles:
una version compartida, o una version independiente por modulo.

## Decision

Una sola version, declarada en el POM raiz y heredada por todos los modulos:
`1.0.0-SNAPSHOT`. El release se hace con `versions:set` sobre el reactor
completo.

## Justificacion

- Las librerias internas **no se publican fuera del monorepo**. Nadie externo
  consume `lib-exceptions`, asi que su numero de version no comunica nada.
- Dentro del reactor, las dependencias se resuelven con `${project.version}`:
  no hay posibilidad de que una aplicacion use una version antigua de una
  libreria. El problema del "diamante de versiones" simplemente no existe.
- El coste operativo del versionado independiente (release por modulo, matriz
  de compatibilidad, resolucion de conflictos) no se justifica con cuatro
  equipos.

Que una version comun **no** implica: despliegue comun. Las seis aplicaciones
se siguen desplegando por separado y en momentos distintos. La version es
solo una etiqueta de la fotografia del codigo.

## Consecuencias

- El `pom.xml` de cada modulo es mas corto: no declara `<version>`.
- Un release publica seis imagenes, aunque solo haya cambiado una. Si eso
  llega a molestar, el pipeline puede filtrar por aplicaciones afectadas
  desde el ultimo tag.
- Si en el futuro hay que publicar una libreria fuera de la organizacion,
  habra que revisar esta decision: las opciones son Maven CI Friendly
  Versions (`revision`/`sha1`) o extraer esa libreria a su propio ciclo.
