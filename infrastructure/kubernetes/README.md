# infrastructure/kubernetes

Manifiestos base de las seis aplicaciones.

```bash
# Todo el sistema
kubectl apply -k infrastructure/kubernetes/base

# Una sola aplicacion (despliegue independiente)
kubectl apply -f infrastructure/kubernetes/base/retiros.yaml
```

Puntos a destacar para el equipo:

- **Un Deployment por aplicacion.** Replicas, CPU y memoria se ajustan por
  servicio. `transaction` puede ir a 10 replicas mientras `health-clinic`
  se queda en 1.
- **Un Secret por aplicacion.** Ninguna base de datos se comparte entre
  microservicios; compartir esquema es la forma mas rapida de volver a tener
  un monolito distribuido.
- **Sondas de Quarkus.** `/q/health/started`, `/q/health/live` y
  `/q/health/ready` los aporta la extension `quarkus-smallrye-health`.
  En `utils` y `configuration`, `ready` incluye ademas el chequeo que usa
  `lib-windows-registry`.
