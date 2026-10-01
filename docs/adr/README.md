# Architecture Decision Records

Un ADR captura una decision, su contexto y sus consecuencias. En un monorepo
son especialmente utiles: las decisiones afectan a varios equipos a la vez y
la discusion se repite cada seis meses si no esta escrita.

Formato: [MADR](https://adr.github.io/madr/) simplificado.

| ADR                                                   | Decision                                     | Estado    |
|-------------------------------------------------------|----------------------------------------------|-----------|
| [0001](0001-monorepo-maven-multimodulo.md)            | Monorepo con Maven multi-modulo              | Aceptada  |
| [0002](0002-arquitectura-hexagonal.md)                | Arquitectura hexagonal en todas las apps     | Aceptada  |
| [0003](0003-separacion-apps-libs.md)                  | Separacion estricta entre apps y libs        | Aceptada  |
| [0004](0004-jandex-para-descubrimiento-cdi.md)        | Jandex en lugar de beans.xml                 | Aceptada  |
| [0005](0005-problem-details-para-errores.md)          | Problem Details (RFC 7807) como contrato     | Aceptada  |
| [0006](0006-contratos-de-eventos-compartidos.md)      | Cuando compartir un contrato de evento       | Aceptada  |
| [0007](0007-version-unica-del-monorepo.md)            | Version unica (lockstep) para todo el repo   | Aceptada  |
