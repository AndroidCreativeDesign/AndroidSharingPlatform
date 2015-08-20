package life.z_turn.app.utils;

import android.util.Log;

import life.z_turn.app.BuildConfig;

/**
 * Log工具类
 * Created by daixiaodong on 15/8/8.
 */
public class LogUtil {

    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static void i(String tag, String message) {
        if (DEBUG) {
            Log.i(tag, message);
        }
    }

}
