package com.sample.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 날짜 유틸리티
 */
@Slf4j
public class DateUtils {

    /**
     * Date형의 값을 지정된 DateTimeFormatter포맷으로 한 값으로 반환한다.
     *
     * @param fromDate
     * @param formatter
     * @return
     */
    public static String format(final Date fromDate, final DateTimeFormatter formatter) {
        val zoneId = getZoneId();
        val localDateTime = fromDate.toInstant().atZone(zoneId).toLocalDateTime();
        val result = formatter.format(localDateTime);
        return result;
    }

    /**
     * LocalDateTime형의 값을 지정된 DateTimeFormatter포맷으로 한 값을 반환한다。
     * 
     * @param fromLocalDateTime
     * @param formatter
     * @return
     */
    public static String format(final LocalDateTime fromLocalDateTime, final DateTimeFormatter formatter) {
        val result = formatter.format(fromLocalDateTime);
        return result;
    }

    /**
     * Date형의 값을 LocalDateTime형으로 변환해서 반환한다.
     * 
     * @param fromDate
     * @return
     */
    public static LocalDateTime toLocalDateTime(final Date fromDate) {
        val zoneId = getZoneId();
        return fromDate.toInstant().atZone(zoneId).toLocalDateTime();
    }

    /**
     * LocalDateTime형의 값을 Date형으로 변환해서 반환한다.
     * 
     * @param fromLocalDateTime
     * @return
     */
    public static Date toDate(final LocalDateTime fromLocalDateTime) {
        val zoneId = getZoneId();
        val zoneDateTime = ZonedDateTime.of(fromLocalDateTime, zoneId);
        return Date.from(zoneDateTime.toInstant());
    }

    /**
     * LocalDate형의 값을 Date형으로 변환하여 반환한다.
     * 
     * @param localDate
     * @return
     */
    public static Date toDate(final LocalDate localDate) {
        val zoneId = getZoneId();
        val zoneDateTime = localDate.atStartOfDay(zoneId).toInstant();
        return Date.from(zoneDateTime);
    }

    protected static ZoneId getZoneId() {
        return ZoneId.systemDefault();
    }
}
