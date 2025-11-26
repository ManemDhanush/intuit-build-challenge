package com.example.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SimpleLogger {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public static void log(String message) {
        String time = LocalTime.now().format(formatter);
        String thread = Thread.currentThread().getName();
        System.out.printf("[%s] [%-15s] %s%n", time, thread, message);
    }

    public static void error(String message) {
        String time = LocalTime.now().format(formatter);
        String thread = Thread.currentThread().getName();
        System.err.printf("[%s] [%-15s] ERROR: %s%n", time, thread, message);
    }
}
