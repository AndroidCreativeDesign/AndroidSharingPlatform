package cn.daixiaodong.myapp.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.feedback.FeedbackAgent;
import com.bmob.pay.tool.BmobPay;
import com.squareup.picasso.Picasso;
import com.umeng.update.UmengUpdateAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;
import cn.daixiaodong.myapp.config.Constants;
import cn.daixiaodong.myapp.fragment.HomeFragment;
import cn.daixiaodong.myapp.fragment.PushMessageFragment;
import cn.daixiaodong.myapp.fragment.UserCollectFragment;
import cn.daixiaodong.myapp.fragment.UserFollowFragment;
import cn.daixiaodong.myapp.receiver.PushBroadcastReceiver;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 应用主界面
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private int mCurrentPosition = R.id.action_home;

    public static final String BROAD_RECEIVER_ACTION = "broad_receiver";

    @ViewById(R.id.id_tb_toolbar)
    Toolbar mViewToolbar;

    @ViewById(R.id.id_layout_drawer_layout)
    DrawerLayout mDrawerLayout;


    @ViewById(R.id.id_nv_navigation)
    NavigationView mViewNav;

    private ArrayList<Fragment> mFragments;

    private CircleImageView mProfilePhoto;

    private TextView mUsername;

    private UserStatusChangeBroadcastReceiver mReceiver;

    private AVUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            initData();
        } else {

            mFragments = (ArrayList<Fragment>) getSupportFragmentManager().getFragments();

            // activity 实例被销毁，属性 为 NULL
            Log.i("onCreate", "savedInstanceState != null");
            Log.i("savedInstanceState", savedInstanceState.toString());
            if (mFragments == null && mViewToolbar == null) {
                Log.i("mFragments", "mFragments == null");
                Log.i("mViewToolbar", " mViewToolbar == null");
            }

        }

        // 接受消息推送 广播 数据 处理相应逻辑
        if (BROAD_RECEIVER_ACTION.equals(getIntent().getAction())) {
            Bundle bundle = getIntent().getBundleExtra(PushBroadcastReceiver.BROAD_DATA_KEY);
            String title = bundle.getString("title");
            Log.i(BROAD_RECEIVER_ACTION, title);
        }

        // 第三方SDK初始化
        AVInstallation.getCurrentInstallation().saveInBackground();
        PushService.setDefaultPushCallback(this, MainActivity_.class);
        FeedbackAgent agent = new FeedbackAgent(this);
        agent.sync();
        UmengUpdateAgent.update(this);
        BmobPay.init(this, Constants.BMOB_APPLICATION_ID);


        // 注册用户登录状态改变的广播
        mReceiver = new UserStatusChangeBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_USER_SIGN_IN);
        filter.addAction(Constants.ACTION_USER_SIGN_OUT);
        registerReceiver(mReceiver, filter);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("tag", "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        //

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @AfterViews
    void init() {

        initToolbar();
        initNav();
        setCurrentFragment("首页", R.id.action_home);
    }

    private void initData() {
        mFragments = new ArrayList<>();
        mUser = AVUser.getCurrentUser();
    }

    private void initNav() {


        mViewNav.findViewById(R.id.llayout_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AVUser.getCurrentUser() != null) {

                    UserProfileActivity_.intent(MainActivity.this).start();
                } else {
                    SignInActivity_.intent(MainActivity.this).start();
                }
            }
        });

        mProfilePhoto = (CircleImageView) mViewNav.findViewById(R.id.id_iv_profile_photo);

        mUsername = (TextView) mViewNav.findViewById(R.id.tv_username);


        if (mUser != null) {
            Picasso.with(this).load(mUser.getString("profilePhotoUrl")).into(mProfilePhoto);
            mUsername.setText(mUser.getUsername());
        }

        mViewNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                mDrawerLayout.closeDrawer(GravityCompat.START);

                if (menuItem.getItemId() == R.id.action_feedback) {
                    FeedbackAgent agent = new FeedbackAgent(MainActivity.this);
                    agent.startDefaultThreadActivity();
                    return true;
                }
                if (menuItem.getItemId() == R.id.action_settings) {
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    return true;
                }

                if (mCurrentPosition != menuItem.getItemId()) {

                    setCurrentFragment(menuItem.getTitle().toString(), menuItem.getItemId());
                    setCurrentTitle(menuItem.getTitle().toString());
                    mCurrentPosition = menuItem.getItemId();
                    menuItem.setChecked(true);
                }

                return true;
            }
        });
    }

    private void setCurrentTitle(String s) {
        mViewToolbar.setTitle(s);
    }


    /**
     * 设置Toolbar，设置标题，设置Drawer导航
     */
    private void initToolbar() {
        setSupportActionBar(mViewToolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mViewToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mViewToolbar.setTitleTextColor(Color.WHITE);
        mViewToolbar.setTitle("首页");
    }


    private void setCurrentFragment(String title, int itemId) {

//        Log.i("tag", "setCurrentFragment");
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment mCurrentFragment = fragmentManager.findFragmentByTag(title);
        // 将集合中的fragment隐藏
        for (Fragment fragment : mFragments) {

            fragmentManager.beginTransaction().hide(fragment).commit();

        }
        if (mCurrentFragment == null) {
            switch (itemId) {
                // 首页
                case R.id.action_home:
                    Log.i("tag", "case 0");
                    mCurrentFragment = new HomeFragment();
                    break;
                // 收藏
                case R.id.action_collect:
                    Log.i("tag", "case 1");
                    mCurrentFragment = new UserCollectFragment();
                    break;
                // 消息
                case R.id.action_message:
                    Log.i("tag", "case 2");
                    mCurrentFragment = new PushMessageFragment();
                    break;
                // 关注
                case R.id.action_follow:
                    Log.i("tag", "action follow");
                    mCurrentFragment = new UserFollowFragment();

                    break;
            }

            // 将mCurrentFragment对象引用压入fragment管理栈
            fragmentManager.beginTransaction().add(R.id.id_layout_content_container, mCurrentFragment, title).commit();
            // 存入集合中
            mFragments.add(mCurrentFragment);
        } else {

            // 显示找到的fragment视图
            fragmentManager.beginTransaction().show(mCurrentFragment).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            case R.id.action_sign_out:

                AVUser.logOut();
                /*这样写  收不到广播
                Intent intent = new Intent(MainActivity.this, UserStatusChangeBroadcastReceiver.class);
                intent.setAction(Constants.ACTION_USER_SIGN_OUT);*/
                Intent intent = new Intent(Constants.ACTION_USER_SIGN_OUT);
                sendBroadcast(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    class UserStatusChangeBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constants.ACTION_USER_SIGN_IN.equals(action)) {
                updateUIWhenUserSignIn();
            } else if (Constants.ACTION_USER_SIGN_OUT.equals(action)) {
                updateUIWhenUserSignOut();

            }
        }
    }

    private void updateUIWhenUserSignOut() {
        mProfilePhoto.setImageResource(R.drawable.ic_add_white_48dp);
        mUsername.setText("未登录");
    }

    private void updateUIWhenUserSignIn() {
        Picasso.with(MainActivity.this).load(AVUser.getCurrentUser().getString("profile_photo_url")).into(mProfilePhoto);
        mUsername.setText(AVUser.getCurrentUser().getUsername());
    }


}
