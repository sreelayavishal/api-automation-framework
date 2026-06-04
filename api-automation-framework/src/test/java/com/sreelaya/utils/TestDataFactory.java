package com.sreelaya.utils;

import com.github.javafaker.Faker;
import com.sreelaya.models.Booking;
import com.sreelaya.models.BookingDates;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Generates randomised, realistic test data using JavaFaker.
 * Using dynamic data avoids hardcoded values and makes tests more robust.
 */
public class TestDataFactory {

    private static final Faker faker = new Faker();
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Builds a fully populated Booking with random data.
     */
    public static Booking randomBooking() {
        LocalDate checkin  = LocalDate.now().plusDays(faker.number().numberBetween(1, 30));
        LocalDate checkout = checkin.plusDays(faker.number().numberBetween(1, 14));

        return Booking.builder()
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .totalprice(faker.number().numberBetween(100, 5000))
                .depositpaid(faker.bool().bool())
                .bookingdates(BookingDates.builder()
                        .checkin(checkin.format(DATE_FMT))
                        .checkout(checkout.format(DATE_FMT))
                        .build())
                .additionalneeds(faker.options().option("Breakfast", "Lunch", "Dinner", "None"))
                .build();
    }

    /**
     * Builds a Booking with a specific first and last name (useful for update tests).
     */
    public static Booking bookingWithName(String firstname, String lastname) {
        Booking booking = randomBooking();
        booking.setFirstname(firstname);
        booking.setLastname(lastname);
        return booking;
    }
}
