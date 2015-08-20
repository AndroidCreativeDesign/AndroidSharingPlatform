package life.z_turn.app.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import life.z_turn.app.R;
import life.z_turn.app.activity.common.BaseActivity;
import life.z_turn.app.config.Constants;
import life.z_turn.app.fragment.UpdateCrowdfundingFragment;
import life.z_turn.app.fragment.UpdateDefaultFragment;
import life.z_turn.app.fragment.UpdateRecruitmentFragment;


/**
 * 用户更新自己发布的idea 界面
 */
@EActivity
public class UpdateIdeaActivity extends BaseActivity {


    @Extra(Constants.EXTRA_KEY_OBJECT_STRING)
    String mObjectStr;


    @Extra(Constants.EXTRA_KEY_TYPE)
    int mType;


    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_update);
        setUpToolbar();
        Fragment fragment = null;
        switch (mType) {
            case 0:
                fragment = UpdateDefaultFragment.newInstance(mObjectStr, null);
                break;
            case 1:
                fragment = UpdateRecruitmentFragment.newInstance(mObjectStr, null);
                break;
            case 2:
                fragment = UpdateCrowdfundingFragment.newInstance(mObjectStr, null);
                break;
        }
        getFragmentManager().beginTransaction().add(R.id.flayout_container, fragment).commit();
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
