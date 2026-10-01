package com.example.utils.infrastructure.health;

import com.example.monorepo.windows.registry.RegistryHive;
import com.example.monorepo.windows.registry.WindowsRegistryReader;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

/**
 * Consumo de {@code lib-windows-registry} desde una aplicacion.
 *
 * <p>La aplicacion depende de la INTERFAZ {@link WindowsRegistryReader}; la
 * implementacion (dummy en memoria en esta PoC, nativa via JNA en produccion)
 * la aporta la libreria. Ese es justo el motivo de extraer una capacidad
 * tecnica dependiente de plataforma a un modulo propio: se escribe una vez,
 * se prueba una vez y las seis aplicaciones la consumen igual.</p>
 *
 * <p>Si el dominio necesitara estos valores, el camino correcto no seria
 * inyectar el reader en el servicio de aplicacion, sino declarar un puerto de
 * salida e implementarlo con un adaptador que use esta libreria.</p>
 */
@Readiness
@ApplicationScoped
public class PlatformRegistryHealthCheck implements HealthCheck {

    private static final String PLATFORM_KEY = "SOFTWARE\\Example\\Platform";

    private final WindowsRegistryReader registryReader;

    @Inject
    public PlatformRegistryHealthCheck(WindowsRegistryReader registryReader) {
        this.registryReader = registryReader;
    }

    @Override
    public HealthCheckResponse call() {
        boolean present = registryReader.exists(RegistryHive.HKEY_LOCAL_MACHINE, PLATFORM_KEY);
        return HealthCheckResponse.named("platform-registry")
                .status(present)
                .withData("installPath", registryReader
                        .readString(RegistryHive.HKEY_LOCAL_MACHINE, PLATFORM_KEY, "InstallPath")
                        .orElse("desconocido"))
                .withData("maxRetries", registryReader
                        .readInteger(RegistryHive.HKEY_LOCAL_MACHINE, PLATFORM_KEY, "MaxRetries")
                        .orElse(0))
                .build();
    }
}
