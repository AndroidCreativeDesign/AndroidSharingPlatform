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

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;


/**
 *  发布一个idea界面
 */
@EActivity(R.layout.activity_publish_idea)
public class PublishIdeaActivity extends BaseActivity {

    @ViewById(R.id.id_tb_toolbar)
    Toolbar mViewToolbar;


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

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish_idea, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_done:

                final AVObject idea = new AVObject("idea");
                idea.put("title", "标题");   // 标题
                idea.put("joinNum", 1);   // 参与人数
                idea.put("introduce", "介绍");  // 介绍
                idea.put("imgUrl", "http://img.1985t.com/uploads/attaches/2015/03/31071-m9t3Ri.jpg");  // 图片
                idea.put("startDate", new Date());  // 开始时间
                idea.put("endDate", new Date());  // 结束时间
                idea.put("address", "井大");  // 地点
                idea.put("stage", 0);  // 阶段
                idea.put("type", 0);   // 类型
                idea.put("tag", 0);  // 奖项 例如： 0:无  1:专题 2:最热门  3:最佳创意
                idea.put("tagName","社团专题");
                idea.put("user", AVUser.getCurrentUser());
                idea.put("totalNum", 11);  // 总人数
                idea.put("collectNum", 11);  // 收藏数
                idea.put("totalFee", 111.9); // 总费用
                idea.put("isRecruit", 0);  // 是否招募
                idea.put("recruit", 0); // 招募类型
                idea.put("user", AVUser.getCurrentUser());  // 用户

                idea.saveInBackground(new SaveCallback() {
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



    private void initToolbar() {
        setSupportActionBar(mViewToolbar);
        mViewToolbar.setTitle("创建");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }
}
