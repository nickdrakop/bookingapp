/**
 @author nick.drakopoulos
 */
package com.market.bookingapp.service

import com.market.bookingapp.AbstractSpec
import com.market.bookingapp.dao.BookingDao
import com.market.bookingapp.domain.BookingEntity
import com.market.bookingapp.dto.BookingDto
import com.market.bookingapp.dto.BookingOverlapDetectorRequestDto
import com.market.bookingapp.dto.BookingStatus
import com.market.bookingapp.dto.TimeAllocationType
import com.market.bookingapp.exception.AppError
import com.market.bookingapp.exception.ApplicationException
import com.market.bookingapp.mapper.BookingMapper
import spock.lang.Unroll

class BookingServiceSpec extends AbstractSpec {
    BookingService service

    BookingDao mockedBookingDao
    BookingMapper mockedBookingMapper
    BookingOverlapDetector mockedBookingOverlapDetector

    Integer expectedId
    Integer propertyId
    List<BookingDto> expectedBookingDtos
    List<BookingEntity> expectedBookingEntities
    BookingDto expectedBookingDto
    BookingEntity expectedBookingEntity

    def setup() {
        expectedId = 5
        expectedBookingDto = Mock(BookingDto)
        expectedBookingEntity = Mock(BookingEntity)
        expectedBookingDtos = Mock(List)
        expectedBookingEntities = Mock(List)
        propertyId = 4
        mockedBookingDao = Mock(BookingDao)
        mockedBookingMapper = Mock(BookingMapper)
        mockedBookingOverlapDetector = Mock(BookingOverlapDetector)
        service = new BookingService(mockedBookingDao, mockedBookingMapper, mockedBookingOverlapDetector)
    }

    def "should successfully call the fetch method"() {
        when:
            BookingDto booking = service.fetch(expectedId)

        then:
            expectedBookingDto == booking

        and:
            1 * mockedBookingMapper.mapToBookingDto(expectedBookingEntity) >> expectedBookingDto
            1 * mockedBookingDao.findById(expectedId) >> Optional.of(expectedBookingEntity)
            0 * _
    }

    def "should return booking not found exception when call the fetch method"() {
        when:
            service.fetch(expectedId)

        then:
            ApplicationException e = thrown(ApplicationException)
            e.appError == AppError.BOOKING_NOT_FOUND

        and:
            1 * mockedBookingDao.findById(expectedId) >> Optional.empty()
            0 * _
    }

    def "should successfully call the fetchAll method"() {
        when:
            List<BookingDto> bookings = service.fetchAll()

        then:
            expectedBookingDtos == bookings

        and:
            1 * mockedBookingMapper.mapToBookingDtos(expectedBookingEntities) >> expectedBookingDtos
            1 * mockedBookingDao.findAll() >> expectedBookingEntities
            0 * _
    }

    @Unroll
    def "should successfully call the save method - #scenario"() {
        given:
            BookingDto bookingDto = new BookingDto(propertyId: 1, guestId: 1, startDate: fromString,
                    endDate: toString, status: givenStatus)

        when:
            Integer bookingId = service.save(bookingDto)

        then:
            expectedId == bookingId

        and:
            1 * mockedBookingMapper.mapToBookingEntity(_ as BookingDto) >> {
                BookingDto dto ->
                    assert dto.status == actualStoredStatus
                    return expectedBookingEntity
            }
            1 * expectedBookingEntity.getPropertyId() >> 1
            1 * expectedBookingEntity.getStartDate() >> from
            1 * expectedBookingEntity.getEndDate() >> to
            1 * mockedBookingOverlapDetector.overlapsWithActiveBookingOrBlock(_ as BookingOverlapDetectorRequestDto) >> {
                BookingOverlapDetectorRequestDto dto ->
                    assert dto.selfId.isEmpty()
                    assert dto.timeAllocationType == TimeAllocationType.BOOKING
                    assert dto.propertyId == 1
                    assert dto.startDate == from
                    assert dto.endDate == to
                    return false
            }
            1 * mockedBookingDao.save(expectedBookingEntity) >> expectedId
            0 * _

        where:
            givenStatus             | actualStoredStatus      | scenario
            BookingStatus.ACTIVE    | BookingStatus.ACTIVE    | "Booking should be active when give ACTIVE status"
            BookingStatus.CANCELLED | BookingStatus.CANCELLED | "Booking should be cancelled when give CANCELLED status"
            null                    | BookingStatus.ACTIVE    | "Booking should be active when we do not specify status"
    }

    def "should return overlap exception when call the save method"() {
        given:
            BookingDto bookingDto = new BookingDto(propertyId: 1, guestId: 1, startDate: fromString,
                    endDate: toString, status: BookingStatus.ACTIVE)

        when:
            service.save(bookingDto)

        then:
            ApplicationException e = thrown(ApplicationException)
            e.appError == AppError.OVERLAP_FOR_GIVEN_DATE_RANGE

        and:
            1 * mockedBookingMapper.mapToBookingEntity(_ as BookingDto) >> expectedBookingEntity
            1 * expectedBookingEntity.getPropertyId() >> 1
            1 * expectedBookingEntity.getStartDate() >> from
            1 * expectedBookingEntity.getEndDate() >> to
            1 * mockedBookingOverlapDetector.overlapsWithActiveBookingOrBlock(_ as BookingOverlapDetectorRequestDto) >> {
                BookingOverlapDetectorRequestDto dto ->
                    assert dto.selfId.isEmpty()
                    assert dto.timeAllocationType == TimeAllocationType.BOOKING
                    assert dto.propertyId == 1
                    assert dto.startDate == from
                    assert dto.endDate == to
                    return true
            }
            0 * _
    }

    @Unroll
    def "should return invalid params exception when call the save method - #scenario"() {
        given:
            BookingDto bookingDto = new BookingDto(propertyId: propertyIdVal, guestId: guestIdVal, startDate: startDateVal,
                    endDate: endDateVal, status: BookingStatus.ACTIVE)

        when:
            service.save(bookingDto)

        then:
            ApplicationException e = thrown(ApplicationException)
            e.appError == AppError.INVALID_PARAMETERS

        and:
            0 * _
        where:
            propertyIdVal  | guestIdVal | startDateVal | endDateVal   | scenario
            null           | 1          | fromString   | toString     | "propertyId is missing"
            1              | null       | fromString   | toString     | "guestId is missing"
            1              | 1          | null         | toString     | "startDate is missing"
            1              | 1          | fromString   | null         | "endDate is missing"
    }

    def "should successfully call the delete method"() {
        when:
            service.delete(expectedId)

        then:
           noExceptionThrown()

        and:
            1 * mockedBookingDao.deleteById(expectedId)
            0 * _
    }
}
