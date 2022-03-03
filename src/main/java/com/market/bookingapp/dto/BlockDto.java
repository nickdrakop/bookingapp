/**
 * @author nick.drakopoulos
 */

package com.market.bookingapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BlockDto {

    private Integer id;

    @NotNull
    private Integer propertyId;

    @NotEmpty
    private String startDate;

    @NotEmpty
    private String endDate;
}
