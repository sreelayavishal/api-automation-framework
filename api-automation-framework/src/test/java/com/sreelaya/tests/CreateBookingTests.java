package com.sreelaya.tests;

import com.sreelaya.models.Booking;
import com.sreelaya.models.BookingResponse;
import com.sreelaya.utils.BaseTest;
import com.sreelaya.utils.TestDataFactory;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("Booking Management")
@Feature("Create Booking")
public class CreateBookingTests extends BaseTest {

    @Test(priority = 1)
    @Story("Create booking with all valid fields")
    @Severity(SeverityLevel.BLOCKER)
    @Description("POST /booking with all required fields should return 200 and a booking ID")
    public void createBooking_allFields_shouldReturn200() {
        Booking payload = TestDataFactory.randomBooking();

        BookingResponse response = given()
            .spec(requestSpec)
            .body(payload)
        .when()
            .post("/booking")
        .then()
            .statusCode(200)
            .body("bookingid", notNullValue())
            .body("booking.firstname", equalTo(payload.getFirstname()))
            .body("booking.lastname", equalTo(payload.getLastname()))
            .body("booking.totalprice", equalTo(payload.getTotalprice()))
            .body("booking.depositpaid", equalTo(payload.isDepositpaid()))
            .extract()
            .as(BookingResponse.class);

        assertThat(response.getBookingid()).isPositive();
        assertThat(response.getBooking().getFirstname()).isEqualTo(payload.getFirstname());
        log.info("✅ Created booking ID: {}", response.getBookingid());
    }

    @Test(priority = 2)
    @Story("Create booking without optional additionalneeds field")
    @Severity(SeverityLevel.NORMAL)
    @Description("POST /booking without additionalneeds should still return 200")
    public void createBooking_withoutAdditionalNeeds_shouldReturn200() {
        Booking payload = TestDataFactory.randomBooking();
        payload.setAdditionalneeds(null);

        given()
            .spec(requestSpec)
            .body(payload)
        .when()
            .post("/booking")
        .then()
            .statusCode(200)
            .body("bookingid", notNullValue());

        log.info("✅ Booking without additionalneeds created successfully");
    }

    @Test(priority = 3)
    @Story("Create booking with missing required field - firstname")
    @Severity(SeverityLevel.CRITICAL)
    @Description("POST /booking without firstname should return 400 or 500")
    public void createBooking_missingFirstname_shouldReturnError() {
        String incompletePayload = "{\n" +
            "  \"lastname\": \"Smith\",\n" +
            "  \"totalprice\": 500,\n" +
            "  \"depositpaid\": true,\n" +
            "  \"bookingdates\": {\n" +
            "    \"checkin\": \"2025-01-01\",\n" +
            "    \"checkout\": \"2025-01-05\"\n" +
            "  }\n" +
            "}";

        given()
            .spec(requestSpec)
            .body(incompletePayload)
        .when()
            .post("/booking")
        .then()
            .statusCode(anyOf(equalTo(400), equalTo(500)));

        log.info("✅ Missing firstname correctly rejected");
    }

    @Test(priority = 4)
    @Story("Create booking with checkout before checkin")
    @Severity(SeverityLevel.NORMAL)
    @Description("POST /booking where checkout < checkin should return an error")
    public void createBooking_invalidDates_shouldReturnError() {
        String invalidDatesPayload = "{\n" +
            "  \"firstname\": \"Test\",\n" +
            "  \"lastname\": \"User\",\n" +
            "  \"totalprice\": 300,\n" +
            "  \"depositpaid\": false,\n" +
            "  \"bookingdates\": {\n" +
            "    \"checkin\": \"2025-06-10\",\n" +
            "    \"checkout\": \"2025-06-01\"\n" +
            "  }\n" +
            "}";

        given()
            .spec(requestSpec)
            .body(invalidDatesPayload)
        .when()
            .post("/booking")
        .then()
            .statusCode(anyOf(equalTo(400), equalTo(500)));

        log.info("✅ Invalid date range correctly rejected");
    }

    @Test(priority = 5)
    @Story("Create booking with negative price")
    @Severity(SeverityLevel.NORMAL)
    @Description("POST /booking with a negative totalprice — boundary test")
    public void createBooking_negativePrice_shouldReturnError() {
        Booking payload = TestDataFactory.randomBooking();
        payload.setTotalprice(-1);

        given()
            .spec(requestSpec)
            .body(payload)
        .when()
            .post("/booking")
        .then()
            .statusCode(anyOf(equalTo(400), equalTo(500)));

        log.info("✅ Negative price boundary test passed");
    }
}
