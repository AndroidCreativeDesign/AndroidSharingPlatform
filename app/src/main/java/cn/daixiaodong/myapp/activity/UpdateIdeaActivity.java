package cn.daixiaodong.myapp.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;
import cn.daixiaodong.myapp.fragment.UpdateCrowdfundingFragment;
import cn.daixiaodong.myapp.fragment.UpdateDefaultFragment;
import cn.daixiaodong.myapp.fragment.UpdateRecruitmentFragment;


/**
 * 用户更新自己发布的idea 界面
 */
@EActivity
public class UpdateIdeaActivity extends BaseActivity {

    @Extra
    String objectId;

    @Extra
    String title;

    @Extra
    int type;


    @ViewById(R.id.id_tb_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_idea_update);
        setSupportActionBar(mToolbar);
        Fragment fragment = null;
        switch (type) {
            case 0:
                fragment = UpdateDefaultFragment.newInstance(objectId, title);
                break;
            case 1:
                fragment = UpdateRecruitmentFragment.newInstance(objectId, title);
                break;
            case 2:
                fragment = UpdateCrowdfundingFragment.newInstance(objectId, title);
                break;
        }
        getFragmentManager().beginTransaction().add(R.id.flayout_container, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_idea_edit, menu);
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
