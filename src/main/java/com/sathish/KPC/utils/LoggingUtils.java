package com.sathish.KPC.utils;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoggingUtils {
    public static void doLogInfoWithMessage(String message) {
        log.info(message);
    }

    public static void doLogWarnWithMessage(String message) {
        log.warn(message);
    }

    public static void doLogErrorWithMessage(String message) {
        log.error(message);
    }

    public static void doLogInfoWithMessageAndObject(String message, Object... object) {
        log.info(message, object);
    }

    public static void doLogWarnWithMessageAndObject(String message, Object... object) {
        log.warn(message, object);
    }

    public static void doLogErrorWithMessageAndObject(String message, Object... object) {
        log.error(message, object);
    }
}
