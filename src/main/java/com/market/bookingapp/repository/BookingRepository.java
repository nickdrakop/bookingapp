/**
 @author nick.drakopoulos
 */

package com.market.bookingapp.repository;

import com.market.bookingapp.domain.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {

    @Query(value = "SELECT * FROM booking " +
        "WHERE status = 'ACTIVE' " +
        "AND property_id = :propertyId " +
        "AND (:selfBookingId IS NULL OR id != :selfBookingId) " +
        "AND ( (:startDate IS NULL OR :endDate IS NULL)" + // we should always have a start or end date
        "   OR (:startDate BETWEEN start_date AND end_date) " + // start date of new allocation is within the range of an existing
        "   OR (:endDate BETWEEN start_date AND end_date)" + // end date of new allocation is within the range of an existing
        "   OR (:startDate <= start_date AND :endDate >= end_date))", // date range of new allocation includes/covers/overlaps fully with the range of an existing
        nativeQuery = true)
    List<BookingEntity> findOverlappingWith(Integer selfBookingId, Integer propertyId, Instant startDate, Instant endDate);
}
