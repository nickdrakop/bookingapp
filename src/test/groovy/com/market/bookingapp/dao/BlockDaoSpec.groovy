/**
 @author nick.drakopoulos
 */
package com.market.bookingapp.dao

import com.market.bookingapp.AbstractSpec
import com.market.bookingapp.domain.BlockEntity
import com.market.bookingapp.repository.BlockRepository
import spock.lang.Unroll

class BlockDaoSpec extends AbstractSpec {
    BlockDao dao
    BlockRepository mockedBlockRepository

    Integer expectedId
    Integer propertyId
    List<BlockEntity> expectedBlocks
    BlockEntity expectedBlock

    def setup() {
        expectedId = 5
        expectedBlock = Mock(BlockEntity)
        expectedBlocks = Mock(List)
        propertyId = 4
        mockedBlockRepository = Mock(BlockRepository)
        dao = new BlockDao(mockedBlockRepository)
    }

    def "should successfully call the findById method"() {
        when:
            Optional<BlockEntity> block = dao.findById(expectedId)

        then:
            expectedBlock == block.get()

        and:
            1 * mockedBlockRepository.findById(expectedId) >> Optional.of(expectedBlock)
            0 * _
    }

    def "should successfully call the findAll method"() {
        when:
            List<BlockEntity> blocks = dao.findAll()

        then:
            expectedBlocks == blocks

        and:
            1 * mockedBlockRepository.findAll() >> expectedBlocks
            0 * _
    }

    def "should successfully call the findOverlappingWith method"() {
        when:
            List<BlockEntity> blocks = dao.findOverlappingWith(Optional.empty(), propertyId, from, to)

        then:
            expectedBlocks == blocks

        and:
            1 * mockedBlockRepository.findOverlappingWith(null, propertyId, from, to) >> expectedBlocks
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
            1             | from      | null     | "EndDate is mandatory!"    | "to param is empty"
            null          | from      | null     | "PropertyId is mandatory!" | "propertyId param is empty"
    }

    def "should successfully call the save method"() {
        given:
            BlockEntity blockEntity = new BlockEntity(id: expectedId, propertyId: 1, startDate: from,
                    endDate: to, createdAt: from)

        when:
            Integer blockId = dao.save(blockEntity)

        then:
            expectedId == blockId

        and:
            1 * expectedBlock.id >> expectedId
            1 * mockedBlockRepository.save(blockEntity) >> expectedBlock
            0 * _
    }

    def "should successfully call the deleteById method"() {
        when:
            dao.deleteById(expectedId)

        then:
            noExceptionThrown()

        and:
            1 * mockedBlockRepository.deleteById(expectedId)
            0 * _
    }

}
