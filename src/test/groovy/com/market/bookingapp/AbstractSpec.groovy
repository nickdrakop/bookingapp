package com.market.bookingapp

import com.market.bookingapp.util.DateFormatterUtil
import spock.lang.Specification

import java.time.Instant

class AbstractSpec extends Specification {
    static DateFormatterUtil dateFormatterUtil = new DateFormatterUtil("Europe/Athens", "yyyy-MM-dd HH:mm:ss")
    static String fromString = "2022-03-02 08:00:00"
    static Instant from = dateFormatterUtil.getInstant("2022-03-02 08:00:00")
    static String toString = "2022-03-03 08:00:00"
    static Instant to = dateFormatterUtil.getInstant("2022-03-03 08:00:00")
}
