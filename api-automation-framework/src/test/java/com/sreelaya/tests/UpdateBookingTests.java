package com.sreelaya.tests;

import com.sreelaya.models.Booking;
import com.sreelaya.utils.BaseTest;
import com.sreelaya.utils.TestDataFactory;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Booking Management")
@Feature("Update Booking")
public class UpdateBookingTests extends BaseTest {

    private int bookingId;

    @BeforeClass
    public void createBookingForUpdate() {
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

        log.info("Created booking ID {} for update tests", bookingId);
    }

    @Test(priority = 1)
    @Story("Full update (PUT) with valid token")
    @Severity(SeverityLevel.CRITICAL)
    @Description("PUT /booking/{id} with auth token should update all fields and return 200")
    public void fullUpdate_withValidToken_shouldReturn200() {
        Booking updated = TestDataFactory.bookingWithName("Sreelaya", "Updated");

        authenticatedRequest()
            .body(updated)
        .when()
            .put("/booking/" + bookingId)
        .then()
            .statusCode(200)
            .body("firstname", equalTo("Sreelaya"))
            .body("lastname", equalTo("Updated"));

        log.info("✅ Full update of booking {} succeeded", bookingId);
    }

    @Test(priority = 2)
    @Story("Full update (PUT) without auth token")
    @Severity(SeverityLevel.CRITICAL)
    @Description("PUT /booking/{id} without token should return 403 Forbidden")
    public void fullUpdate_withoutToken_shouldReturn403() {
        Booking updated = TestDataFactory.randomBooking();

        given()
            .spec(requestSpec)
            .body(updated)
        .when()
            .put("/booking/" + bookingId)
        .then()
            .statusCode(403);

        log.info("✅ Unauthenticated PUT correctly returned 403");
    }

    @Test(priority = 3)
    @Story("Partial update (PATCH) with valid token")
    @Severity(SeverityLevel.NORMAL)
    @Description("PATCH /booking/{id} should update only the provided fields")
    public void partialUpdate_withValidToken_shouldReturn200() {
        String patchBody = "{\"firstname\":\"PatchedName\"}";

        authenticatedRequest()
            .body(patchBody)
        .when()
            .patch("/booking/" + bookingId)
        .then()
            .statusCode(200)
            .body("firstname", equalTo("PatchedName"));

        log.info("✅ Partial update (PATCH) of booking {} succeeded", bookingId);
    }

    @Test(priority = 4)
    @Story("Partial update (PATCH) without auth token")
    @Severity(SeverityLevel.NORMAL)
    @Description("PATCH /booking/{id} without token should return 403")
    public void partialUpdate_withoutToken_shouldReturn403() {
        given()
            .spec(requestSpec)
            .body("{\"firstname\":\"Hacker\"}")
        .when()
            .patch("/booking/" + bookingId)
        .then()
            .statusCode(403);

        log.info("✅ Unauthenticated PATCH correctly returned 403");
    }
}
