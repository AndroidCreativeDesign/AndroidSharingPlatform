package life.z_turn.app.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import life.z_turn.app.R;
import life.z_turn.app.adapter.ViewPagerAdapter;
import life.z_turn.app.config.Constants;
import life.z_turn.app.fragment.DepartmentIntroduceFragment;
import life.z_turn.app.fragment.DepartmentPhotosFragment;


@EActivity
public class DepartmentDetailActivity extends AppCompatActivity {

    public final static String EXTRA_KEY_DEPARTMENT_ID = "departmentId";


    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tab_layout)
    TabLayout mTabLayout;

    @ViewById(R.id.view_pager)
    ViewPager mViewPager;

    @Extra(Constants.EXTRA_KEY_DEPARTMENT_STRING_OBJECT)
    String mDepartmentStringObject;

    @Extra(EXTRA_KEY_DEPARTMENT_ID)
    String mDepartmentId;

    @Extra(Constants.EXTRA_KEY_DEPARTMENT_NAME)
    String mDepartmentName;


    @AfterViews
    void init() {
        setUpToolbar();
        setUpViewPager();
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private void setUpViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(2);
        DepartmentIntroduceFragment introduceFragment = DepartmentIntroduceFragment.newInstance(mDepartmentId, mDepartmentStringObject);
        DepartmentPhotosFragment photosFragment = DepartmentPhotosFragment.newInstance(mDepartmentId, null);
        adapter.addFragment(introduceFragment, "介绍");
        adapter.addFragment(photosFragment, "记忆");
        mViewPager.setAdapter(adapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_detail);

    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(mDepartmentName);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_department_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
