/**
 @author nick.drakopoulos
 */

package com.market.bookingapp.mapper;

import com.market.bookingapp.domain.BlockEntity;
import com.market.bookingapp.dto.BlockDto;
import com.market.bookingapp.util.DateFormatterUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BlockMapper {

    private final DateFormatterUtil dateFormatterUtil;

    public BlockEntity mapToBlockEntity(BlockDto blockDto) {
        BlockEntity blockEntity = new BlockEntity();

        blockEntity.setId(blockDto.getId());
        blockEntity.setPropertyId(blockDto.getPropertyId());
        blockEntity.setStartDate(dateFormatterUtil.getInstant(blockDto.getStartDate()));
        blockEntity.setEndDate(dateFormatterUtil.getInstant(blockDto.getEndDate()));

        return blockEntity;
    }

    public BlockDto mapToBlockDto(BlockEntity entity) {
        BlockDto dto = new BlockDto();

        dto.setId(entity.getId());
        dto.setPropertyId(entity.getPropertyId());
        dto.setStartDate(dateFormatterUtil.instantToStringTimezonedDate(entity.getStartDate()));
        dto.setEndDate(dateFormatterUtil.instantToStringTimezonedDate(entity.getEndDate()));

        return dto;
    }

    public List<BlockDto> mapToBlockDtos(List<BlockEntity> blockEntityList) {
        if (blockEntityList == null) {
            return null;
        }

        return blockEntityList.stream()
                .map(this::mapToBlockDto)
                .collect(Collectors.toList());
    }
}
