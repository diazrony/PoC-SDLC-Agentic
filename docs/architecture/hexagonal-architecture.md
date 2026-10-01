# Arquitectura hexagonal: convenciones del monorepo

Todas las aplicaciones usan la misma estructura de paquetes. No es
burocracia: es lo que permite que un desarrollador cambie de equipo y sepa
leer el codigo el primer dia.

```text
src/main/java/com/example/<app>/
|
+-- domain/                      Java puro. Cero frameworks.
|   +-- model/                   Agregados y Value Objects
|   +-- service/                 Reglas que no caben en un agregado
|   +-- exception/               Errores de negocio y catalogo de codigos
|
+-- application/                 Casos de uso. Tampoco lleva frameworks.
|   +-- port/
|   |   +-- in/                  Lo que el exterior puede pedir
|   |   +-- out/                 Lo que la aplicacion necesita del exterior
|   +-- service/                 Implementacion de los puertos de entrada
|   +-- dto/                     Contratos de entrada y salida
|   +-- mapper/                  Dominio -> DTO
|
+-- infrastructure/              Todo lo que es tecnologia
    +-- adapter/
    |   +-- in/rest/             JAX-RS
    |   +-- out/persistence/     Implementa los puertos de persistencia
    |   +-- out/messaging/       Implementa el puerto de eventos
    +-- persistence/
    |   +-- entity/              @Entity
    |   +-- repository/          Panache
    |   +-- mapper/              Dominio <-> Entidad
    +-- config/                  CDI, @ConfigMapping, arranque
    +-- health/                  Health checks (solo donde aplica)
```

## Por que `application` no lleva anotaciones

`XService` es una clase normal con un constructor. Se publica como bean desde
`infrastructure/config/XBeanConfiguration` con un metodo `@Produces`.

Cuesta cinco lineas de configuracion por aplicacion. A cambio:

- el caso de uso se prueba con `new XService(...)`, sin arrancar Quarkus
  (ver `XServiceTest`: tarda milisegundos);
- cambiar de Quarkus a cualquier otra cosa afecta a `infrastructure`, no a la
  logica;
- la regla ArchUnit `applicationIsFrameworkFree` es verificable y no una
  recomendacion en una wiki.

CDI expone automaticamente todos los tipos del bean producido, asi que
`GetXUseCase` y `CreateXUseCase` quedan inyectables sin declarar nada mas.

Si un equipo prefiere anotar el servicio con `@ApplicationScoped`, es una
decision legitima: basta con relajar esa regla ArchUnit. Lo que no es
legitimo es tener la regla y saltarsela.

## Nomenclatura obligatoria

Las reglas ArchUnit dependen de estos sufijos:

| Sufijo               | Paquete esperado                             |
|----------------------|----------------------------------------------|
| `Resource`           | `infrastructure.adapter.in.rest`             |
| `Entity`             | `infrastructure.persistence.entity`          |
| `Port` / `UseCase`   | `application.port.out` / `application.port.in` |
| `Adapter`            | `infrastructure.adapter.out.*`               |

Cualquier clase en `infrastructure.adapter.out` debe implementar al menos un
puerto de `application.port.out`. Un adaptador que no implementa un puerto no
es un adaptador: es codigo suelto en la capa equivocada.
