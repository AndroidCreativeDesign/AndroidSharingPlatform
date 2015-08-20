package life.z_turn.app.fragment;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import life.z_turn.app.R;
import life.z_turn.app.activity.EditIntroduceActivity_;
import life.z_turn.app.adapter.InfoAdapter;
import life.z_turn.app.config.Constants;
import life.z_turn.app.model.InfoBean;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.ToastUtil;
import life.z_turn.app.view.MyItemDecoration;


public class UpdateRecruitmentFragment extends Fragment implements InfoAdapter.OnItemClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<InfoBean> mData;

    private View mConvertView;

    private String mObjectStr;
    private AVObject mIdea;

    private String mTitle, mIntroduce;
    private int mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute;
    private int mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute;

    private boolean isStart;


    public static UpdateRecruitmentFragment newInstance(String param1, String param2) {
        UpdateRecruitmentFragment fragment = new UpdateRecruitmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mObjectStr = getArguments().getString(ARG_PARAM1);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mConvertView = inflater.inflate(R.layout.fragment_update_default, container, false);
        return mConvertView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mObjectStr != null) {
            try {
                mIdea = AVObject.parseAVObject(mObjectStr);
                setData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            initData();
        }
        setUpRecyclerView();
    }

    private void setData() {
        mTitle = mIdea.getString(Constants.COLUMN_TITLE);
        mIntroduce = mIdea.getString(Constants.COLUMN_INTRODUCE);
        Date startDate = mIdea.getDate(Constants.COLUMN_START_DATE);
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(startDate);
        mStartYear = calendar.get(Calendar.YEAR);
        mStartMonth = calendar.get(Calendar.MONTH);
        mStartDay = calendar.get(Calendar.DAY_OF_MONTH);
        mStartHour = calendar.get(Calendar.HOUR_OF_DAY);
        mStartMinute = calendar.get(Calendar.MINUTE);


        Date endDate = mIdea.getDate(Constants.COLUMN_END_DATE);
        Calendar endCalendar = Calendar.getInstance(Locale.CHINA);
        endCalendar.setTime(endDate);
        mEndYear = endCalendar.get(Calendar.YEAR);
        mEndMonth = endCalendar.get(Calendar.MONTH);
        mEndDay = endCalendar.get(Calendar.DAY_OF_MONTH);
        mEndHour = endCalendar.get(Calendar.HOUR_OF_DAY);
        mEndMinute = endCalendar.get(Calendar.MINUTE);

        mData = new ArrayList<>();
        mData.add(new InfoBean("标题", mTitle));
        mData.add(new InfoBean("介绍", mIntroduce));
        mData.add(new InfoBean("开始日期", getResources().getString(R.string.date, mStartYear, mStartMonth, mStartDay)));
        mData.add(new InfoBean("开始时间", getResources().getString(R.string.time, mStartHour, mStartMinute)));
        mData.add(new InfoBean("结束日期", getResources().getString(R.string.date, mEndYear, mEndMonth, mEndDay)));
        mData.add(new InfoBean("结束时间", getResources().getString(R.string.time, mEndHour, mEndMinute)));
        mData.add(new InfoBean("可参与总人数", null));
        mData.add(new InfoBean("已参与人数", null));
        mData.add(new InfoBean("活动费用", null));
        mData.add(new InfoBean("是否需要报名信息", null));
    }

    private void initData() {
        mData = new ArrayList<>();
        mData.add(new InfoBean("标题", null));
        mData.add(new InfoBean("介绍", null));
        mData.add(new InfoBean("开始日期", null));
        mData.add(new InfoBean("开始时间", null));
        mData.add(new InfoBean("结束日期", null));
        mData.add(new InfoBean("结束时间", null));
        mData.add(new InfoBean("可参与总人数", null));
        mData.add(new InfoBean("已参与人数", null));
        mData.add(new InfoBean("活动费用", null));
        mData.add(new InfoBean("是否需要报名信息", null));
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) mConvertView.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        InfoAdapter adapter = new InfoAdapter(getActivity(), mData);
        recyclerView.addItemDecoration(new MyItemDecoration(getActivity().getResources().getDrawable(R.drawable.item_shape)));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_update_default, menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_preview) {

            return true;
        }

        if (itemId == R.id.action_done) {

            saveData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveData() {
        Calendar startCalendar = Calendar.getInstance(Locale.CHINA);
        startCalendar.set(mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute);
        Calendar endCalendar = Calendar.getInstance(Locale.CHINA);
        endCalendar.set(mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute);

        Date startDate = new Date(startCalendar.getTimeInMillis());
        Date endDate = new Date(endCalendar.getTimeInMillis());

        AVObject object = new AVObject(Constants.TABLE_IDEA);
        object.put(Constants.COLUMN_USER, AVUser.getCurrentUser());
        object.put(Constants.COLUMN_TITLE, mTitle);
        object.put(Constants.COLUMN_INTRODUCE, mIntroduce);
        object.put(Constants.COLUMN_START_DATE, new Date());
        object.put(Constants.COLUMN_END_DATE, new Date());
        object.put(Constants.COLUMN_TYPE, Constants.TYPE_RECRUITMENT);
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    ToastUtil.showToast(getActivity(), "发布成功，等待审核");
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(getActivity(), e);
                }
            }
        });

    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder, int position) {
        InfoAdapter.InfoViewHolder holder = (InfoAdapter.InfoViewHolder) viewHolder;

        switch (position) {
            case 0:
                showEditTitleDialog(holder, position);
                break;
            case 1:
                EditIntroduceActivity_.intent(getActivity()).startForResult(Constants.REQUEST_EDIT_INTRODUCE);
                break;
            case 2:
                isStart = true;
                showDatePicker(holder, position);
                break;
            case 3:
                isStart = true;
                showTimePicker(holder, position);
                break;
            case 4:
                isStart = false;
                showDatePicker(holder, position);
                break;
            case 5:
                isStart = false;
                showTimePicker(holder, position);
                break;
            case 6:
                showJoinNumDialog(holder, position);
                break;

        }
    }

    private void showJoinNumDialog(final InfoAdapter.InfoViewHolder holder, final int position) {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_real_name, null);

        final AlertDialog d = new AlertDialog.Builder(getActivity())
                .setTitle("标题")
                .setView(view)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", null)
                .create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btnConfirm = d.getButton(AlertDialog.BUTTON_POSITIVE);
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editRealName = (EditText) view.findViewById(R.id.dialog_edit_content);
                        String realName = editRealName.getText().toString();
                        if (realName.trim().isEmpty()) {
                            /*((InfoAdapter.DepartmentViewHolder)viewHolder).value.setText("未填写");
                            mData.get(position).setValue(null);*/
                        } else {
                            holder.value.setText(realName);
                            mData.get(position).setValue(realName);
                            d.dismiss();
                        }
                    }
                });
            }
        });
        d.show();
    }

    private void showTimePicker(final InfoAdapter.InfoViewHolder holder, final int position) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                holder.value.setText(hourOfDay + ":" + minute);
                mData.get(position).setValue(hourOfDay + ":" + minute);
                if (isStart) {
                    mStartHour = hourOfDay;
                    mStartMinute = minute;
                } else {
                    mEndHour = hourOfDay;
                    mEndMinute = minute;
                }
            }
        }, 14, 22, true);
        timePickerDialog.show();
    }

    private void showDatePicker(final InfoAdapter.InfoViewHolder viewHolder, final int position) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = getResources().getString(R.string.date, year, monthOfYear, dayOfMonth);
                viewHolder.value.setText(date);
                mData.get(position).setValue(date);
                if (isStart) {
                    mStartYear = year;
                    mStartMonth = monthOfYear;
                    mStartDay = dayOfMonth;
                } else {
                    mEndYear = year;
                    mEndMonth = monthOfYear;
                    mEndDay = dayOfMonth;
                }
            }
        }, 2015, 1, 1);
        datePickerDialog.show();
    }

    private void showEditTitleDialog(final InfoAdapter.InfoViewHolder viewHolder, final int position) {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_real_name, null);

        final AlertDialog d = new AlertDialog.Builder(getActivity())
                .setTitle("标题")
                .setView(view)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", null)
                .create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btnConfirm = d.getButton(AlertDialog.BUTTON_POSITIVE);
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editRealName = (EditText) view.findViewById(R.id.dialog_edit_content);
                        mTitle = editRealName.getText().toString();

                        if (mTitle.trim().isEmpty()) {
                            /*((InfoAdapter.DepartmentViewHolder)viewHolder).value.setText("未填写");
                            mData.get(position).setValue(null);*/
                        } else {
                            viewHolder.value.setText(mTitle);
                            mData.get(position).setValue(mTitle);
                            d.dismiss();
                        }
                    }
                });
            }
        });
        d.show();
    }


}
