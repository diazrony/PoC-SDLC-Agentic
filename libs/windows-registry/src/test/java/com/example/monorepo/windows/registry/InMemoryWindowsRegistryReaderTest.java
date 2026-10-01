package com.example.monorepo.windows.registry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryWindowsRegistryReaderTest {

    private final InMemoryWindowsRegistryReader reader = new InMemoryWindowsRegistryReader();

    @Test
    void leeUnValorDeCadena() {
        assertEquals("LOCAL", reader.readString(
                RegistryHive.HKEY_LOCAL_MACHINE, "SOFTWARE\\Example\\Platform", "Environment").orElseThrow());
    }

    @Test
    void leeUnValorNumerico() {
        assertEquals(3, reader.readInteger(
                RegistryHive.HKEY_LOCAL_MACHINE, "SOFTWARE\\Example\\Platform", "MaxRetries").orElseThrow());
    }

    @Test
    void detectaClavesExistentesEInexistentes() {
        assertTrue(reader.exists(RegistryHive.HKEY_LOCAL_MACHINE, "SOFTWARE\\Example\\Platform"));
        assertFalse(reader.exists(RegistryHive.HKEY_CURRENT_USER, "SOFTWARE\\Desconocido"));
    }
}
