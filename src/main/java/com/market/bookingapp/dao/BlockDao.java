/**
 @author nick.drakopoulos
 */

package com.market.bookingapp.dao;

import com.market.bookingapp.domain.BlockEntity;
import com.market.bookingapp.repository.BlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

@Component
@RequiredArgsConstructor
public class BlockDao {

    private final BlockRepository repository;

    public Optional<BlockEntity> findById(Integer id) {
        return repository.findById(id);
    }

    public List<BlockEntity> findAll() {
        return repository.findAll();
    }

    public List<BlockEntity> findOverlappingWith(Optional<Integer> selfBlockId, Integer propertyId, Instant startDate, Instant endDate) {
        checkArgument(propertyId != null, "PropertyId is mandatory!");
        checkArgument(startDate != null, "StartDate is mandatory!");
        checkArgument(endDate != null, "EndDate is mandatory!");
        return repository.findOverlappingWith(selfBlockId.orElse(null), propertyId, startDate, endDate);
    }

    public Integer save(BlockEntity entity) {
        return Optional.ofNullable(repository.save(entity))
            .map(BlockEntity::getId)
            .orElse(null);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

}
