package com.eprotech.rates.server.service.util;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by mercury on 21-Aug-16.
 */
public class DateTimeParser {

    @Test
    public void test() {
        String time = "2016-08-21T22:38:00.0Z";

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
        System.out.println(dateTime.toInstant(ZoneOffset.UTC).toEpochMilli());
        System.out.println(System.currentTimeMillis());

        AtomicLong heartbeatDuration = new AtomicLong();
        heartbeatDuration.set(System.currentTimeMillis());
        System.out.println(heartbeatDuration.get());
        heartbeatDuration.getAndUpdate(operand -> (dateTime.toInstant(ZoneOffset.UTC).toEpochMilli() - operand));
        System.out.println(heartbeatDuration.get());

    }
}
