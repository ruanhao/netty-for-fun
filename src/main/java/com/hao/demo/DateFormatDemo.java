package com.hao.demo;

import java.text.SimpleDateFormat;
import lombok.SneakyThrows;

public class DateFormatDemo {

    @SneakyThrows
    private static long parse(String dateString) {
        String pattern = "HH:mm:ss.SSS zzz EEE MMM dd yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.parse(dateString.trim()).getTime();
    }

    @SneakyThrows
    public static void main(String[] args) {
        long time1 = parse("18:44:19.471 UTC Tue Jun 14 2022");
        long time2 = parse("18:44:19.471 EDT Tue Jun 14 2022");

        System.err.println("time1: " + time1);
        System.err.println("time2: " + time2);

        long offset = (time1 - time2) / 1000 / 3600;
        System.err.println("offset: " + offset);
    }
}
