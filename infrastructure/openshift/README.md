# infrastructure/openshift

OpenShift reutiliza los `Deployment`, `Service` y `ConfigMap` de
`infrastructure/kubernetes` y anade dos piezas propias:

| Fichero                            | Para que sirve                                    |
|------------------------------------|---------------------------------------------------|
| `authorization-route.yaml`         | Exposicion externa con terminacion TLS en el edge |
| `authorization-buildconfig.yaml`   | Build dentro del cluster a partir del monorepo    |

Lo importante del `BuildConfig` en un monorepo es `contextDir`: cada build
apunta a la carpeta de **una** aplicacion. Sin eso, cualquier commit
reconstruiria los seis servicios y se perderia la ventaja del CI selectivo.

Se incluye solo el ejemplo de `authorization`; el resto es el mismo fichero
cambiando el nombre y el `contextDir`.
