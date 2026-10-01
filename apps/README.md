# apps/ : aplicaciones desplegables

Cada carpeta es un **microservicio Quarkus independiente**: su propio
artefacto, su propia imagen, su propia base de datos, su propio ciclo de
despliegue y su propio escalado.

| Aplicacion      | Paquete Java                 | API base              | Puerto | Librerias adicionales |
|-----------------|------------------------------|-----------------------|--------|-----------------------|
| `authorization` | `com.example.authorization`  | `/api/authorizations` | 8081   | -                     |
| `transaction`   | `com.example.transaction`    | `/api/transactions`   | 8082   | `lib-events`          |
| `utils`         | `com.example.utils`          | `/api/utils`          | 8083   | `lib-windows-registry`|
| `configuration` | `com.example.configuration`  | `/api/configurations` | 8084   | `lib-windows-registry`|
| `health-clinic` | `com.example.healthclinic`   | `/api/patients`       | 8085   | -                     |
| `retiros`       | `com.example.retiros`        | `/api/retiros`        | 8086   | `lib-events`          |

Las tres librerias comunes (`exceptions`, `http`, `observability`) estan
declaradas una sola vez en `apps/pom.xml` y las heredan las seis.

## La regla que no se negocia

```text
apps -> apps   PROHIBIDO
```

No hay ni una dependencia entre estas seis carpetas, y hay tres mecanismos
vigilandolo ([ADR 0003](../docs/adr/0003-separacion-apps-libs.md)).
Si dos aplicaciones necesitan colaborar: evento, llamada HTTP, o extraer lo
comun a una libreria. Nunca una dependencia Maven.

## Estructura interna

Las seis son identicas por dentro. Ver
[docs/architecture/hexagonal-architecture.md](../docs/architecture/hexagonal-architecture.md).

## Anadir una aplicacion

```bash
scripts/new-module.sh app billing com.example.billing
```

Despues:

1. Anadir el paquete a `MonorepoPackages.APPLICATIONS` en `libs/testing`,
   para que las reglas `apps -> apps` la tengan en cuenta.
2. Asignar propietario en `.github/CODEOWNERS`.
3. Crear el manifiesto en `infrastructure/kubernetes/base/`.
4. Anadirla a la matriz de `.github/workflows/build.yml`.
