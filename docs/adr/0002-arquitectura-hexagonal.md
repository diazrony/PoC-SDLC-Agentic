# 0002. Arquitectura hexagonal en todas las aplicaciones

- Estado: Aceptada
- Fecha: 2026-01-15

## Contexto

En el modelo anterior, los recursos JAX-RS inyectaban repositorios Panache y
devolvian entidades JPA directamente. Consecuencias observadas: cambiar una
columna rompia el contrato publico de la API, y probar una regla de negocio
requeria arrancar la aplicacion y una base de datos.

## Decision

Las seis aplicaciones usan Ports & Adapters con tres capas: `domain`,
`application` e `infrastructure`. Las dependencias apuntan hacia adentro.

`domain` y `application` son **Java puro**. El servicio de aplicacion se
publica como bean CDI mediante un metodo `@Produces` en
`infrastructure/config`.

## Consecuencias

Positivas:

- Los casos de uso se prueban sin framework (`XServiceTest` usa lambdas como
  implementacion de los puertos de salida).
- El modelo de persistencia puede evolucionar sin tocar el dominio.
- La estructura es identica en las seis aplicaciones.

Negativas:

- Mas clases: hay un mapper dominio/entidad y otro dominio/DTO.
- Cinco lineas de configuracion CDI por aplicacion.

Se acepta el coste: aparece una vez por aplicacion y se amortiza en cada
cambio posterior.

## Verificacion

`ArchitectureTest` en cada aplicacion ejecuta ocho reglas de
`libs/testing`. Una violacion rompe el build igual que un test funcional.
