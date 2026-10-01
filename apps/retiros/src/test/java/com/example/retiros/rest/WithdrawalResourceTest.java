package com.example.retiros.rest;

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
class WithdrawalResourceTest {

    @Test
    void devuelve200ParaUnRegistroActivo() {
        given()
            .when().get("/api/retiros/W-0001")
            .then()
                .statusCode(200)
                .body("id", is("W-0001"))
                .body("status", is("ACTIVE"))
                .header("X-Correlation-Id", notNullValue());
    }

    @Test
    void devuelveProblemDetails404CuandoNoExiste() {
        given()
            .when().get("/api/retiros/NO-EXISTE")
            .then()
                .statusCode(404)
                .contentType("application/problem+json")
                .body("code", is("RET-0001"))
                .body("status", is(404))
                .body("traceId", notNullValue())
                .body("instance", is("/api/retiros/NO-EXISTE"));
    }

    @Test
    void devuelveProblemDetails422CuandoEstaBloqueado() {
        given()
            .when().get("/api/retiros/W-0002")
            .then()
                .statusCode(422)
                .contentType("application/problem+json")
                .body("code", is("RET-0002"));
    }

    @Test
    void devuelve201AlCrear() {
        given()
            .contentType("application/json")
            .body("{\"label\":\"creado desde el test\",\"amount\":42.00}")
            .when().post("/api/retiros")
            .then()
                .statusCode(201)
                .header("Location", notNullValue())
                .body("status", is("ACTIVE"));
    }

    @Test
    void devuelveProblemDetails400CuandoLaPeticionEsInvalida() {
        given()
            .contentType("application/json")
            .body("{\"label\":\"\",\"amount\":1.00}")
            .when().post("/api/retiros")
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
