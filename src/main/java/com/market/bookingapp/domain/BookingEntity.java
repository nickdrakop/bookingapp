/**
 * @author nick.drakopoulos
 */

package com.market.bookingapp.domain;

import com.market.bookingapp.dto.BookingStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "booking")
public class BookingEntity extends AbstractEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "property_id")
    private Integer propertyId;

    @Column(name = "guest_id")
    private Integer guestId;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
