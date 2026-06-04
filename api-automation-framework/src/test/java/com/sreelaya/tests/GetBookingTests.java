package com.sreelaya.tests;

import com.sreelaya.models.Booking;
import com.sreelaya.models.BookingResponse;
import com.sreelaya.utils.BaseTest;
import com.sreelaya.utils.TestDataFactory;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("Booking Management")
@Feature("GET Booking")
public class GetBookingTests extends BaseTest {

    private int bookingId;

    @BeforeClass
    public void createTestBooking() {
        // Create a booking so we have a valid ID to GET
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

        log.info("Created test booking with ID: {}", bookingId);
    }

    @Test(priority = 1)
    @Story("Get all booking IDs")
    @Severity(SeverityLevel.NORMAL)
    @Description("GET /booking should return a list of booking IDs")
    public void getAllBookings_shouldReturn200WithList() {
        given()
            .spec(requestSpec)
        .when()
            .get("/booking")
        .then()
            .statusCode(200)
            .body("$", not(empty()))
            .body("[0].bookingid", notNullValue());

        log.info("✅ GET /booking returned booking list");
    }

    @Test(priority = 2)
    @Story("Get booking by valid ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("GET /booking/{id} with a valid ID should return the booking details")
    public void getBookingById_validId_shouldReturn200() {
        Booking response = given()
            .spec(requestSpec)
        .when()
            .get("/booking/" + bookingId)
        .then()
            .statusCode(200)
            .body("firstname", notNullValue())
            .body("bookingdates.checkin", notNullValue())
            .extract()
            .as(Booking.class);

        assertThat(response.getFirstname()).isNotBlank();
        assertThat(response.getTotalprice()).isGreaterThan(0);
        log.info("✅ GET /booking/{} returned valid booking", bookingId);
    }

    @Test(priority = 3)
    @Story("Get booking by invalid ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("GET /booking/{id} with a non-existent ID should return 404")
    public void getBookingById_invalidId_shouldReturn404() {
        given()
            .spec(requestSpec)
        .when()
            .get("/booking/999999999")
        .then()
            .statusCode(404);

        log.info("✅ GET /booking/999999999 correctly returned 404");
    }

    @Test(priority = 4)
    @Story("Filter bookings by first name")
    @Severity(SeverityLevel.NORMAL)
    @Description("GET /booking?firstname=X should return filtered results")
    public void getBookings_filterByFirstname_shouldReturn200() {
        given()
            .spec(requestSpec)
            .queryParam("firstname", "Jim")
        .when()
            .get("/booking")
        .then()
            .statusCode(200);

        log.info("✅ GET /booking?firstname=Jim filter works");
    }
}
