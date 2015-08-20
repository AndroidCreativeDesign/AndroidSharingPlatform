package life.z_turn.app.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.androidannotations.annotations.EActivity;

import life.z_turn.app.R;
import life.z_turn.app.activity.common.BaseActivity;
import life.z_turn.app.utils.LogUtil;


/**
 * idea 介绍 编辑 界面
 */
@EActivity
public class EditIdeaDetailActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_idea_detail);
        findViewById(R.id.ripple_creative).setOnClickListener(this);
        findViewById(R.id.ripple_crowdfunding).setOnClickListener(this);
        findViewById(R.id.ripple_recruitment).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_idea_detail, menu);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ripple_creative:
                LogUtil.i("xxxxx","xxxxx");
                EditDefaultActivity_.intent(this).start();
                break;
            case R.id.ripple_recruitment:
                EditRecruitmentActivity_.intent(this).start();
                break;
            case R.id.ripple_crowdfunding:
                EditCrowdfundingActivity_.intent(this).start();
                break;
            case R.id.ripple_not_have:
                break;
        }
    }
}
