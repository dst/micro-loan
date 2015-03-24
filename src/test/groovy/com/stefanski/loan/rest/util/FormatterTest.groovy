package com.stefanski.loan.rest.util

import spock.lang.Specification

import java.time.LocalDate

import static java.time.Month.*

/**
 * @author Dariusz Stefanski
 */
class FormatterTest extends Specification {
    def "should correctly format time"() {
        expect:
            Formatter.formatTime(time.atStartOfDay()) == formatted
        where:
            time                             | formatted
            LocalDate.of(2014, JUNE, 16)     | "2014-06-16"
            LocalDate.of(2014, DECEMBER, 16) | "2014-12-16"
            LocalDate.of(2000, JANUARY, 1)   | "2000-01-01"
    }
}
