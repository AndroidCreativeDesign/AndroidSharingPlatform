package cn.daixiaodong.myapp.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;
import cn.daixiaodong.myapp.fragment.UpdateCrowdfundingFragment;
import cn.daixiaodong.myapp.fragment.UpdateDefaultFragment;
import cn.daixiaodong.myapp.fragment.UpdateRecruitmentFragment;


@EActivity
public class UpdateActivity extends BaseActivity {


    private List<Fragment> mFragments;


    @ViewById(R.id.group_type)
    RadioGroup mTypeGroup;

    @ViewById(R.id.id_tb_toolbar)
    Toolbar mToolbar;

    @AfterViews
    void init() {
        setSupportActionBar(mToolbar);

        mTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_type_default:
                        setCurrentFragment(0,"default");
                        break;
                    case R.id.rbtn_type_recruitment:
                        showToast("rbtn_type_recruitment");
                        setCurrentFragment(1, "recruitment");
                        break;
                    case R.id.rbtn_type_crowdfunding:
                        showToast("rbtn_type_crowdfunding");
                        setCurrentFragment(2, "crowdfunding");
                        break;
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recruitment);

        if (savedInstanceState == null) {
            mFragments = new ArrayList<>();
        }
        setCurrentFragment(0, "default");
    }

    public void setCurrentFragment(int id, String tag) {
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            for (Fragment fragment1 : mFragments) {
                if (fragment1 != fragment)
                    getFragmentManager().beginTransaction().hide(fragment1).commit();
            }
            getFragmentManager().beginTransaction().show(fragment).commit();
            return;
        }
        switch (id) {
            case 0:
                fragment = new UpdateDefaultFragment();
                break;
            case 1:
                fragment = new UpdateRecruitmentFragment();
                break;
            case 2:
                fragment = new UpdateCrowdfundingFragment();

        }
        for (Fragment fragment1 : mFragments) {
            getFragmentManager().beginTransaction().hide(fragment1).commit();
        }
        mFragments.add(fragment);
        getFragmentManager().beginTransaction().add(R.id.flayout_container, fragment, tag).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_recruitment, menu);
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
