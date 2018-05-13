package com.zhuanghongji.screenshot.lib;

import android.util.Log;

/**
 * MLog
 *
 * @author zhuanghongji
 */

public class MLog {

    private static final String TAG = "ScreenshotManager";

    private static boolean isLogEnable = false;

    public static void setLogEnableState(boolean isLogEnable) {
        MLog.isLogEnable = isLogEnable;
    }

    public static void log(String msg, Object... args) {
        if (!isLogEnable) {
            return;
        }
        String message = String.format(msg, args);
        Log.v(TAG, message);
    }
}
