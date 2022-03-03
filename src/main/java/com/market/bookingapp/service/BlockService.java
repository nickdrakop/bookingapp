/**
 @author nick.drakopoulos
 */

package com.market.bookingapp.service;

import com.market.bookingapp.dao.BlockDao;
import com.market.bookingapp.domain.BlockEntity;
import com.market.bookingapp.dto.BlockDto;
import com.market.bookingapp.dto.BookingDto;
import com.market.bookingapp.dto.BookingOverlapDetectorRequestDto;
import com.market.bookingapp.dto.TimeAllocationType;
import com.market.bookingapp.exception.AppError;
import com.market.bookingapp.exception.ApplicationException;
import com.market.bookingapp.mapper.BlockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Manages blocks.
 * A block is when the property owner selects a start and end date where no one can make a booking on the dates
 * within the date range.
 */
@Component
@RequiredArgsConstructor
public class BlockService {

    private final BlockDao blockDao;
    private final BlockMapper blockMapper;
    private final BookingOverlapDetector bookingOverlapDetector;

    public List<BlockDto> fetchAll() {
        return blockMapper.mapToBlockDtos(blockDao.findAll());
    }

    public Integer save(BlockDto blockDto) {
        validateForCreate(blockDto);
        BlockEntity entityToSave = blockMapper.mapToBlockEntity(blockDto);
        BookingOverlapDetectorRequestDto requestDto = BookingOverlapDetectorRequestDto.builder()
            .selfId(Optional.empty())
            .timeAllocationType(TimeAllocationType.BLOCK)
            .propertyId(entityToSave.getPropertyId())
            .startDate(entityToSave.getStartDate())
            .endDate(entityToSave.getEndDate()).build();

        if (bookingOverlapDetector.overlapsWithActiveBookingOrBlock(requestDto)) {
            throw new ApplicationException(AppError.OVERLAP_FOR_GIVEN_DATE_RANGE);
        }
        return blockDao.save(entityToSave);
    }

    public void delete(Integer id) {
        try {
            blockDao.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ApplicationException(AppError.BLOCK_NOT_FOUND);
        }
    }

    private void validateForCreate(BlockDto blockDto) {
        if (blockDto.getPropertyId() == null || blockDto.getStartDate() == null || blockDto.getEndDate() == null) {
            throw new ApplicationException(AppError.INVALID_PARAMETERS);
        }
    }
}
