package org.javacore.time;

import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

public class Zone {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat.format(new Date()));

        Date date = new Date();
        date = DateUtils.addDays(date, 7);
        date = DateUtils.setHours(date, 10);
        date = DateUtils.setMilliseconds(date, 0);
        date = DateUtils.setMinutes(date, 0);
        date = DateUtils.setSeconds(date, 0);

        String str = simpleDateFormat.format(date);
        System.out.println(str);
        System.out.println(date.getTime());

        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(ZoneOffset.of("+09:00")));
        System.out.println(simpleDateFormat.format(date));
        date = simpleDateFormat.parse(str);
        System.out.println(date.getTime());
        System.out.println(simpleDateFormat.format(date));


        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(ZoneOffset.of("+00:00")));
        System.out.println(simpleDateFormat.format(date));

        System.out.println(date.getTime());

    }

}
