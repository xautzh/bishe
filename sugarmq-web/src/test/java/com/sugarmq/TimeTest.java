package com.sugarmq;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTest {
    public static void main(String[] args) {
        Date currentTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(currentTime);
        System.out.println(dateString);
        ParsePosition parsePosition = new ParsePosition(8);
        Date currentTime_2 = format.parse(dateString,parsePosition);
        System.out.println(currentTime_2);
    }
}
