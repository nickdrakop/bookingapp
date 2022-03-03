/**
 @author nick.drakopoulos
 */
package com.market.bookingapp.service

import com.market.bookingapp.AbstractSpec
import com.market.bookingapp.dao.BlockDao
import com.market.bookingapp.domain.BlockEntity
import com.market.bookingapp.dto.BlockDto
import com.market.bookingapp.dto.BookingOverlapDetectorRequestDto
import com.market.bookingapp.dto.TimeAllocationType
import com.market.bookingapp.exception.AppError
import com.market.bookingapp.exception.ApplicationException
import com.market.bookingapp.mapper.BlockMapper
import spock.lang.Unroll

class BlockServiceSpec extends AbstractSpec {
    BlockService service

    BlockDao mockedBlockDao
    BlockMapper mockedBlockMapper
    BookingOverlapDetector mockedBookingOverlapDetector

    Integer expectedId
    Integer propertyId
    List<BlockDto> expectedBlockDtos
    List<BlockEntity> expectedBlockEntities
    BlockDto expectedBlockDto
    BlockEntity expectedBlockEntity

    def setup() {
        expectedId = 5
        expectedBlockDto = Mock(BlockDto)
        expectedBlockEntity = Mock(BlockEntity)
        expectedBlockDtos = Mock(List)
        expectedBlockEntities = Mock(List)
        propertyId = 4
        mockedBlockDao = Mock(BlockDao)
        mockedBlockMapper = Mock(BlockMapper)
        mockedBookingOverlapDetector = Mock(BookingOverlapDetector)
        service = new BlockService(mockedBlockDao, mockedBlockMapper, mockedBookingOverlapDetector)
    }

    def "should successfully call the fetchAll method"() {
        when:
            List<BlockDto> blocks = service.fetchAll()

        then:
            expectedBlockDtos == blocks

        and:
            1 * mockedBlockMapper.mapToBlockDtos(expectedBlockEntities) >> expectedBlockDtos
            1 * mockedBlockDao.findAll() >> expectedBlockEntities
            0 * _
    }

    def "should successfully call the save method"() {
        given:
            BlockDto blockDto = new BlockDto(propertyId: 1, startDate: fromString, endDate: toString)

        when:
            Integer blockId = service.save(blockDto)

        then:
            expectedId == blockId

        and:
            1 * mockedBlockMapper.mapToBlockEntity(_ as BlockDto) >> expectedBlockEntity
            1 * expectedBlockEntity.getPropertyId() >> 1
            1 * expectedBlockEntity.getStartDate() >> from
            1 * expectedBlockEntity.getEndDate() >> to
            1 * mockedBookingOverlapDetector.overlapsWithActiveBookingOrBlock(_ as BookingOverlapDetectorRequestDto) >> {
                BookingOverlapDetectorRequestDto dto ->
                    assert dto.selfId.isEmpty()
                    assert dto.timeAllocationType == TimeAllocationType.BLOCK
                    assert dto.propertyId == 1
                    assert dto.startDate == from
                    assert dto.endDate == to
                    return false
            }
            1 * mockedBlockDao.save(expectedBlockEntity) >> expectedId
            0 * _
    }

    def "should return overlap exception when call the save method"() {
        given:
            BlockDto blockDto = new BlockDto(propertyId: 1, startDate: fromString, endDate: toString)

        when:
            service.save(blockDto)

        then:
            ApplicationException e = thrown(ApplicationException)
            e.appError == AppError.OVERLAP_FOR_GIVEN_DATE_RANGE

        and:
            1 * mockedBlockMapper.mapToBlockEntity(_ as BlockDto) >> expectedBlockEntity
            1 * expectedBlockEntity.getPropertyId() >> 1
            1 * expectedBlockEntity.getStartDate() >> from
            1 * expectedBlockEntity.getEndDate() >> to
            1 * mockedBookingOverlapDetector.overlapsWithActiveBookingOrBlock(_ as BookingOverlapDetectorRequestDto) >> {
                BookingOverlapDetectorRequestDto dto ->
                    assert dto.selfId.isEmpty()
                    assert dto.timeAllocationType == TimeAllocationType.BLOCK
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
            BlockDto blockDto = new BlockDto(propertyId: propertyIdVal, startDate: startDateVal, endDate: endDateVal)

        when:
            service.save(blockDto)

        then:
            ApplicationException e = thrown(ApplicationException)
            e.appError == AppError.INVALID_PARAMETERS

        and:
            0 * _
        where:
            propertyIdVal  | startDateVal | endDateVal   | scenario
            null           | fromString   | toString     | "propertyId is missing"
            1              | null         | toString     | "startDate is missing"
            1              | fromString   | null         | "endDate is missing"
    }

    def "should successfully call the delete method"() {
        when:
            service.delete(expectedId)

        then:
           noExceptionThrown()

        and:
            1 * mockedBlockDao.deleteById(expectedId)
            0 * _
    }
}
