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
public class BookingDto {

    private Integer id;

    @NotNull
    private Integer propertyId;

    @NotNull
    private Integer guestId;

    @NotEmpty
    private String startDate; // inclusive

    @NotEmpty
    private String endDate; // exclusive

    private BookingStatus status;
}
