package com.example.monorepo.observability.interceptor;

import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marca un metodo o clase para que se registren su duracion y resultado.
 *
 * <p>Ejemplo tipico de capacidad transversal que no debe reimplementarse
 * en cada microservicio.</p>
 */
@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Observed {

    /** Nombre logico de la operacion. Si se omite se usa el nombre del metodo. */
    @Nonbinding
    String value() default "";
}
