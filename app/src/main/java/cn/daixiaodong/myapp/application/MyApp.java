package cn.daixiaodong.myapp.application;

import android.app.Application;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;

/**
 * Created by daixiaodong on 15/7/11.
 */
public class MyApp extends Application {
    public void onCreate() {
        // 请用你的AppId，AppKey。并在管理台启用手机号码短信验证
        AVOSCloud.initialize(this, "89g57jiok2kx2p50joq1ieuvjgeqplyhle3xskfgnm219c7n",
                "mg7ay31hkmyjnr66j5m1sewoebf1ht7gwttghzq5b4bigpsz");

        AVAnalytics.enableCrashReport(this, true);
        
    }
}
