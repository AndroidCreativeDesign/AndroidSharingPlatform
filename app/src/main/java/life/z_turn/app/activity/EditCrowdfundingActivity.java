package life.z_turn.app.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import life.z_turn.app.R;
import life.z_turn.app.config.Constants;
import life.z_turn.app.fragment.UpdateCrowdfundingFragment;


@EActivity
public class EditCrowdfundingActivity extends AppCompatActivity {

    @Extra(Constants.EXTRA_KEY_OBJECT_STRING)
    String mObjectStr;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_default);

        setUpToolbar();

        Fragment fragment = UpdateCrowdfundingFragment.newInstance(mObjectStr,null);
        getFragmentManager().beginTransaction().add(R.id.flayout_container, fragment).commit();
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("梦想众筹");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
