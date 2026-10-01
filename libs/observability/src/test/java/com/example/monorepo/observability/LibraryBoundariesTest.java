package com.example.monorepo.observability;

import com.example.monorepo.testing.architecture.MonorepoDependencyRules;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Comprueba la frontera {@code libs -> apps} a nivel de codigo.
 *
 * <p>Se ejecuta desde esta libreria porque su classpath de test incluye
 * {@code lib-common-core}, {@code lib-http} y {@code lib-testing}, de modo
 * que una sola importacion cubre varios modulos compartidos a la vez.</p>
 *
 * <p>Es el complemento del Maven Enforcer de {@code libs/pom.xml}: el
 * Enforcer rechaza la dependencia declarada en el POM, ArchUnit rechaza la
 * referencia en el codigo.</p>
 */
class LibraryBoundariesTest {

    private static JavaClasses sharedLibraries;

    @BeforeAll
    static void importSharedLibraries() {
        sharedLibraries = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.example.monorepo");
    }

    @Test
    void ningunaLibreriaDependeDeUnaAplicacion() {
        MonorepoDependencyRules.librariesDoNotDependOnApplications().check(sharedLibraries);
    }
}
