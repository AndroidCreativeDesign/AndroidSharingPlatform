package cn.daixiaodong.myapp.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.avos.avoscloud.AVObject;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;
import cn.daixiaodong.myapp.adapter.ViewPagerAdapter;
import cn.daixiaodong.myapp.fragment.AssociationIntroduceFragment;
import cn.daixiaodong.myapp.fragment.AssociationPhotosFragment;


/**
 * 协会详情界面
 */
@EActivity
public class AssociationDetailActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tl_tab_layout)
    TabLayout mTabLayout;

    @ViewById(R.id.vp_view_pager)
    ViewPager mViewPager;


    @Extra
    String mAssociationId;

    @Extra
    String mAssociationName;

    private AVObject mAssociation;

    @AfterExtras
    void initData() {

    }

    @AfterViews
    void init() {
        setUpToolbar();
        setUpViewPager();
        mTabLayout.setupWithViewPager(mViewPager);
        mToolbar.setTitle(mAssociationName);

    }

    private void setUpViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(2);
        Log.i("id=---->",mAssociationId);
        AssociationIntroduceFragment introduceFragment = AssociationIntroduceFragment.newInstance(mAssociationId, null);
        AssociationPhotosFragment photosFragment = AssociationPhotosFragment.newInstance(mAssociationId, null);
        adapter.addFragment(introduceFragment, "介绍");
        adapter.addFragment(photosFragment, "精彩活动");
        mViewPager.setAdapter(adapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_association);

    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //mToolbar.setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_association, menu);
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
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
