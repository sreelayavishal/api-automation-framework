package com.sreelaya.tests;

import com.sreelaya.models.Booking;
import com.sreelaya.utils.BaseTest;
import com.sreelaya.utils.TestDataFactory;
import io.qameta.allure.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Epic("Booking Management")
@Feature("Schema Validation")
public class SchemaValidationTests extends BaseTest {

    private int bookingId;

    @BeforeClass
    public void createBookingForSchema() {
        Booking payload = TestDataFactory.randomBooking();
        bookingId = given()
                .spec(requestSpec)
                .body(payload)
            .when()
                .post("/booking")
            .then()
                .statusCode(200)
                .extract()
                .path("bookingid");

        log.info("Schema test using booking ID: {}", bookingId);
    }

    @Test(priority = 1)
    @Story("POST /booking response matches JSON schema")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The response body from POST /booking must conform to booking-schema.json")
    public void createBooking_responseShouldMatchSchema() {
        Booking payload = TestDataFactory.randomBooking();

        given()
            .spec(requestSpec)
            .body(payload)
        .when()
            .post("/booking")
        .then()
            .statusCode(200)
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/booking-schema.json"));

        log.info("✅ POST /booking response matches JSON schema");
    }

    @Test(priority = 2)
    @Story("Response time is within acceptable threshold")
    @Severity(SeverityLevel.NORMAL)
    @Description("POST /booking should respond within 3 seconds")
    public void createBooking_responseShouldBeFast() {
        Booking payload = TestDataFactory.randomBooking();

        given()
            .spec(requestSpec)
            .body(payload)
        .when()
            .post("/booking")
        .then()
            .statusCode(200)
            .time(org.hamcrest.Matchers.lessThan(3000L));

        log.info("✅ Response time within 3 second threshold");
    }

    @Test(priority = 3)
    @Story("GET /booking/{id} response has correct content type")
    @Severity(SeverityLevel.NORMAL)
    @Description("Response Content-Type must be application/json")
    public void getBooking_contentTypeShouldBeJson() {
        given()
            .spec(requestSpec)
        .when()
            .get("/booking/" + bookingId)
        .then()
            .statusCode(200)
            .contentType("application/json");

        log.info("✅ Content-Type is application/json");
    }
}
