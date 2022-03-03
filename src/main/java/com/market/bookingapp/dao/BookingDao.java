/**
 @author nick.drakopoulos
 */

package com.market.bookingapp.dao;

import com.market.bookingapp.domain.BookingEntity;
import com.market.bookingapp.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

@Component
@RequiredArgsConstructor
public class BookingDao {

    private final BookingRepository repository;

    public Optional<BookingEntity> findById(Integer id) {
        return repository.findById(id);
    }

    public List<BookingEntity> findAll() {
        return repository.findAll();
    }

    public List<BookingEntity> findOverlappingWith(Optional<Integer> selfBookingId, Integer propertyId, Instant startDate, Instant endDate) {
        checkArgument(propertyId != null, "PropertyId is mandatory!");
        checkArgument(startDate != null, "StartDate is mandatory!");
        checkArgument(endDate != null, "EndDate is mandatory!");
        return repository.findOverlappingWith(selfBookingId.orElse(null), propertyId, startDate, endDate);
    }

    public Integer save(BookingEntity entity) {
        return Optional.ofNullable(repository.save(entity))
            .map(BookingEntity::getId)
            .orElse(null);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

}
