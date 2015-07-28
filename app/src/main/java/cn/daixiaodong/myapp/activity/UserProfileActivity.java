package cn.daixiaodong.myapp.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.androidannotations.annotations.EActivity;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;


/**
 *  用户个人资料 界面
 */
@EActivity(R.layout.activity_user_profile)
public class UserProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
