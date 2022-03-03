/**
 @author nick.drakopoulos
 */

package com.market.bookingapp.api;

import com.market.bookingapp.dto.BlockDto;
import com.market.bookingapp.dto.BookingDto;
import com.market.bookingapp.service.BlockService;
import com.market.bookingapp.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Manages bookings and blocks.
 * A booking is when a guest selects a start and end date and submits a reservation on a property.
 * A block is when the property owner selects a start and end date where no one can make a booking on the dates
 * within the date range.
 */
@RestController
@RequestMapping(path = "/api")
@RequiredArgsConstructor
public class BookingApi {
    private static final Logger LOG = LoggerFactory.getLogger(BookingApi.class);

    private final BookingService bookingService;
    private final BlockService blockService;

    @GetMapping(path = "/bookings/{id}")
    public BookingDto getBooking(@PathVariable Integer id) {

        LOG.info("A new request was received in getBooking");
        return bookingService.fetch(id);
    }

    @GetMapping(path = "/bookings")
    public List<BookingDto> getAllBookings() {

        LOG.info("A new request was received in getAllBookings");
        return bookingService.fetchAll();
    }

    @PostMapping(path = "/bookings")
    public Integer createBooking(@RequestBody @Valid BookingDto bookingDto) {

        LOG.info("A new request was received in createBooking with request: {}", bookingDto);
        return bookingService.save(bookingDto);
    }

    @PutMapping(path = "/bookings")
    public Integer updateBooking(@RequestBody @Valid BookingDto bookingDto) {

        LOG.info("A new request was received in updateBooking with request: {}", bookingDto);
        return bookingService.update(bookingDto);
    }

    @DeleteMapping(path = "/bookings/{id}")
    public void deleteBooking(@PathVariable Integer id) {
        LOG.info("A new request was received in deleteBooking");
        bookingService.delete(id);
    }

    @GetMapping(path = "/blocks")
    public List<BlockDto> getAllBlocks() {

        LOG.info("A new request was received in getAllBlocks");
        return blockService.fetchAll();
    }

    @PostMapping(path = "/blocks")
    public Integer createBlock(@RequestBody @Valid BlockDto blockDto) {

        LOG.info("A new request was received in createBlock with request: {}", blockDto);
        return blockService.save(blockDto);
    }

    @DeleteMapping(path = "/blocks/{id}")
    void deleteBlock(@PathVariable Integer id) {
        LOG.info("A new request was received in deleteBlock");
        blockService.delete(id);
    }
}
