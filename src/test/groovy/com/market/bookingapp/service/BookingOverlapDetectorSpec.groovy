/**
 @author nick.drakopoulos
 */
package com.market.bookingapp.service

import com.market.bookingapp.AbstractSpec
import com.market.bookingapp.dao.BlockDao
import com.market.bookingapp.dao.BookingDao
import com.market.bookingapp.domain.BlockEntity
import com.market.bookingapp.domain.BookingEntity
import com.market.bookingapp.dto.BookingOverlapDetectorRequestDto
import com.market.bookingapp.dto.TimeAllocationType

class BookingOverlapDetectorSpec extends AbstractSpec {
    static List<BookingEntity> bookingEntities = [new BookingEntity(id: 1)]
    static List<BlockEntity> blockEntities = [new BlockEntity(id: 1)]

    BookingOverlapDetector service

    BookingDao mockedBookingDao
    BlockDao mockedBlockDao

    Integer propertyId

    def setup() {
        propertyId = 4
        mockedBookingDao = Mock(BookingDao)
        mockedBlockDao = Mock(BlockDao)
        service = new BookingOverlapDetector(mockedBookingDao, mockedBlockDao)
    }

    def "should successfully call the fetch method - no overlap"() {
        given:
            BookingOverlapDetectorRequestDto request = BookingOverlapDetectorRequestDto.builder()
                    .selfId(Optional.empty())
                    .timeAllocationType(TimeAllocationType.BOOKING)
                    .propertyId(propertyId)
                    .startDate(from)
                    .endDate(to).build()
        when:
            boolean overlap = service.overlapsWithActiveBookingOrBlock(request)

        then:
            !overlap

        and:
            1 * mockedBookingDao.findOverlappingWith(Optional.empty(), propertyId, from, to) >> Collections.emptyList()
            1 * mockedBlockDao.findOverlappingWith(Optional.empty(), propertyId, from, to) >> Collections.emptyList()
            0 * _
    }

    def "should successfully call the fetch method - overlap in bookings"() {
        given:
            BookingOverlapDetectorRequestDto request = BookingOverlapDetectorRequestDto.builder()
                    .selfId(Optional.empty())
                    .timeAllocationType(TimeAllocationType.BOOKING)
                    .propertyId(propertyId)
                    .startDate(from)
                    .endDate(to).build()
        when:
            boolean overlap = service.overlapsWithActiveBookingOrBlock(request)

        then:
            overlap

        and:
            1 * mockedBookingDao.findOverlappingWith(Optional.empty(), propertyId, from, to) >> bookingEntities
            0 * _
    }

    def "should successfully call the fetch method - overlap in blocks"() {
        given:
            BookingOverlapDetectorRequestDto request = BookingOverlapDetectorRequestDto.builder()
                    .selfId(Optional.empty())
                    .timeAllocationType(TimeAllocationType.BOOKING)
                    .propertyId(propertyId)
                    .startDate(from)
                    .endDate(to).build()
        when:
            boolean overlap = service.overlapsWithActiveBookingOrBlock(request)

        then:
            overlap

        and:
            1 * mockedBookingDao.findOverlappingWith(Optional.empty(), propertyId, from, to) >> Collections.emptyList()
            1 * mockedBlockDao.findOverlappingWith(Optional.empty(), propertyId, from, to) >> blockEntities
            0 * _
    }
}
