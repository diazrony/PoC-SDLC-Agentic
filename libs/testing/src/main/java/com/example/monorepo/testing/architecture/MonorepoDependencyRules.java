package com.example.monorepo.testing.architecture;

import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Reglas de frontera entre modulos del monorepo.
 *
 * <pre>
 *   apps -&gt; libs   PERMITIDO
 *   libs -&gt; libs   PERMITIDO (controlado)
 *   apps -&gt; apps   PROHIBIDO
 *   libs -&gt; apps   PROHIBIDO
 * </pre>
 *
 * <p>Complementan al Maven Enforcer: el Enforcer bloquea la dependencia
 * declarada en el POM, ArchUnit bloquea el acoplamiento a nivel de codigo
 * (por ejemplo si alguien copia una clase de otra app).</p>
 */
public final class MonorepoDependencyRules {

    private MonorepoDependencyRules() {
    }

    /** Una aplicacion no puede referenciar clases de otra aplicacion. */
    public static ArchRule noDependenciesOnOtherApplications(String ownApplicationPackage) {
        String[] foreignApplications = MonorepoPackages.APPLICATIONS.stream()
                .filter(candidate -> !candidate.equals(ownApplicationPackage))
                .map(candidate -> candidate + "..")
                .toArray(String[]::new);

        return noClasses()
                .that().resideInAPackage(ownApplicationPackage + "..")
                .should().dependOnClassesThat().resideInAnyPackage(foreignApplications)
                .because("cada aplicacion debe poder desplegarse y evolucionar por separado")
                .allowEmptyShould(true);
    }

    /** Una libreria no puede referenciar clases de ninguna aplicacion. */
    public static ArchRule librariesDoNotDependOnApplications() {
        String[] allApplications = MonorepoPackages.APPLICATIONS.stream()
                .map(candidate -> candidate + "..")
                .toArray(String[]::new);

        return noClasses()
                .that().resideInAPackage(MonorepoPackages.LIBS + "..")
                .should().dependOnClassesThat().resideInAnyPackage(allApplications)
                .because("las librerias son transversales y no conocen a sus consumidores")
                .allowEmptyShould(true);
    }
}
