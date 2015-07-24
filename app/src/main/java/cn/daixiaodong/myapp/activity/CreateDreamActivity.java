package cn.daixiaodong.myapp.activity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.HashMap;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;


@EActivity(R.layout.activity_create_dream)
public class CreateDreamActivity extends BaseActivity {

    @ViewById(R.id.id_tb_toolbar)
    Toolbar mViewToolbar;


    private AVObject dream;

    @ViewById(R.id.id_et_title)
    EditText mViewTitle;

    @ViewById(R.id.id_rg_radio_group)
    RadioGroup mViewRadioGroup;

    private int mType = 0;

    @AfterViews
    void init() {
        initToolbar();
        mViewRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.id_rb_zhaomu:
                        mType = 0;
                        break;
                }
            }
        });
        dream = new AVObject("dream");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_dream_first_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_next:

                final AVObject dream = new AVObject("dream");
                dream.put("title", "标题");   // 标题
                dream.put("joinNum", 1);   // 参与人数
                dream.put("introduce", "介绍");  // 介绍
                dream.put("imgUrl", "http://img.1985t.com/uploads/attaches/2015/03/31071-m9t3Ri.jpg");  // 图片
                dream.put("startDate", new Date());  // 开始时间
                dream.put("endDate", new Date());  // 结束时间
                dream.put("address", "井大");  // 地点
                dream.put("stage", 0);  // 阶段
                dream.put("type", 0);   // 类型
                dream.put("prize", 1);  // 奖项 例如： 最佳创意  最热门
                dream.put("user", AVUser.getCurrentUser());
                dream.put("totalNum", 11);  // 总人数
                dream.put("collectNum", 11);  // 收藏数
                dream.put("totalFee", 111.9); // 总费用
                dream.put("isRecruit", 0);  // 是否招募
                dream.put("recruit", 0); // 招募类型
                dream.put("user", AVUser.getCurrentUser());  // 用户
                dream.put("topic", 0);
                dream.put("topicName", "");
                dream.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            showToast("保存成功");
                        }
                    }
                });

                AVObject association = new AVObject("association");
                association.put("name", "演讲与口才协会");
                association.saveInBackground();


                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDream() {
        String title = mViewTitle.getText().toString();
        dream.put("title", title);
        dream.put("type", mType);
        HashMap<String, Object> map = new HashMap<>();
        map.put("title", title);

        CreateDreamSecondActivity_.intent(this).dream(map).start();
    }

    private void initToolbar() {
        setSupportActionBar(mViewToolbar);
        mViewToolbar.setTitle("创建");
        mViewToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_48dp);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }
}
