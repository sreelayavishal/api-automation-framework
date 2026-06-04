package com.sreelaya.utils;

import com.sreelaya.config.ConfigManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeSuite;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Base class for all test classes.
 * Sets up RestAssured specifications and manages the auth token.
 */
public class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);
    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec;
    protected static String authToken;

    @BeforeSuite(alwaysRun = true)
    public void globalSetup() {
        ConfigManager config = ConfigManager.getInstance();

        // ── Base request spec ───────────────────────────────────────────
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(config.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())   // attaches req/resp to Allure report
                .log(LogDetail.ALL)
                .build();

        // ── Base response spec ──────────────────────────────────────────
        responseSpec = new ResponseSpecBuilder()
                .expectResponseTime(org.hamcrest.Matchers.lessThan(5000L))  // max 5s response
                .build();

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        // ── Generate auth token ─────────────────────────────────────────
        authToken = generateToken(config.getAdminUsername(), config.getAdminPassword());
        log.info("Auth token generated successfully");
    }

    /**
     * Calls /auth and returns the token string.
     */
    protected String generateToken(String username, String password) {
        String body = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
        return given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract()
                .path("token");
    }

    /**
     * Returns a request spec pre-loaded with the Cookie auth token.
     * Use this for PUT / PATCH / DELETE requests.
     */
    protected RequestSpecification authenticatedRequest() {
        return given()
                .spec(requestSpec)
                .cookie("token", authToken);
    }
}
