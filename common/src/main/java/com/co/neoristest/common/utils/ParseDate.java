package com.co.neoristest.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParseDate {

    private ParseDate(){

    }

    public static LocalDateTime formattDateTimeToParam(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return LocalDateTime.parse(date, formatter);
    }
}
