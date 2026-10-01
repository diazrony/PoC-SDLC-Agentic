package com.example.monorepo.windows.registry;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementacion dummy en memoria.
 *
 * <p>En produccion se sustituiria por un adaptador nativo (por ejemplo JNA
 * sobre Advapi32). Para la PoC basta con demostrar el patron: la aplicacion
 * inyecta {@link WindowsRegistryReader} y desconoce la tecnologia subyacente.</p>
 */
@ApplicationScoped
public class InMemoryWindowsRegistryReader implements WindowsRegistryReader {

    private final Map<String, String> values = new ConcurrentHashMap<>();

    public InMemoryWindowsRegistryReader() {
        seed(RegistryHive.HKEY_LOCAL_MACHINE,
                "SOFTWARE\\Example\\Platform", "InstallPath", "C:\\Program Files\\Example");
        seed(RegistryHive.HKEY_LOCAL_MACHINE,
                "SOFTWARE\\Example\\Platform", "Environment", "LOCAL");
        seed(RegistryHive.HKEY_LOCAL_MACHINE,
                "SOFTWARE\\Example\\Platform", "MaxRetries", "3");
    }

    public final void seed(RegistryHive hive, String key, String valueName, String value) {
        values.put(path(hive, key, valueName), value);
    }

    @Override
    public Optional<String> readString(RegistryHive hive, String key, String valueName) {
        return Optional.ofNullable(values.get(path(hive, key, valueName)));
    }

    @Override
    public Optional<Integer> readInteger(RegistryHive hive, String key, String valueName) {
        return readString(hive, key, valueName).map(raw -> {
            try {
                return Integer.valueOf(raw);
            } catch (NumberFormatException cause) {
                throw new WindowsRegistryException(
                        "El valor %s de %s no es numerico".formatted(valueName, key), cause);
            }
        });
    }

    @Override
    public boolean exists(RegistryHive hive, String key) {
        String prefix = hive.name() + "\\" + key + "\\";
        return values.keySet().stream().anyMatch(entry -> entry.startsWith(prefix));
    }

    private String path(RegistryHive hive, String key, String valueName) {
        return hive.name() + "\\" + key + "\\" + valueName;
    }
}
