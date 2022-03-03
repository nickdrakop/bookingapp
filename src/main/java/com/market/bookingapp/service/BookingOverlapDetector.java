/**
 @author nick.drakopoulos
 */

package com.market.bookingapp.service;

import com.market.bookingapp.dao.BlockDao;
import com.market.bookingapp.dao.BookingDao;
import com.market.bookingapp.domain.BlockEntity;
import com.market.bookingapp.domain.BookingEntity;
import com.market.bookingapp.dto.BookingOverlapDetectorRequestDto;
import com.market.bookingapp.dto.TimeAllocationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Identifies whether a given date range overlaps with an existing active booking or block of a property.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BookingOverlapDetector {

    private final BookingDao bookingDao;
    private final BlockDao blockDao;

    public boolean overlapsWithActiveBookingOrBlock(BookingOverlapDetectorRequestDto requestDto) {
        Integer propertyId = requestDto.getPropertyId();
        Instant startDate = requestDto.getStartDate();
        Instant endDate = requestDto.getEndDate();
        Optional<Integer> selfBookingId = requestDto.getTimeAllocationType().equals(TimeAllocationType.BOOKING)
            ? requestDto.getSelfId() : Optional.empty();
        Optional<Integer> selfBlockId = requestDto.getTimeAllocationType().equals(TimeAllocationType.BLOCK)
            ? requestDto.getSelfId() : Optional.empty();
        String messageForTimeAllocation = messageForTimeAllocation(selfBookingId, selfBlockId, requestDto);

        List<String> overlappingBookingIdsForGivenDates = bookingDao.findOverlappingWith(selfBookingId, propertyId, startDate, endDate).stream()
            .map(BookingEntity::getId).map(Object::toString).collect(Collectors.toList());
        if (overlappingBookingIdsForGivenDates.size() > 0) {
            log.warn("Date range startDate/endDate: {}/{} {} overlaps with already existing and active bookings of property with id: {} " +
                "(overlapping booking ids for given dates: {})", startDate, endDate, messageForTimeAllocation, propertyId, String.join(",", overlappingBookingIdsForGivenDates));
            return true;
        }
        List<String> overlappingBlockIdsForGivenDates = blockDao.findOverlappingWith(selfBlockId, propertyId, startDate, endDate).stream()
            .map(BlockEntity::getId).map(Object::toString).collect(Collectors.toList());
        if (overlappingBlockIdsForGivenDates.size() > 0) {
            log.warn("Date range startDate/endDate: {}/{} {} overlaps with already existing blocks of property with id: {} " +
                    "(overlapping block ids for given dates: {})", startDate, endDate, messageForTimeAllocation, propertyId, String.join(",", overlappingBlockIdsForGivenDates));
            return true;
        }
        return false;
    }

    private String messageForTimeAllocation(Optional<Integer> selfBookingId, Optional<Integer> selfBlockId, BookingOverlapDetectorRequestDto requestDto) {
        if (selfBookingId.isPresent()) {
            return "for existing BOOKING (id: " + selfBookingId.get() + " )";
        } else if (selfBlockId.isPresent()) {
            return "for existing BLOCK (id: " + selfBlockId.get() + " )";
        } else if (requestDto.getTimeAllocationType().equals(TimeAllocationType.BOOKING)) {
            return "for new BOOKING";
        } else {
            return "for new BLOCK";
        }
    }
}
