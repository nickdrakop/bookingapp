/**
 @author nick.drakopoulos
 */
package com.market.bookingapp.mapper

import com.market.bookingapp.AbstractSpec
import com.market.bookingapp.domain.BookingEntity
import com.market.bookingapp.dto.BookingDto
import com.market.bookingapp.dto.BookingStatus

import java.time.Instant

class BookingMapperSpec extends AbstractSpec {

    BookingMapper mapper
    Instant someInstant

    Integer expectedId
    Integer propertyId
    def setup() {
        someInstant = Instant.now()
        mapper = new BookingMapper(dateFormatterUtil)
        expectedId = 5
        propertyId = 4
    }

    def "should successfully call the mapToBookingDto method"() {
        given:
            BookingEntity bookingEntity = new BookingEntity(id: expectedId, propertyId: 1, guestId: 1,
                    startDate: from, endDate: to, status: BookingStatus.CANCELLED, createdAt: from)

        when:
            BookingDto dto = mapper.mapToBookingDto(bookingEntity)

        then:
            assertBookingEntityEqualsDto(bookingEntity, dto)
            0 * _
    }

    def "should successfully call the mapToBookingEntity method"() {
        given:
            BookingDto bookingDto = new BookingDto(propertyId: 1, guestId: 1, startDate: fromString, endDate: toString)

        when:
            BookingEntity entity = mapper.mapToBookingEntity(bookingDto)

        then:
            assertBookingEntityEqualsDto(entity, bookingDto)
            0 * _
    }

    def "should successfully call the mapToBookingDtos method"() {
        given:
            List<BookingEntity> entities = [
                    new BookingEntity(id: 1, propertyId: 1, guestId: 1, startDate: from,
                            endDate: to, status: BookingStatus.CANCELLED, createdAt: from),
                    new BookingEntity(id: 2, propertyId: 2, guestId: 1, startDate: from,
                            endDate: to, status: BookingStatus.ACTIVE, createdAt: from)]

        when:
            List<BookingDto> dtos = mapper.mapToBookingDtos(entities)

        then:
            assertBookingEntitiesEqualsDtos(entities, dtos)
            0 * _
    }

    private static assertBookingEntitiesEqualsDtos(Collection<BookingEntity> entities, Collection<BookingDto> dtos) {
        dtos.forEach{
            dto ->
                BookingEntity foundEntity = entities.find { entity -> entity.id == dto.id }
                assertBookingEntityEqualsDto(foundEntity, dto)
        }
        true
    }

    private static assertBookingEntityEqualsDto(BookingEntity entity, BookingDto dto) {
        assert entity.id == dto.id

        assert entity.propertyId == dto.propertyId
        assert entity.guestId == dto.guestId
        assert dateFormatterUtil.instantToStringTimezonedDate(entity.startDate) == dto.startDate
        assert dateFormatterUtil.instantToStringTimezonedDate(entity.endDate) == dto.endDate
        assert entity.status == dto.status
        entity
    }
}
