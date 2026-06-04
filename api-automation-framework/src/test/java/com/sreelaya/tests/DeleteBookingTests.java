package com.sreelaya.tests;

import com.sreelaya.models.Booking;
import com.sreelaya.utils.BaseTest;
import com.sreelaya.utils.TestDataFactory;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Booking Management")
@Feature("Delete Booking")
public class DeleteBookingTests extends BaseTest {

    private int createTempBooking() {
        Booking payload = TestDataFactory.randomBooking();
        return given()
                .spec(requestSpec)
                .body(payload)
            .when()
                .post("/booking")
            .then()
                .statusCode(200)
                .extract()
                .path("bookingid");
    }

    @Test(priority = 1)
    @Story("Delete booking with valid token")
    @Severity(SeverityLevel.CRITICAL)
    @Description("DELETE /booking/{id} with auth token should return 201 Created (API quirk)")
    public void deleteBooking_withValidToken_shouldReturn201() {
        int id = createTempBooking();

        authenticatedRequest()
        .when()
            .delete("/booking/" + id)
        .then()
            .statusCode(201);   // Note: Restful-Booker returns 201 on delete (not 204)

        // Verify it's actually gone
        given()
            .spec(requestSpec)
        .when()
            .get("/booking/" + id)
        .then()
            .statusCode(404);

        log.info("✅ Booking {} deleted and confirmed gone (404)", id);
    }

    @Test(priority = 2)
    @Story("Delete booking without auth token")
    @Severity(SeverityLevel.CRITICAL)
    @Description("DELETE /booking/{id} without token should return 403 Forbidden")
    public void deleteBooking_withoutToken_shouldReturn403() {
        int id = createTempBooking();

        given()
            .spec(requestSpec)
        .when()
            .delete("/booking/" + id)
        .then()
            .statusCode(403);

        log.info("✅ Unauthenticated DELETE correctly returned 403");
    }

    @Test(priority = 3)
    @Story("Delete non-existent booking")
    @Severity(SeverityLevel.NORMAL)
    @Description("DELETE /booking/{id} for a non-existent ID should return 405 or 404")
    public void deleteBooking_nonExistentId_shouldReturnError() {
        authenticatedRequest()
        .when()
            .delete("/booking/999999999")
        .then()
            .statusCode(anyOf(equalTo(404), equalTo(405)));

        log.info("✅ Delete non-existent booking correctly returned error");
    }
}
