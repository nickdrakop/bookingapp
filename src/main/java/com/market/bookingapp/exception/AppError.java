/**
 @author nick.drakopoulos
 */

package com.market.bookingapp.exception;

public enum AppError {

    BOOKING_NOT_FOUND(1, "Booking was not found."),
    BLOCK_NOT_FOUND(2, "Block was not found."),
    OVERLAP_FOR_GIVEN_DATE_RANGE(3, "The given date range overlaps with an existing active booking or block."),
    INVALID_DATE_FORMAT(4, "Invalid format for date parameter."),
    INVALID_PARAMETERS(5, "Invalid parameters.");

    private final int code;
    private final String description;

    AppError(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getErrorCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
