package com.example.configuration.rest;

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
class ConfigurationSettingResourceTest {

    @Test
    void devuelve200ParaUnRegistroActivo() {
        given()
            .when().get("/api/configurations/C-0001")
            .then()
                .statusCode(200)
                .body("id", is("C-0001"))
                .body("status", is("ACTIVE"))
                .header("X-Correlation-Id", notNullValue());
    }

    @Test
    void devuelveProblemDetails404CuandoNoExiste() {
        given()
            .when().get("/api/configurations/NO-EXISTE")
            .then()
                .statusCode(404)
                .contentType("application/problem+json")
                .body("code", is("CONF-0001"))
                .body("status", is(404))
                .body("traceId", notNullValue())
                .body("instance", is("/api/configurations/NO-EXISTE"));
    }

    @Test
    void devuelveProblemDetails422CuandoEstaBloqueado() {
        given()
            .when().get("/api/configurations/C-0002")
            .then()
                .statusCode(422)
                .contentType("application/problem+json")
                .body("code", is("CONF-0002"));
    }

    @Test
    void devuelve201AlCrear() {
        given()
            .contentType("application/json")
            .body("{\"label\":\"creado desde el test\",\"numericValue\":42.00}")
            .when().post("/api/configurations")
            .then()
                .statusCode(201)
                .header("Location", notNullValue())
                .body("status", is("ACTIVE"));
    }

    @Test
    void devuelveProblemDetails400CuandoLaPeticionEsInvalida() {
        given()
            .contentType("application/json")
            .body("{\"label\":\"\",\"numericValue\":1.00}")
            .when().post("/api/configurations")
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
