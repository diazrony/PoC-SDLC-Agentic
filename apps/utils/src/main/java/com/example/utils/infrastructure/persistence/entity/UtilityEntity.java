package com.example.utils.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * MODELO DE PERSISTENCIA. No es el modelo de dominio.
 *
 * <p>Mantenerlos separados tiene un coste (un mapper) y un beneficio grande:
 * el esquema de base de datos puede cambiar sin arrastrar al dominio, y el
 * dominio puede modelar invariantes que JPA no sabe expresar.</p>
 */
@Entity
@Table(name = "UTILITIES")
public class UtilityEntity {

    @Id
    @Column(name = "ID", length = 64, nullable = false)
    public String id;

    @Column(name = "LABEL", length = 200, nullable = false)
    public String label;

    @Column(name = "STATUS", length = 20, nullable = false)
    public String status;

    @Column(name = "THRESHOLD", precision = 19, scale = 2, nullable = false)
    public BigDecimal threshold;

    @Column(name = "CREATED_AT", nullable = false)
    public Instant createdAt;

    public UtilityEntity() {
    }
}
