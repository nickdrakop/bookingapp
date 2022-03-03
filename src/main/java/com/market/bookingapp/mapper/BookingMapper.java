/**
 @author nick.drakopoulos
 */

package com.market.bookingapp.mapper;

import com.market.bookingapp.domain.BookingEntity;
import com.market.bookingapp.dto.BookingDto;
import com.market.bookingapp.util.DateFormatterUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BookingMapper {

    private final DateFormatterUtil dateFormatterUtil;

    public BookingEntity mapToBookingEntity(BookingDto bookingDto) {
        BookingEntity bookingEntity = new BookingEntity();

        bookingEntity.setId(bookingDto.getId());
        bookingEntity.setPropertyId(bookingDto.getPropertyId());
        bookingEntity.setGuestId(bookingDto.getGuestId());
        bookingEntity.setStartDate(dateFormatterUtil.getInstant(bookingDto.getStartDate()));
        bookingEntity.setEndDate(dateFormatterUtil.getInstant(bookingDto.getEndDate()));
        bookingEntity.setStatus(bookingDto.getStatus());

        return bookingEntity;
    }

    public BookingDto mapToBookingDto(BookingEntity entity) {
        BookingDto dto = new BookingDto();

        dto.setId(entity.getId());
        dto.setPropertyId(entity.getPropertyId());
        dto.setGuestId(entity.getGuestId());
        dto.setStartDate(dateFormatterUtil.instantToStringTimezonedDate(entity.getStartDate()));
        dto.setEndDate(dateFormatterUtil.instantToStringTimezonedDate(entity.getEndDate()));
        dto.setStatus(entity.getStatus());

        return dto;
    }

    public List<BookingDto> mapToBookingDtos(List<BookingEntity> bookingEntityList) {
        if (bookingEntityList == null) {
            return null;
        }

        return bookingEntityList.stream()
                .map(this::mapToBookingDto)
                .collect(Collectors.toList());
    }
}
