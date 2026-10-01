package com.example.configuration.architecture;

import com.example.monorepo.testing.architecture.HexagonalArchitectureRules;
import com.example.monorepo.testing.architecture.MonorepoDependencyRules;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

/**
 * Las reglas de arquitectura se ejecutan como un test mas: una violacion
 * rompe el build igual que un test funcional en rojo.
 *
 * <p>Las reglas estan definidas una sola vez en {@code libs/testing} y se
 * reutilizan en las seis aplicaciones.</p>
 */
class ArchitectureTest {

    private static final String BASE_PACKAGE = "com.example.configuration";

    private static JavaClasses classes;

    @BeforeAll
    static void importProductionClasses() {
        // Se importa el directorio de clases compiladas para no incluir
        // bytecode generado por el framework ni clases de test.
        classes = new ClassFileImporter().importPath(Path.of("target", "classes"));
    }

    @Test
    void lasDependenciasApuntanHaciaElDominio() {
        HexagonalArchitectureRules.dependenciesPointInwards(BASE_PACKAGE).check(classes);
    }

    @Test
    void elDominioNoDependeDeNingunFramework() {
        HexagonalArchitectureRules.domainIsFrameworkFree(BASE_PACKAGE).check(classes);
    }

    @Test
    void laCapaDeAplicacionNoDependeDeNingunFramework() {
        HexagonalArchitectureRules.applicationIsFrameworkFree(BASE_PACKAGE).check(classes);
    }

    @Test
    void dominioYAplicacionIgnoranElTransporteHttp() {
        HexagonalArchitectureRules.innerLayersIgnoreTransportConcerns(BASE_PACKAGE).check(classes);
    }

    @Test
    void losAdaptadoresDeSalidaImplementanUnPuerto() {
        HexagonalArchitectureRules.outboundAdaptersImplementOutputPorts(BASE_PACKAGE).check(classes);
    }

    @Test
    void losRecursosRestVivenEnElAdaptadorDeEntrada() {
        HexagonalArchitectureRules.restResourcesLiveInTheInboundAdapter(BASE_PACKAGE).check(classes);
    }

    @Test
    void lasEntidadesJpaNoSalenDeLaCapaDePersistencia() {
        HexagonalArchitectureRules.jpaEntitiesStayInPersistence(BASE_PACKAGE).check(classes);
    }

    @Test
    void estaAplicacionNoDependeDeNingunaOtraAplicacion() {
        MonorepoDependencyRules.noDependenciesOnOtherApplications(BASE_PACKAGE).check(classes);
    }
}
