package com.example.monorepo.events.publisher;

import com.example.monorepo.events.contract.DomainEvent;

/**
 * Puerto de publicacion de eventos.
 *
 * <p>La libreria define el CONTRATO; cada aplicacion aporta el adaptador
 * (Kafka, AMQP, outbox en base de datos...). Compartir la interfaz no acopla
 * a nadie a una tecnologia de mensajeria concreta.</p>
 */
@FunctionalInterface
public interface EventPublisher {

    void publish(DomainEvent event);
}
