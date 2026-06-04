
# 🚀 API Automation Framework

![Java](https://img.shields.io/badge/Java-11-orange?logo=java)
![REST Assured](https://img.shields.io/badge/REST--Assured-5.4.0-green)
![TestNG](https://img.shields.io/badge/TestNG-7.9.0-red)
![Allure](https://img.shields.io/badge/Allure-2.25.0-yellow)
![CI](https://github.com/sreelaya-vishal/api-automation-framework/actions/workflows/api-tests.yml/badge.svg)

A production-grade REST API test automation framework built with **Java + REST Assured + TestNG + Allure Reports**.

Targets the open-source [Restful-Booker](https://restful-booker.herokuapp.com) hotel booking API, covering:
- ✅ Positive tests (happy path)
- ❌ Negative tests (error handling, boundary values)
- 🔐 Authentication tests
- 📐 JSON Schema validation
- ⚡ Response time assertions
- 🛡️ Basic security checks (SQL injection)

---

## 🏗️ Project Structure

```
api-automation-framework/
├── .github/workflows/
│   └── api-tests.yml              # GitHub Actions CI pipeline
├── src/test/
│   ├── java/com/sreelaya/
│   │   ├── config/
│   │   │   └── ConfigManager.java # Singleton property loader
│   │   ├── models/
│   │   │   ├── Booking.java       # Request/response POJO
│   │   │   ├── BookingDates.java
│   │   │   └── BookingResponse.java
│   │   ├── utils/
│   │   │   ├── BaseTest.java      # RestAssured specs + auth token
│   │   │   └── TestDataFactory.java # Dynamic data with JavaFaker
│   │   └── tests/
│   │       ├── AuthTests.java
│   │       ├── GetBookingTests.java
│   │       ├── CreateBookingTests.java
│   │       ├── UpdateBookingTests.java
│   │       ├── DeleteBookingTests.java
│   │       ├── NegativeTests.java
│   │       └── SchemaValidationTests.java
│   └── resources/
│       ├── config.properties
│       ├── log4j2.xml
│       └── schemas/
│           └── booking-schema.json
├── testng.xml
└── pom.xml
```

---

## ⚙️ Tech Stack

| Tool | Purpose |
|---|---|
| Java 11 | Core language |
| REST Assured 5.4 | HTTP client + assertions |
| TestNG 7.9 | Test runner, parallel execution |
| Allure 2.25 | Rich HTML test reports |
| Jackson | JSON serialization/deserialization |
| Lombok | Boilerplate reduction (getters, setters, builder) |
| JavaFaker | Dynamic, randomised test data |
| Maven | Build and dependency management |
| GitHub Actions | CI/CD — runs tests on every push |

---

## 🚀 How to Run Locally

### Prerequisites
- Java 11+
- Maven 3.8+

### Clone the repo
```bash
git clone https://github.com/sreelaya-vishal/api-automation-framework.git
cd api-automation-framework
```

### Run all tests
```bash
mvn clean test
```

### Run a specific test class
```bash
mvn clean test -Dtest=CreateBookingTests
```

### Generate Allure Report
```bash
mvn allure:report
mvn allure:serve     # opens report in browser automatically
```

---

## 📊 Test Coverage

| Test Class | # Tests | Type |
|---|---|---|
| AuthTests | 3 | Auth — positive + negative |
| GetBookingTests | 4 | GET — positive + filter + negative |
| CreateBookingTests | 5 | POST — positive + boundary + negative |
| UpdateBookingTests | 4 | PUT + PATCH — auth + no-auth |
| DeleteBookingTests | 3 | DELETE — auth + no-auth + not found |
| NegativeTests | 6 | Edge cases + security |
| SchemaValidationTests | 3 | Schema + response time + content-type |
| **Total** | **28** | |

---

## 🔑 Key Design Decisions

**1. Singleton ConfigManager** — Reads `config.properties` once; all tests share one config instance. Easy to swap environments (dev/staging/prod) by changing a single property.

**2. BaseTest + RequestSpec** — All tests extend `BaseTest`. Auth token is generated once in `@BeforeSuite` and reused across the entire suite — no redundant `/auth` calls.

**3. TestDataFactory + JavaFaker** — Tests never use hardcoded names or dates. Every run uses fresh randomised data, preventing state pollution between test runs.

**4. Allure annotations** — Every test is tagged with `@Epic`, `@Feature`, `@Story`, `@Severity`, and `@Description`, producing a meaningful stakeholder-readable report.

**5. Parallel execution** — `testng.xml` runs test methods in parallel (3 threads), cutting suite execution time by ~60%.

---

## 🤔 What I Learned Building This

- How to structure a framework that is maintainable and scalable — not just a collection of test scripts
- How `RequestSpecBuilder` eliminates duplication across 28 tests
- Why dynamic test data (Faker) matters: hardcoded data causes flaky tests when APIs retain state
- How to write meaningful Allure annotations so non-technical stakeholders can read reports
- How to set up GitHub Actions to run tests on every push with automatic report upload

---

## 📬 Contact

**Sreelaya Vishal** — QA Automation & Manual Test Engineer  
📧 sreelayavishal@gmail.com  
🔗 [linkedin.com/in/sreelaya-vishal](https://www.linkedin.com/in/sreelaya-vishal/)

