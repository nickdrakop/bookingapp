/**
 @author nick.drakopoulos
 */

package com.market.bookingapp.repository;

import com.market.bookingapp.domain.BlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<BlockEntity, Integer> {

    @Query(value = "SELECT * FROM block " +
        "WHERE property_id = :propertyId " +
        "AND (:selfBlockId IS NULL OR id != :selfBlockId) " +
        "AND ( (:startDate IS NULL OR :endDate IS NULL)" + // we should always have a start or end date
        "   OR (:startDate BETWEEN start_date AND end_date) " + // start date of new allocation is within the range of an existing
        "   OR (:endDate BETWEEN start_date AND end_date)" + // end date of new allocation is within the range of an existing
        "   OR (:startDate <= start_date AND :endDate >= end_date))", // date range of new allocation includes/covers/overlaps fully with the range of an existing
        nativeQuery = true)
    List<BlockEntity> findOverlappingWith(Integer selfBlockId, Integer propertyId, Instant startDate, Instant endDate);
}
