package com.echo.mengxiang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.echo.mengxiang.utils.SharedUtils;

import org.androidannotations.annotations.EActivity;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.MainActivity_;
import cn.daixiaodong.myapp.activity.common.BaseActivity;

@EActivity(R.layout.activity_welcome_start)
public class WelcomeStartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		initStart();
	}
	public void initStart(){
		new Handler(new Handler.Callback() {
			//处理接收到的消息的方法
			@Override
			public boolean handleMessage(Message msg) {
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
				return false;
			}
		}).sendEmptyMessageDelayed(0, 3000);//表示延时三秒进行任务的执行
	}
}
