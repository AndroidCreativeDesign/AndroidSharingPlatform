package life.z_turn.app.utils;

import android.util.Log;

import com.avos.avoscloud.AVException;

import life.z_turn.app.BuildConfig;

/**
 *  异常处理 工具类
 * Created by daixiaodong on 15/8/8.
 */
public class ExceptionUtil {
    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static void printStackTrace(Exception e) {
        if (DEBUG) {
            if(e instanceof AVException){
                int errorCode = ((AVException) e).getCode();
                Log.e("AVException","ErrorCode:"+errorCode+"--->"+"Message:"+e.getMessage());
                return;
            }
            e.printStackTrace();
        }
    }
}
