/**
 @author nick.drakopoulos
 */
package com.market.bookingapp.api

import com.market.bookingapp.AbstractSpec
import com.market.bookingapp.dto.BlockDto
import com.market.bookingapp.dto.BookingDto
import com.market.bookingapp.dto.BookingStatus
import com.market.bookingapp.service.BlockService
import com.market.bookingapp.service.BookingService

class BookingApiSpec extends AbstractSpec {

    BookingApi api
    BookingService mockedBookingService
    BlockService mockedBlockService

    Integer expectedId
    List<BookingDto> expectedBookings
    List<BlockDto> expectedBlocks
    BookingDto expectedBooking

    def setup() {
        expectedId = 5
        mockedBookingService = Mock(BookingService)
        mockedBlockService = Mock(BlockService)
        api = new BookingApi(mockedBookingService, mockedBlockService)
    }

    def "should successfully call the getBooking method api"() {
        when:
            BookingDto booking = api.getBooking(expectedId)

        then:
            expectedBooking == booking

        and:
            1 * mockedBookingService.fetch(expectedId) >> expectedBooking
            0 * _
    }

    def "should successfully call the getAllBookings method api"() {
        when:
            List<BookingDto> allBookings = api.getAllBookings()

        then:
            expectedBookings == allBookings

        and:
            1 * mockedBookingService.fetchAll() >> expectedBookings
            0 * _
    }

    def "should successfully call the createBooking method api"() {
        given:
            BookingDto bookingDto = new BookingDto(propertyId: 1, guestId: 1, startDate: fromString, endDate: toString)
        when:
            Integer id = api.createBooking(bookingDto)

        then:
            expectedId == id

        and:
            1 * mockedBookingService.save(bookingDto) >> expectedId
            0 * _
    }

    def "should successfully call the updateBooking method api to cancel a booking"() {
        given:
            BookingDto bookingDto = new BookingDto(id: expectedId, propertyId: 1, guestId: 1,
                    startDate: fromString, endDate: toString, status: BookingStatus.CANCELLED.name())
        when:
            Integer id = api.updateBooking(bookingDto)

        then:
            expectedId == id

        and:
            1 * mockedBookingService.update(bookingDto) >> expectedId
            0 * _
    }

    def "should successfully call the deleteBooking method api to delete a booking"() {
        when:
            api.deleteBooking(expectedId)

        then:
            noExceptionThrown()

        and:
            1 * mockedBookingService.delete(expectedId)
            0 * _
    }

    def "should successfully call the getAllBlocks method api"() {
        when:
            List<BlockDto> allBlocks = api.getAllBlocks()

        then:
            expectedBlocks == allBlocks

        and:
            1 * mockedBlockService.fetchAll() >> expectedBlocks
            0 * _
    }

    def "should successfully call the createBlock method api"() {
        given:
            BlockDto blockDto = new BlockDto(propertyId: 1, startDate: fromString, endDate: toString)
        when:
            Integer id = api.createBlock(blockDto)

        then:
            expectedId == id

        and:
            1 * mockedBlockService.save(blockDto) >> expectedId
            0 * _
    }

    def "should successfully call the deleteBlock method api to delete a block"() {
        when:
            api.deleteBlock(expectedId)

        then:
            noExceptionThrown()

        and:
            1 * mockedBlockService.delete(expectedId)
            0 * _
    }
}
