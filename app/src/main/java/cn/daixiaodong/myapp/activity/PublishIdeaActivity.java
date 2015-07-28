package cn.daixiaodong.myapp.activity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;
import java.util.Date;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;


/**
 * 发布一个idea界面
 */
@EActivity(R.layout.activity_publish_idea)
public class PublishIdeaActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    @ViewById(R.id.id_tb_toolbar)
    Toolbar mViewToolbar;


    @ViewById(R.id.et_title)
    EditText mViewTitle;

    @ViewById(R.id.group_type)
    RadioGroup mViewRadioGroup;


    @ViewById(R.id.tv_start_date)
    TextView mStartDateTxt;
    @ViewById(R.id.tv_start_time)
    TextView mStartTimeTxt;

    @ViewById(R.id.tv_end_date)
    TextView mEndDateTxt;


    @ViewById(R.id.tv_end_time)
    TextView mEndTimeTxt;

    @ViewById(R.id.et_join_num)
    EditText mJoinNumEdit;

    @ViewById(R.id.et_total_num)
    EditText mJoinTotalNumEdit;

    @ViewById(R.id.group_stage)
    RadioGroup mStageGroup;

    @ViewById(R.id.chk_recruit)
    CheckBox mRecruitChk;

    @ViewById(R.id.chk_crowdfunding)
    CheckBox mCrowdfundingChk;

    @ViewById(R.id.et_introduce)
    EditText mIntroduceEdit;

    @ViewById(R.id.et_crowdfunding_total_amount)
    EditText mCrowdingfundingEdit;


    private int mType = 0;
    private int isRecruit = 0;


    private Date mStartDate;
    private Date mEndDate;


    Calendar mStarCalendar;
    Calendar mEndCalendar;

    private DatePickerDialog mStartDateDialog;

    private DatePickerDialog mEndDateDialog;

    private boolean isStartDateSet;
    private boolean isStartTimeSet;
    private boolean isEndDateSet;
    private boolean isEndTimeSet;

    @Click({R.id.tv_start_date, R.id.tv_start_time, R.id.tv_end_date, R.id.tv_end_time})
    void click(View view) {
        Calendar now = Calendar.getInstance();
        switch (view.getId()) {
            case R.id.tv_start_date:


                mStartDateDialog = DatePickerDialog.newInstance(
                        this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                mStartDateDialog.show(getFragmentManager(), "Datepickerdialog");

                break;
            case R.id.tv_start_time:

                TimePickerDialog timePickerDialog2 = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
                                String time = hourOfDay + ":" + minute;
                                isStartTimeSet = true;
                                mStarCalendar.set(Calendar.HOUR, hourOfDay);
                                mStarCalendar.set(Calendar.MINUTE, minute);
                                mStartTimeTxt.setText(time);
                            }
                        },
                        now.get(Calendar.HOUR),
                        now.get(Calendar.MINUTE),
                        true
                );
                timePickerDialog2.show(getFragmentManager(), "TimePickerDialog");
                break;
            case R.id.tv_end_date:

                mEndDateDialog = DatePickerDialog.newInstance(
                        this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                mEndDateDialog.show(getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.tv_end_time:

                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
                                String time = hourOfDay + ":" + minute;
                                isEndTimeSet = true;
                                mEndCalendar.set(Calendar.HOUR, hourOfDay);
                                mEndCalendar.set(Calendar.MINUTE, minute);
                                mEndTimeTxt.setText(time);
                            }
                        },
                        now.get(Calendar.HOUR),
                        now.get(Calendar.MINUTE),
                        true
                );
                timePickerDialog.show(getFragmentManager(), "TimePickerDialog");
                break;

        }
    }


    @AfterViews
    void init() {
        initToolbar();
        mStarCalendar = Calendar.getInstance();
        mEndCalendar = Calendar.getInstance();
        mViewRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_type_one:
                        mType = 0;
                        break;
                }
            }
        });
        mStageGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_stage_one:
                        break;
                }
            }
        });
        mRecruitChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //
                    isRecruit = 1;
                }
            }
        });
        mCrowdfundingChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isRecruit = 2;
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

                if (!isStartDateSet || !isStartTimeSet) {
                    showToast("请设置开始日期以及时间");
                    break;
                }
                if (!isEndDateSet || !isEndTimeSet) {
                    showToast("请设置结束日期以及时间");
                    break;
                }
                if(mEndCalendar.getTimeInMillis() < mStarCalendar.getTimeInMillis()){
                    showToast("请设置正确的起止时间");
                    break;
                }


                int joinNum = Integer.parseInt(mJoinNumEdit.getText().toString());
                int joinTotalNum = Integer.parseInt(mJoinTotalNumEdit.getText().toString());
                int crowdfunding = Integer.parseInt(mCrowdingfundingEdit.getText().toString());

                String title = mViewTitle.getText().toString();
                String introduce = mIntroduceEdit.getText().toString();


                final AVObject idea = new AVObject("idea");
                idea.put("title", title);   // 标题
                idea.put("joinNum", joinNum);   // 参与人数
                idea.put("introduce", introduce);  // 介绍
                idea.put("imgUrl", "http://img.1985t.com/uploads/attaches/2015/03/31071-m9t3Ri.jpg");  // 图片
                idea.put("startDate", mStarCalendar.getTime());  // 开始时间
                idea.put("endDate", mEndCalendar.getTime());  // 结束时间
                idea.put("address", "井大");  // 地点
                idea.put("stage", 0);  // 阶段
                idea.put("type", mType);   // 类型
                idea.put("tag", 0);  // 奖项 例如： 0:无  1:专题 2:最热门  3:最佳创意
                idea.put("tagName", "社团专题");

                idea.put("totalNum", joinTotalNum);  // 总人数
                idea.put("collectNum", 11);  // 收藏数
                idea.put("totalFee", 111.9); // 总费用
               /* idea.put("isRecruit", 0);  // 是否招募
                idea.put("recruit", 0); // 招募类型*/
                idea.put("crowdfundingTotalAmount",crowdfunding);
//                idea.put("id",1);
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        if (view == mStartDateDialog) {
            isStartDateSet = true;
            mStarCalendar.set(year, monthOfYear, dayOfMonth);
            mStartDateTxt.setText(date);
        }
        if (view == mEndDateDialog) {
            isEndDateSet = true;
            mEndCalendar.set(year, monthOfYear, dayOfMonth);
            mEndDateTxt.setText(date);
        }


    }
}
