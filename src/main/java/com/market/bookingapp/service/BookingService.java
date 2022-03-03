/**
 @author nick.drakopoulos
 */

package com.market.bookingapp.service;

import com.market.bookingapp.dao.BookingDao;
import com.market.bookingapp.domain.BookingEntity;
import com.market.bookingapp.dto.BookingDto;
import com.market.bookingapp.dto.BookingOverlapDetectorRequestDto;
import com.market.bookingapp.dto.BookingStatus;
import com.market.bookingapp.dto.TimeAllocationType;
import com.market.bookingapp.exception.AppError;
import com.market.bookingapp.exception.ApplicationException;
import com.market.bookingapp.mapper.BookingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Manages bookings.
 * A booking is when a guest selects a start and end date and submits a reservation on a property.
 */
@Component
@RequiredArgsConstructor
public class BookingService {

    private final BookingDao bookingDao;
    private final BookingMapper bookingMapper;
    private final BookingOverlapDetector bookingOverlapDetector;

    public BookingDto fetch(Integer id) {
        Optional<BookingEntity> bookingEntity = bookingDao.findById(id);
        if (bookingEntity.isEmpty()) {
            throw new ApplicationException(AppError.BOOKING_NOT_FOUND);
        }
        return bookingMapper.mapToBookingDto(bookingEntity.get());
    }

    public List<BookingDto> fetchAll() {
        return bookingMapper.mapToBookingDtos(bookingDao.findAll());
    }

    public Integer save(BookingDto bookingDto) {
        validateForCreate(bookingDto);
        if (bookingDto.getStatus() == null) {
            bookingDto.setStatus(BookingStatus.ACTIVE);
        }
        BookingEntity entityToSave = bookingMapper.mapToBookingEntity(bookingDto);
        BookingOverlapDetectorRequestDto requestDto = BookingOverlapDetectorRequestDto.builder()
            .selfId(Optional.empty())
            .timeAllocationType(TimeAllocationType.BOOKING)
            .propertyId(entityToSave.getPropertyId())
            .startDate(entityToSave.getStartDate())
            .endDate(entityToSave.getEndDate()).build();

        if (bookingOverlapDetector.overlapsWithActiveBookingOrBlock(requestDto)) {
            throw new ApplicationException(AppError.OVERLAP_FOR_GIVEN_DATE_RANGE);
        }
        return bookingDao.save(entityToSave);
    }

    public Integer update(BookingDto bookingDto) {
        validateForUpdate(bookingDto);
        Optional<BookingEntity> existingEntity = bookingDao.findById(bookingDto.getId());
        if (existingEntity.isEmpty()) {
            throw new ApplicationException(AppError.BOOKING_NOT_FOUND);
        }
        BookingEntity entityToSave = bookingMapper.mapToBookingEntity(bookingDto);
        // when it's for update, overlap check makes sense only when
        // 1) you switch from CANCELLED TO ACTIVE status OR
        // 2) you have an ACTIVE booking, and you actually change the dates
        boolean switchingFromCancelledToActive = BookingStatus.CANCELLED.equals(existingEntity.get().getStatus())
            && BookingStatus.ACTIVE.equals(entityToSave.getStatus());
        boolean startDateIsBeingChanged = !existingEntity.get().getStartDate().equals(entityToSave.getStartDate());
        boolean endDateIsBeingChanged = !existingEntity.get().getEndDate().equals(entityToSave.getEndDate());

        if (switchingFromCancelledToActive || (BookingStatus.ACTIVE.equals(entityToSave.getStatus())
            && (startDateIsBeingChanged || endDateIsBeingChanged))) {
            BookingOverlapDetectorRequestDto requestDto = BookingOverlapDetectorRequestDto.builder()
                .selfId(existingEntity.map(BookingEntity::getId))
                .timeAllocationType(TimeAllocationType.BOOKING)
                .propertyId(entityToSave.getPropertyId())
                .startDate(entityToSave.getStartDate())
                .endDate(entityToSave.getEndDate()).build();
            if (bookingOverlapDetector.overlapsWithActiveBookingOrBlock(requestDto)) {
                throw new ApplicationException(AppError.OVERLAP_FOR_GIVEN_DATE_RANGE);
            }
        }

        return bookingDao.save(entityToSave);
    }

    public void delete(Integer id) {
        try {
            bookingDao.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ApplicationException(AppError.BOOKING_NOT_FOUND);
        }
    }

    private void validateForUpdate(BookingDto bookingDto) {
        if (bookingDto.getId() == null || bookingDto.getPropertyId() == null || bookingDto.getGuestId() == null
            || bookingDto.getStartDate() == null || bookingDto.getEndDate() == null || bookingDto.getStatus() == null) {
            throw new ApplicationException(AppError.INVALID_PARAMETERS);
        }
    }

    private void validateForCreate(BookingDto bookingDto) {
        if (bookingDto.getPropertyId() == null || bookingDto.getGuestId() == null || bookingDto.getStartDate() == null
            || bookingDto.getEndDate() == null) {
            throw new ApplicationException(AppError.INVALID_PARAMETERS);
        }
    }
}
