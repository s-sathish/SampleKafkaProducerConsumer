package com.sathish.KPC.utils;

import java.sql.Timestamp;

public class CommonUtils {

    public static long getCurrentTimeStamp() {
        return new Timestamp(System.currentTimeMillis()).getTime();
    }
}
