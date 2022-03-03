package com.market.bookingapp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.Optional;

/**
 * A request to check whether a given date range that is supposed to be blocked by an owner or booked by a guest
 * for a property overlaps with an existing active booking or block.
 * For this check we should ignore the same block or booking if it's being updated.
 */
@Builder
@Getter
@ToString
public class BookingOverlapDetectorRequestDto {

    Optional<Integer> selfId;
    TimeAllocationType timeAllocationType;
    Integer propertyId;
    Instant startDate;
    Instant endDate;

}
