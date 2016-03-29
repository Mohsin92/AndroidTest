/**
 * Logger
 * Author 	: Jitesh Rana <jhrana@gmail.com>
 * Company 	: Differenz Systems
 * Date		: 7-May-2014
 */
package com.example.administrator.swipe_fragment.utility;

import android.util.Log;

public final class Logger {

    public static void debugE(String className, String message) {
        Log.e(SipConfig.APP_VERSION, className + ":" + message);
    }

    public static void debugE(String message) {
        Log.e(SipConfig.APP_VERSION, message);
    }

    public static void debugV(String message) {
        Log.v(SipConfig.APP_VERSION, message);
    }

    public static void debugV(String className, String message) {
        Log.v(SipConfig.APP_VERSION, className + ":" + message);
    }
}
