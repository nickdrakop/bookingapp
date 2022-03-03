/**
 @author nick.drakopoulos
 */
package com.market.bookingapp.dao

import com.market.bookingapp.AbstractSpec
import com.market.bookingapp.domain.BookingEntity
import com.market.bookingapp.dto.BookingStatus
import com.market.bookingapp.repository.BookingRepository
import spock.lang.Unroll

class BookingDaoSpec extends AbstractSpec {
    BookingDao dao
    BookingRepository mockedBookingRepository

    Integer expectedId
    Integer propertyId
    List<BookingEntity> expectedBookings
    BookingEntity expectedBooking

    def setup() {
        expectedId = 5
        expectedBooking = Mock(BookingEntity)
        expectedBookings = Mock(List)
        propertyId = 4
        mockedBookingRepository = Mock(BookingRepository)
        dao = new BookingDao(mockedBookingRepository)
    }

    def "should successfully call the findById method"() {
        when:
            Optional<BookingEntity> booking = dao.findById(expectedId)

        then:
            expectedBooking == booking.get()

        and:
            1 * mockedBookingRepository.findById(expectedId) >> Optional.of(expectedBooking)
            0 * _
    }

    def "should successfully call the findAll method"() {
        when:
            List<BookingEntity> bookings = dao.findAll()

        then:
            expectedBookings == bookings

        and:
            1 * mockedBookingRepository.findAll() >> expectedBookings
            0 * _
    }

    def "should successfully call the findOverlappingWith method"() {
        when:
            List<BookingEntity> bookings = dao.findOverlappingWith(Optional.empty(), propertyId, from, to)

        then:
            expectedBookings == bookings

        and:
            1 * mockedBookingRepository.findOverlappingWith(null, propertyId, from, to) >> expectedBookings
            0 * _
    }

    @Unroll
    def "missing params when call the findOverlappingWith method - #scenario"() {
        when:
            dao.findOverlappingWith(Optional.empty(), propertyIdVal, fromDate, toDate)

        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.getMessage() == errorMessage

        and:
            0 * _
        where:
            propertyIdVal | fromDate  | toDate   | errorMessage               | scenario
            1             | null      | null     | "StartDate is mandatory!"  | "both from and to params are empty"
            1             | null      | to       | "StartDate is mandatory!"  | "from param is empty"
            1             | from      | null     | "EndDate is mandatory!"    | "to param is empty"
            null          | from      | null     | "PropertyId is mandatory!" | "propertyId param is empty"
    }

    def "should successfully call the save method"() {
        given:
            BookingEntity bookingEntity = new BookingEntity(id: expectedId, propertyId: 1, guestId: 1,
                    startDate: from, endDate: to, status: BookingStatus.CANCELLED, createdAt: from)

        when:
            Integer bookingId = dao.save(bookingEntity)

        then:
            expectedId == bookingId

        and:
            1 * expectedBooking.id >> expectedId
            1 * mockedBookingRepository.save(bookingEntity) >> expectedBooking
            0 * _
    }

    def "should successfully call the deleteById method"() {
        when:
            dao.deleteById(expectedId)

        then:
            noExceptionThrown()

        and:
            1 * mockedBookingRepository.deleteById(expectedId)
            0 * _
    }

}
