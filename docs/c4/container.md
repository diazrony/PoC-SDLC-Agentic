# C4 - Nivel 2: Contenedores

```mermaid
C4Container
    title Contenedores desplegados desde el monorepo

    Person(cliente, "Cliente")

    Container_Boundary(plataforma, "Plataforma") {
        Container(auth, "authorization", "Quarkus 3.20 / Java 21", "Imagen propia, 2 replicas, puerto 8081")
        Container(trx, "transaction", "Quarkus 3.20 / Java 21", "Imagen propia, 2 replicas, puerto 8082")
        Container(utl, "utils", "Quarkus 3.20 / Java 21", "Imagen propia, puerto 8083")
        Container(cfg, "configuration", "Quarkus 3.20 / Java 21", "Imagen propia, puerto 8084")
        Container(hcl, "health-clinic", "Quarkus 3.20 / Java 21", "Imagen propia, puerto 8085")
        Container(ret, "retiros", "Quarkus 3.20 / Java 21", "Imagen propia, puerto 8086")

        ContainerDb(dbauth, "DB authorization", "H2 / PostgreSQL", "Esquema propio")
        ContainerDb(dbtrx, "DB transaction", "H2 / PostgreSQL", "Esquema propio")
        ContainerDb(dbret, "DB retiros", "H2 / PostgreSQL", "Esquema propio")
    }

    System_Ext(broker, "Message Broker")

    Rel(cliente, auth, "GET /api/authorizations/{id}", "HTTPS")
    Rel(cliente, trx, "GET /api/transactions/{id}", "HTTPS")
    Rel(cliente, ret, "GET /api/retiros/{id}", "HTTPS")

    Rel(auth, dbauth, "JDBC")
    Rel(trx, dbtrx, "JDBC")
    Rel(ret, dbret, "JDBC")

    Rel(trx, broker, "transaction.created.v1")
    Rel(ret, broker, "withdrawal.requested.v1")
```

## Lo que este diagrama demuestra

Un contenedor por aplicacion, una base de datos por aplicacion, un ciclo de
despliegue por aplicacion:

```text
MONOREPO
   |
   +-- authorization -> JAR -> Container -> Deployment (2 replicas)
   +-- transaction   -> JAR -> Container -> Deployment (2 replicas)
   +-- utils         -> JAR -> Container -> Deployment
   +-- configuration -> JAR -> Container -> Deployment
   +-- health-clinic -> JAR -> Container -> Deployment
   +-- retiros       -> JAR -> Container -> Deployment
```

**Monorepo NO significa monolito.** Lo que se comparte es el repositorio, el
build y las librerias tecnicas. Lo que no se comparte: el proceso, la base de
datos, la configuracion, el ciclo de vida y el escalado.

## Librerias: componentes, no contenedores

`libs/*` no aparece en este diagrama porque ninguna libreria es un
contenedor. Son componentes (nivel 3 de C4) que acaban empaquetados dentro de
cada imagen:

```text
authorization.jar
  +-- lib-exceptions.jar
  +-- lib-http.jar
  +-- lib-observability.jar
  +-- lib-common-core.jar
```

Confundir una libreria compartida con un servicio compartido es el error
clasico al dibujar la arquitectura de un monorepo.
