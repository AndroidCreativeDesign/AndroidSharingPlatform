package life.z_turn.app.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import life.z_turn.app.R;
import life.z_turn.app.config.Constants;
import life.z_turn.app.utils.ExceptionUtil;


@EActivity(R.layout.activity_notification_detail)
public class NotificationDetailActivity extends AppCompatActivity {


    private AVObject mObject;

    @Extra(Constants.EXTRA_KEY_OBJECT_STRING)
    String mObjectStr;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.text_content)
    TextView mTextContent;


    @AfterViews
    void initViews() {
        setUpToolbar();
        initData();
    }

    private void initData() {
        try {
            mObject = AVObject.parseAVObject(mObjectStr);
            mTextContent.setText(mObject.getString("content"));
        } catch (Exception e) {
            ExceptionUtil.printStackTrace(e);
        }
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("消息详情");
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
        switch (id){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
