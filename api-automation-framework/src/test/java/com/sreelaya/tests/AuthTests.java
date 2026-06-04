package com.sreelaya.tests;

import com.sreelaya.utils.BaseTest;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Authentication")
@Feature("Token Generation")
public class AuthTests extends BaseTest {

    @Test(priority = 1)
    @Story("Valid credentials return a token")
    @Severity(SeverityLevel.BLOCKER)
    @Description("POST /auth with valid credentials should return a non-null token string")
    public void validCredentials_shouldReturnToken() {
        given()
            .spec(requestSpec)
            .body("{\"username\":\"admin\",\"password\":\"password123\"}")
        .when()
            .post("/auth")
        .then()
            .statusCode(200)
            .body("token", notNullValue())
            .body("token", not(emptyString()));

        log.info("✅ Auth token generated successfully");
    }

    @Test(priority = 2)
    @Story("Invalid credentials return bad credentials message")
    @Severity(SeverityLevel.CRITICAL)
    @Description("POST /auth with wrong password should not return a valid token")
    public void invalidCredentials_shouldReturnBadCredentials() {
        given()
            .spec(requestSpec)
            .body("{\"username\":\"admin\",\"password\":\"wrongpassword\"}")
        .when()
            .post("/auth")
        .then()
            .statusCode(200)                        // API returns 200 with error reason
            .body("reason", equalTo("Bad credentials"));

        log.info("✅ Invalid credentials correctly rejected");
    }

    @Test(priority = 3)
    @Story("Missing fields in auth request")
    @Severity(SeverityLevel.NORMAL)
    @Description("POST /auth with missing password field")
    public void missingPassword_shouldReturnBadCredentials() {
        given()
            .spec(requestSpec)
            .body("{\"username\":\"admin\"}")
        .when()
            .post("/auth")
        .then()
            .statusCode(200)
            .body("reason", equalTo("Bad credentials"));

        log.info("✅ Missing password field correctly handled");
    }
}
