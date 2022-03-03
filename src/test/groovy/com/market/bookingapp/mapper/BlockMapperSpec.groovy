/**
 @author nick.drakopoulos
 */
package com.market.bookingapp.mapper

import com.market.bookingapp.AbstractSpec
import com.market.bookingapp.domain.BlockEntity
import com.market.bookingapp.dto.BlockDto

import java.time.Instant

class BlockMapperSpec extends AbstractSpec {

    BlockMapper mapper
    Instant someInstant

    Integer expectedId
    Integer propertyId
    def setup() {
        someInstant = Instant.now()
        mapper = new BlockMapper(dateFormatterUtil)
        expectedId = 5
        propertyId = 4
    }

    def "should successfully call the mapToBlockDto method"() {
        given:
            BlockEntity blockEntity = new BlockEntity(id: expectedId, propertyId: 1, startDate: from, endDate: to, createdAt: from)

        when:
            BlockDto dto = mapper.mapToBlockDto(blockEntity)

        then:
            assertBlockEntityEqualsDto(blockEntity, dto)
            0 * _
    }

    def "should successfully call the mapToBlockEntity method"() {
        given:
            BlockDto blockDto = new BlockDto(propertyId: 1, startDate: fromString, endDate: toString)

        when:
            BlockEntity entity = mapper.mapToBlockEntity(blockDto)

        then:
            assertBlockEntityEqualsDto(entity, blockDto)
            0 * _
    }

    def "should successfully call the mapToBlockDtos method"() {
        given:
            List<BlockEntity> entities = [
                    new BlockEntity(id: 1, propertyId: 1, startDate: from, endDate: to, createdAt: from),
                    new BlockEntity(id: 2, propertyId: 2, startDate: from, endDate: to, createdAt: from)]

        when:
            List<BlockDto> dtos = mapper.mapToBlockDtos(entities)

        then:
            assertBlockEntitiesEqualsDtos(entities, dtos)
            0 * _
    }

    private static assertBlockEntitiesEqualsDtos(Collection<BlockEntity> entities, Collection<BlockDto> dtos) {
        dtos.forEach{
            dto ->
                BlockEntity foundEntity = entities.find { entity -> entity.id == dto.id }
                assertBlockEntityEqualsDto(foundEntity, dto)
        }
        true
    }

    private static assertBlockEntityEqualsDto(BlockEntity entity, BlockDto dto) {
        assert entity.id == dto.id

        assert entity.propertyId == dto.propertyId
        assert dateFormatterUtil.instantToStringTimezonedDate(entity.startDate) == dto.startDate
        assert dateFormatterUtil.instantToStringTimezonedDate(entity.endDate) == dto.endDate
        entity
    }
}
