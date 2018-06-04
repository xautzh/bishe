package com.sugarmq;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTest {
    public static void main(String[] args) {
        Date currentTime = new Date();
        long time = currentTime.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(currentTime);
        System.out.println(dateString);
        String timeString = format.format(time);
        System.out.println("改编后的"+timeString);
        System.out.println(new Date());
        System.out.println(new Date().getTime());
    }
}
