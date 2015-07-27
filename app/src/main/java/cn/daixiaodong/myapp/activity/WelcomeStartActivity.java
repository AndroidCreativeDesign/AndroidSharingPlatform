package cn.daixiaodong.myapp.activity;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.echo.sharing.utils.SharedUtils;

import cn.daixiaodong.myapp.R;

public class WelcomeStartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_start);
        //使用java中的定时器进行处理,延时3秒进行跳转
        Timer timer = new Timer();
        timer.schedule(new Task(), 3000);//定时器延时执行任务的方法
    }

    class Task extends TimerTask{
        @Override
        public void run() {
            //实现页面的跳转
            //不是第一次启动
            if (SharedUtils.getWelcomeBoolean(getBaseContext())) {
                startActivity(new Intent(getBaseContext(), MainActivity_.class));
            }else{
                startActivity(new Intent(WelcomeStartActivity.this, WelcomeGuideActivity.class));
                //保存访问记录
                SharedUtils.putWelcomeBoolean(getBaseContext(), true);
            }
            finish();
        }
    }
}
