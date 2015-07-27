package cn.daixiaodong.myapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import cn.daixiaodong.myapp.R;

public class WelcomeStartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_start);

        new Handler(new Handler.Callback(){
            @Override
            public boolean handleMessage(Message msg) {
                startActivity(new Intent(getApplicationContext(), MainActivity_.class));
                return false;
            }
        }).sendEmptyMessageDelayed(0, 3000);
    }
}
