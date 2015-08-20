package life.z_turn.app.activity;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import life.z_turn.app.R;
import life.z_turn.app.activity.common.BaseActivity;
import life.z_turn.app.adapter.ToolbarSpinnerAdapter;
import life.z_turn.app.fragment.UpdateCrowdfundingFragment;
import life.z_turn.app.fragment.UpdateDefaultFragment;
import life.z_turn.app.fragment.UpdateRecruitmentFragment;
import life.z_turn.app.utils.DialogUtil;


@EActivity
public class UpdateActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {


    private List<Fragment> mFragments;
    private String mIntroduceContent;

    @ViewById(R.id.group_type)
    RadioGroup mTypeGroup;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    private Spinner mSpinnerType;

    @AfterViews
    void init() {
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mSpinnerType = (Spinner) mToolbar.findViewById(R.id.spinner_type);
        setUpSpinner();


    }

    private void setUpSpinner() {
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.type));
        ToolbarSpinnerAdapter adapter = new ToolbarSpinnerAdapter(this, Arrays.asList(getResources().getStringArray(R.array.type)));
        mSpinnerType.setAdapter(adapter);
        mSpinnerType.setOnItemSelectedListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recruitment);
        if (savedInstanceState == null) {
            mFragments = new ArrayList<>();
        }
        setCurrentFragment(0, "0");
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
        switch (id){
            case android.R.id.home:
                DialogUtil.ShowAlertDialog(this, R.string.exit_edit_tip, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                finish();
                break;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                setCurrentFragment(0, "0");
                break;
            case 1:
                setCurrentFragment(1, "1");
                break;
            case 2:
                setCurrentFragment(2, "2");
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
