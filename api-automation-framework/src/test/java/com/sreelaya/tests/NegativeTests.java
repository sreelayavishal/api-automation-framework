package com.sreelaya.tests;

import com.sreelaya.utils.BaseTest;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Booking Management")
@Feature("Negative & Boundary Testing")
public class NegativeTests extends BaseTest {

    @Test(priority = 1)
    @Story("Send empty request body to POST /booking")
    @Severity(SeverityLevel.NORMAL)
    @Description("Empty body should return 400 or 500 error")
    public void createBooking_emptyBody_shouldReturnError() {
        given()
            .spec(requestSpec)
            .body("{}")
        .when()
            .post("/booking")
        .then()
            .statusCode(anyOf(equalTo(400), equalTo(500)));

        log.info("✅ Empty body correctly rejected");
    }

    @Test(priority = 2)
    @Story("Send plain text instead of JSON to POST /booking")
    @Severity(SeverityLevel.NORMAL)
    @Description("Wrong content type should return error")
    public void createBooking_wrongContentType_shouldReturnError() {
        given()
            .baseUri("https://restful-booker.herokuapp.com")
            .contentType("text/plain")
            .body("this is not json")
        .when()
            .post("/booking")
        .then()
            .statusCode(anyOf(equalTo(400), equalTo(415), equalTo(500)));

        log.info("✅ Wrong content type correctly rejected");
    }

    @Test(priority = 3)
    @Story("GET booking with string ID instead of integer")
    @Severity(SeverityLevel.MINOR)
    @Description("Non-numeric booking ID in path should return 404")
    public void getBooking_stringId_shouldReturn404() {
        given()
            .spec(requestSpec)
        .when()
            .get("/booking/abc")
        .then()
            .statusCode(404);

        log.info("✅ String ID in path correctly returned 404");
    }

    @Test(priority = 4)
    @Story("GET booking with zero ID")
    @Severity(SeverityLevel.MINOR)
    @Description("Booking ID 0 is not valid — should return 404")
    public void getBooking_zeroId_shouldReturn404() {
        given()
            .spec(requestSpec)
        .when()
            .get("/booking/0")
        .then()
            .statusCode(404);

        log.info("✅ Zero ID correctly returned 404");
    }

    @Test(priority = 5)
    @Story("Request unsupported endpoint")
    @Severity(SeverityLevel.MINOR)
    @Description("GET /booking/nonexistent-path should return 404")
    public void get_unsupportedEndpoint_shouldReturn404() {
        given()
            .spec(requestSpec)
        .when()
            .get("/nonexistent-endpoint")
        .then()
            .statusCode(404);

        log.info("✅ Unsupported endpoint correctly returned 404");
    }

    @Test(priority = 6)
    @Story("SQL injection attempt in query parameter")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Firstname param with SQL injection string should be handled safely")
    public void getBookings_sqlInjectionParam_shouldNotCrash() {
        given()
            .spec(requestSpec)
            .queryParam("firstname", "' OR '1'='1")
        .when()
            .get("/booking")
        .then()
            .statusCode(anyOf(equalTo(200), equalTo(400)));  // must NOT be 500

        log.info("✅ SQL injection in query param did not cause server error");
    }
}
