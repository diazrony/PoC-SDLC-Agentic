package com.example.monorepo.windows.registry;

import java.util.Optional;

/**
 * Puerto de lectura del Registro de Windows.
 *
 * <p>Las aplicaciones dependen SIEMPRE de esta interfaz, nunca de una
 * implementacion concreta. Eso permite:</p>
 * <ul>
 *   <li>sustituir la implementacion real (JNA, WinRegistry, proceso externo)
 *       sin tocar las aplicaciones;</li>
 *   <li>ejecutar tests y pipelines en Linux;</li>
 *   <li>concentrar en un unico modulo el codigo dependiente de plataforma.</li>
 * </ul>
 */
public interface WindowsRegistryReader {

    Optional<String> readString(RegistryHive hive, String key, String valueName);

    Optional<Integer> readInteger(RegistryHive hive, String key, String valueName);

    boolean exists(RegistryHive hive, String key);
}
