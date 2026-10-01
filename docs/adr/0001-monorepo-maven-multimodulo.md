# 0001. Monorepo con Maven multi-modulo

- Estado: Aceptada
- Fecha: 2026-01-15

## Contexto

Seis microservicios Quarkus desarrollados por cuatro equipos. Con un
repositorio por servicio aparecieron tres problemas recurrentes:

1. El codigo transversal (manejo de errores, correlation id, logging) se
   copiaba y pegaba, y divergia.
2. Un cambio en una libreria compartida obligaba a publicar una version,
   abrir seis Pull Requests y esperar a que cada equipo la adoptara.
3. No habia forma barata de hacer un cambio atomico que tocara varios
   servicios.

## Decision

Un unico repositorio con Maven Reactor, estructurado en `apps/` y `libs/`,
con un POM raiz que actua a la vez de **parent** y de **aggregator**.

## Consecuencias

Positivas:

- Un cambio en una libreria y sus consumidores cabe en un solo Pull Request,
  y CI lo valida en conjunto.
- Una unica version de Java, de Quarkus y de cada plugin, centralizada en el
  POM raiz.
- Las fronteras entre modulos se pueden verificar automaticamente, cosa
  imposible repartidos en seis repositorios.
- Inventario de dependencias y politica de seguridad unicos.

Negativas, y como se mitigan:

| Riesgo                              | Mitigacion                                   |
|-------------------------------------|----------------------------------------------|
| CI lento                            | Affected Projects (ADR en `docs/architecture/affected-projects.md`) |
| Acoplamiento accidental entre apps  | Enforcer + ArchUnit + CODEOWNERS             |
| Confusion monorepo/monolito         | Un artefacto y una imagen por aplicacion     |
| Propiedad del codigo difusa         | `.github/CODEOWNERS` por ruta                |

## Alternativas descartadas

- **Un repositorio por servicio + libreria publicada**: es el punto de
  partida, y es justo lo que genero el problema.
- **Gradle con build cache / Bazel / Nx**: mejor rendimiento a gran escala,
  pero el equipo domina Maven y trece modulos no justifican la curva de
  aprendizaje. Revisable si el repositorio crece de forma significativa.
