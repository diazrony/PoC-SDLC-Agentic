# 0005. Problem Details (RFC 7807) como contrato de error

- Estado: Aceptada
- Fecha: 2026-01-22

## Contexto

Cada servicio devolvia sus errores a su manera: unos `{"error": "..."}`,
otros un texto plano, otros un stacktrace. Los consumidores tenian que
programar un parser por servicio.

## Decision

Formato unico basado en RFC 7807, actualizado por RFC 9457, con
`Content-Type: application/problem+json`:

```json
{
  "type": "https://errors.example.com/auth-0001",
  "title": "Authorization no encontrado",
  "status": 404,
  "detail": "No existe Authorization con identificador A-9999",
  "instance": "/api/authorizations/A-9999",
  "traceId": "0f1c...",
  "code": "AUTH-0001",
  "timestamp": "2026-01-22T10:15:30Z"
}
```

`type`, `title`, `status`, `detail` e `instance` son del estandar. `traceId`,
`code`, `timestamp` y `errors` son extensiones, que el propio RFC permite.

## Implementacion

La libreria se divide en tres paquetes con reglas distintas:

```text
exceptions           Java puro.      El DOMINIO puede extenderlo.
exceptions.problem   Modelo.         Representacion del error.
exceptions.rest      JAX-RS.         Solo infraestructura.
```

Esa separacion es lo que permite que `AuthorizationNotFoundException` viva en
`domain/exception` sin que el dominio sepa que existe HTTP. La traduccion
categoria -> status ocurre en un unico sitio, `HttpStatusResolver`:

```text
VALIDATION -> 400    NOT_FOUND -> 404    BUSINESS -> 422
CONFLICT   -> 409    TECHNICAL -> 500
```

## Consecuencias

- Las seis aplicaciones devuelven el mismo contrato sin escribir un solo
  `ExceptionMapper`: los de la libreria se autoregistran via Jandex.
- `UnhandledExceptionMapper` garantiza que ningun stacktrace llega al cliente.
- El `traceId` del Problem Detail es el mismo que aparece en los logs y en la
  cabecera `X-Correlation-Id`, asi que un ticket de soporte se investiga con
  una sola busqueda.
- Los codigos (`AUTH-0001`) son **locales a cada aplicacion**: compartir el
  catalogo acoplaria los servicios. Lo compartido es la interfaz `ErrorCode`.
