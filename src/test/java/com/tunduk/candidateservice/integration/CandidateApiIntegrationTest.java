package com.tunduk.candidateservice.integration;

import com.tunduk.candidateservice.dto.CandidateWriteRequest;
import com.tunduk.candidateservice.dto.StatusChangeRequest;
import com.tunduk.candidateservice.model.enums.CandidateStatus;
import com.tunduk.candidateservice.model.enums.Verdict;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CandidateApiIntegrationTest extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/v1/candidates";
    }

    private CandidateWriteRequest validRequest() {
        CandidateWriteRequest req = new CandidateWriteRequest();
        req.setName("Bubunur Bubunurov");
        req.setEmail("bubunur@example.com");
        req.setPhone("05531410131");
        req.setPosition("java-developer");
        req.setVerdict(Verdict.FIT);
        return req;
    }

    @Test
    void shouldCreateCandidate() {
        given()
                .contentType(ContentType.JSON)
                .body(validRequest())
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("name", equalTo("Bubunur Bubunurov"))
                .body("email", equalTo("bubunur@example.com"))
                .body("id", notNullValue());
    }

    @Test
    void shouldGetCandidateById() {
        String id = given()
                .contentType(ContentType.JSON)
                .body(validRequest())
                .post()
                .then()
                .extract().path("id");

        given()
                .get("/{id}", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id));
    }

    @Test
    void shouldReturnListOfCandidates() {
        given()
                .contentType(ContentType.JSON)
                .body(validRequest())
                .post();

        given()
                .get()
                .then()
                .statusCode(200)
                .body("content", not(empty()));
    }

    @Test
    void shouldReturnListOfCandidatesWithParamFIT() {
        given()
                .contentType(ContentType.JSON)
                .body(validRequest())
                .post()
                .then()
                .statusCode(201);

        given()
                .param("verdict", Verdict.FIT)
                .get()
                .then()
                .statusCode(200)
                .body("content", not(empty()))
                .body("content[0].verdict", equalTo("FIT"));
    }

    @Test
    void shouldUpdateCandidate() {
        String id = given()
                .contentType(ContentType.JSON)
                .body(validRequest())
                .post()
                .then()
                .extract().path("id");

        CandidateWriteRequest updated = validRequest();
        updated.setName("Nurai Nuraiov");

        given()
                .contentType(ContentType.JSON)
                .body(updated)
                .when()
                .put("/{id}", id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Nurai Nuraiov"));
    }

    @Test
    void shouldDeleteCandidate() {
        String id = given()
                .contentType(ContentType.JSON)
                .body(validRequest())
                .post()
                .then()
                .extract().path("id");

        given()
                .delete("/{id}", id)
                .then()
                .statusCode(204);

        given()
                .get("/{id}", id)
                .then()
                .statusCode(404);
    }

    @Test
    void shouldChangeStatus() {
        String id = given()
                .contentType(ContentType.JSON)
                .body(validRequest())
                .post()
                .then()
                .extract().path("id");

        StatusChangeRequest statusReq = new StatusChangeRequest();
        statusReq.setStatus(CandidateStatus.INVITED);
        statusReq.setComment("Назначено интервью");

        given()
                .contentType(ContentType.JSON)
                .body(statusReq)
                .when()
                .patch("/{id}/status", id)
                .then()
                .statusCode(200)
                .body("status", equalTo("IN_REVIEW"));
    }

    @Test
    void shouldReturnStatusHistory() {
        String id = given()
                .contentType(ContentType.JSON)
                .body(validRequest())
                .post()
                .then()
                .extract().path("id");

        given()
                .get("/{id}/status-history", id)
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }
}
