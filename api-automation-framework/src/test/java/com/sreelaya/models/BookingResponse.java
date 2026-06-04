package com.sreelaya.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Represents the response from POST /booking
 * { "bookingid": 123, "booking": { ... } }
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingResponse {
    private int bookingid;
    private Booking booking;
}
