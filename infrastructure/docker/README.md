# infrastructure/docker

Entorno local con las seis aplicaciones corriendo a la vez.

```bash
# 1. Construir los artefactos (todo el reactor)
./mvnw clean package -DskipTests

# 2. Levantar los seis contenedores
docker compose -f infrastructure/docker/docker-compose.yml up --build

# 3. Probar
curl http://localhost:8081/api/authorizations/A-0001
curl http://localhost:8082/api/transactions/T-0001
curl http://localhost:8086/api/retiros/W-0001
```

Para construir una sola aplicacion:

```bash
./mvnw -pl apps/transaction -am package -DskipTests
docker build -f apps/transaction/src/main/docker/Dockerfile.jvm \
             -t example/transaction:local apps/transaction
```

| Aplicacion    | Puerto local | Health                                |
|---------------|--------------|---------------------------------------|
| authorization | 8081         | http://localhost:8081/q/health        |
| transaction   | 8082         | http://localhost:8082/q/health        |
| utils         | 8083         | http://localhost:8083/q/health        |
| configuration | 8084         | http://localhost:8084/q/health        |
| health-clinic | 8085         | http://localhost:8085/q/health        |
| retiros       | 8086         | http://localhost:8086/q/health        |
