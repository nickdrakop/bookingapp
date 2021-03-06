/**
 @author nick.drakopoulos
 */
package com.market.bookingapp.util

import spock.lang.Specification
import spock.lang.Unroll

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.zone.ZoneRules

class DateFormatterUtilSpec extends Specification {


    DateFormatterUtil dateFormatterUtil
    ZoneId zoneId
    Boolean isDst

    def setup() {
        zoneId = ZoneId.of("Europe/Athens")
        dateFormatterUtil = new DateFormatterUtil("Europe/Athens", "yyyy-MM-dd HH:mm:ss")

        ZoneRules zoneRules = zoneId.getRules()
        isDst = zoneRules.isDaylightSavings(ZonedDateTime.now(zoneId).toInstant())
    }

    @Unroll
    def "successfully parse string to instant when givenDate is #givenDate"() {
        when:
            Instant result = dateFormatterUtil.getInstant(givenDate)

        then:
            result.toString() == (isDst ? expectedUtcTimeIfDST : expectedUtcTimeIfNotDST)

        where:
            givenDate             | expectedUtcTimeIfNotDST | expectedUtcTimeIfDST
            "2021-12-05 10:00:00" | "2021-12-05T08:00:00Z"  | "2021-12-05T07:00:00Z"
            "2021-12-01 00:30:00" | "2021-11-30T22:30:00Z"  | "2021-11-30T21:30:00Z"
    }


    @Unroll
    def "successfully format instant to String"() {
        given:
            Instant givenInstant = localTime.atZone(ZoneId.of(zoneId as String)).toInstant()

        when:
            String timezonedDateString = dateFormatterUtil.instantToStringTimezonedDate(givenInstant)

        then:
            expectedTimezonedDateString == timezonedDateString

        where:
            localTime | expectedTimezonedDateString
            LocalDateTime.of(2021, 12, 5, 10, 0) | "2021-12-05 10:00:00"
    }

}
