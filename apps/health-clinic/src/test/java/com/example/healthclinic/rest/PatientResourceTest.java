package com.example.healthclinic.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Test de integracion ligero: arranca la aplicacion completa con H2 en memoria
 * y verifica el flujo REST -> puerto -> servicio -> puerto -> persistencia,
 * incluido el contrato de error Problem Details que aporta lib-exceptions.
 */
@QuarkusTest
class PatientResourceTest {

    @Test
    void devuelve200ParaUnRegistroActivo() {
        given()
            .when().get("/api/patients/P-0001")
            .then()
                .statusCode(200)
                .body("id", is("P-0001"))
                .body("status", is("ACTIVE"))
                .header("X-Correlation-Id", notNullValue());
    }

    @Test
    void devuelveProblemDetails404CuandoNoExiste() {
        given()
            .when().get("/api/patients/NO-EXISTE")
            .then()
                .statusCode(404)
                .contentType("application/problem+json")
                .body("code", is("HCLN-0001"))
                .body("status", is(404))
                .body("traceId", notNullValue())
                .body("instance", is("/api/patients/NO-EXISTE"));
    }

    @Test
    void devuelveProblemDetails422CuandoEstaBloqueado() {
        given()
            .when().get("/api/patients/P-0002")
            .then()
                .statusCode(422)
                .contentType("application/problem+json")
                .body("code", is("HCLN-0002"));
    }

    @Test
    void devuelve201AlCrear() {
        given()
            .contentType("application/json")
            .body("{\"label\":\"creado desde el test\",\"copaymentAmount\":42.00}")
            .when().post("/api/patients")
            .then()
                .statusCode(201)
                .header("Location", notNullValue())
                .body("status", is("ACTIVE"));
    }

    @Test
    void devuelveProblemDetails400CuandoLaPeticionEsInvalida() {
        given()
            .contentType("application/json")
            .body("{\"label\":\"\",\"copaymentAmount\":1.00}")
            .when().post("/api/patients")
            .then()
                .statusCode(400)
                .contentType("application/problem+json")
                .body("code", is("CORE-0001"));
    }

    @Test
    void elEndpointDeSaludRespondeUp() {
        given()
            .when().get("/q/health/ready")
            .then()
                .statusCode(200)
                .body("status", is("UP"));
    }
}
