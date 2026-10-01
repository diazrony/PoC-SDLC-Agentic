package com.example.monorepo.testing.architecture;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * Reglas de arquitectura hexagonal parametrizadas por aplicacion.
 *
 * <p>Definirlas una sola vez en una libreria evita que cada equipo escriba su
 * propia version y que las reglas se degraden con el tiempo.</p>
 */
public final class HexagonalArchitectureRules {

    /** Paquetes de framework que jamas pueden alcanzar al dominio. */
    private static final String[] FRAMEWORK_PACKAGES = {
            "io.quarkus..",
            "jakarta.ws.rs..",
            "jakarta.persistence..",
            "jakarta.enterprise..",
            "jakarta.inject..",
            "jakarta.transaction..",
            "org.hibernate..",
            "com.fasterxml.jackson..",
            "io.smallrye.."
    };

    private HexagonalArchitectureRules() {
    }

    /** Las dependencias apuntan hacia adentro: Infrastructure -> Application -> Domain. */
    public static ArchRule dependenciesPointInwards(String basePackage) {
        return layeredArchitecture()
                .consideringOnlyDependenciesInLayers()
                .layer("Domain").definedBy(basePackage + ".domain..")
                .layer("Application").definedBy(basePackage + ".application..")
                .layer("Infrastructure").definedBy(basePackage + ".infrastructure..")
                .whereLayer("Infrastructure").mayNotBeAccessedByAnyLayer()
                .whereLayer("Application").mayOnlyBeAccessedByLayers("Infrastructure")
                .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure")
                .as("Las dependencias deben apuntar hacia el dominio");
    }

    /** El dominio es Java puro: ni REST, ni JPA, ni CDI, ni Quarkus. */
    public static ArchRule domainIsFrameworkFree(String basePackage) {
        return noClasses()
                .that().resideInAPackage(basePackage + ".domain..")
                .should().dependOnClassesThat().resideInAnyPackage(FRAMEWORK_PACKAGES)
                .because("el dominio debe poder compilarse y testearse sin ningun framework")
                .allowEmptyShould(true);
    }

    /** La capa de aplicacion orquesta, pero tampoco conoce el framework. */
    public static ArchRule applicationIsFrameworkFree(String basePackage) {
        return noClasses()
                .that().resideInAPackage(basePackage + ".application..")
                .should().dependOnClassesThat().resideInAnyPackage(FRAMEWORK_PACKAGES)
                .because("los casos de uso se exponen como beans desde infrastructure.config, "
                        + "no se anotan con el framework")
                .allowEmptyShould(true);
    }

    /** Ni dominio ni aplicacion pueden conocer los adaptadores HTTP de lib-exceptions. */
    public static ArchRule innerLayersIgnoreTransportConcerns(String basePackage) {
        return noClasses()
                .that().resideInAnyPackage(basePackage + ".domain..", basePackage + ".application..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "com.example.monorepo.exceptions.rest..",
                        "com.example.monorepo.http..")
                .because("HTTP es un detalle de transporte que vive en infrastructure")
                .allowEmptyShould(true);
    }

    /** Todo adaptador de salida implementa un puerto de salida. */
    public static ArchRule outboundAdaptersImplementOutputPorts(String basePackage) {
        return classes()
                .that().resideInAPackage(basePackage + ".infrastructure.adapter.out..")
                .and().areNotInterfaces()
                .and().areNotNestedClasses()
                .should(implementAPortFrom(basePackage + ".application.port.out"))
                .allowEmptyShould(true);
    }

    /** Los recursos REST viven en el adaptador de entrada y se llaman Resource. */
    public static ArchRule restResourcesLiveInTheInboundAdapter(String basePackage) {
        return classes()
                .that().haveSimpleNameEndingWith("Resource")
                .should().resideInAPackage(basePackage + ".infrastructure.adapter.in.rest..")
                .allowEmptyShould(true);
    }

    /** Las entidades JPA no se escapan de la capa de persistencia. */
    public static ArchRule jpaEntitiesStayInPersistence(String basePackage) {
        return classes()
                .that().haveSimpleNameEndingWith("Entity")
                .should().resideInAPackage(basePackage + ".infrastructure.persistence.entity..")
                .because("el modelo de persistencia no debe confundirse con el modelo de dominio")
                .allowEmptyShould(true);
    }

    private static ArchCondition<JavaClass> implementAPortFrom(String portPackage) {
        return new ArchCondition<>("implementar un puerto de " + portPackage) {
            @Override
            public void check(JavaClass item, ConditionEvents events) {
                boolean satisfied = item.getAllRawInterfaces().stream()
                        .anyMatch(candidate -> candidate.getPackageName().startsWith(portPackage));
                events.add(new SimpleConditionEvent(item, satisfied,
                        item.getName() + (satisfied ? " implementa " : " NO implementa ") + "un puerto de salida"));
            }
        };
    }
}
