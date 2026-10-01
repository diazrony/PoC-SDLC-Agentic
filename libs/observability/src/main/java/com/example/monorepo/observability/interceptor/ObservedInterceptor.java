package com.example.monorepo.observability.interceptor;

import com.example.monorepo.observability.log.MdcKeys;
import com.example.monorepo.observability.log.StructuredLog;
import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.jboss.logging.Logger;
import org.jboss.logging.MDC;

/**
 * Implementacion del binding {@link Observed}.
 *
 * <p>Para que Quarkus registre este interceptor desde un JAR externo, la
 * libreria debe estar indexada (Jandex) o declarar {@code META-INF/beans.xml}.
 * Aqui se usa Jandex, configurado en {@code libs/pom.xml}.</p>
 */
@Observed
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class ObservedInterceptor {

    private static final Logger LOG = Logger.getLogger(ObservedInterceptor.class);

    @AroundInvoke
    public Object observe(InvocationContext context) throws Exception {
        Observed annotation = context.getMethod().getAnnotation(Observed.class);
        String operation = (annotation == null || annotation.value().isBlank())
                ? context.getMethod().getName()
                : annotation.value();

        MDC.put(MdcKeys.OPERATION, operation);
        long startedAt = System.nanoTime();
        try {
            Object result = context.proceed();
            log(operation, startedAt, "SUCCESS");
            return result;
        } catch (Exception exception) {
            log(operation, startedAt, "FAILURE:" + exception.getClass().getSimpleName());
            throw exception;
        } finally {
            MDC.remove(MdcKeys.OPERATION);
        }
    }

    private void log(String operation, long startedAt, String outcome) {
        long elapsedMillis = (System.nanoTime() - startedAt) / 1_000_000L;
        LOG.info(StructuredLog.event("operation.completed")
                .with("operation", operation)
                .with("outcome", outcome)
                .with("durationMs", elapsedMillis)
                .format());
    }
}
