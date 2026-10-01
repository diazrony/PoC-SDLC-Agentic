# libs/ : librerias internas compartidas

Codigo **tecnico y transversal** que varias aplicaciones necesitan y que no
tendria sentido duplicar. No se despliegan: se empaquetan dentro de cada
aplicacion que las usa.

| Libreria           | Responsabilidad                                        | Depende de            |
|--------------------|--------------------------------------------------------|-----------------------|
| `common-core`      | Java puro: `TimeProvider`, `Ids`, `Preconditions`      | nada                  |
| `exceptions`       | Jerarquia de errores + Problem Details (RFC 7807)      | common-core, http     |
| `http`             | Cabeceras, correlation id, filtros JAX-RS              | common-core           |
| `observability`    | MDC, logging estructurado, interceptor `@Observed`     | common-core, http     |
| `windows-registry` | Lectura del Registro de Windows tras una interfaz      | common-core           |
| `events`           | Contratos de eventos de integracion                    | common-core           |
| `testing`          | Reglas ArchUnit, builders y fixtures (**scope test**)  | common-core           |

## Reglas que cumple toda libreria

1. **Nunca depende de una aplicacion.** Lo verifica el Enforcer de
   `libs/pom.xml`.
2. **No contiene logica de negocio.** Si una libreria sabe que es un
   "retiro", esta mal ubicada.
3. **Se indexa con Jandex** para que Quarkus descubra sus beans CDI sin que
   el consumidor configure nada ([ADR 0004](../docs/adr/0004-jandex-para-descubrimiento-cdi.md)).
4. **Compila contra APIs Jakarta**, no contra extensiones Quarkus completas.
   La extension la aporta la aplicacion.
5. **Hereda su version del POM raiz.** Ningun `<version>` en los POM hijos.

## Antes de crear una libreria nueva

Preguntas en orden:

1. ¿Lo necesitan **dos o mas** aplicaciones hoy, no en teoria?
2. ¿Es **tecnico**, no de negocio?
3. ¿Es **estable**? Una libreria que cambia cada sprint convierte cada cambio
   en un despliegue coordinado de seis servicios.
4. ¿Cabe en una libreria existente?

Si las cuatro respuestas son afirmativas:

```bash
scripts/new-module.sh lib caching
```

Y despues: declarar la version en el `dependencyManagement` del POM raiz y
asignar propietario en `.github/CODEOWNERS`.
