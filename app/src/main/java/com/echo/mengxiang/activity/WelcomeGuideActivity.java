package com.echo.mengxiang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.MainActivity_;
import cn.daixiaodong.myapp.activity.common.BaseActivity;

public class WelcomeGuideActivity extends BaseActivity {

    private Button welcomeGuideBtn;
    private ViewPager welcomeGuidePager;
    private List<View> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_guide);
        welcomeGuideBtn = (Button) findViewById(R.id.btn_welcome_guide);
        welcomeGuidePager = (ViewPager) findViewById(R.id.viewpager_welcome_guide);
        initViewPager();
        initClick();
    }
    public void initClick(){
        welcomeGuideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实现页面跳转
                startActivity(new Intent(getApplicationContext(), MainActivity_.class));
                finish();
            }
        });
    }
    //初始化ViewPager的方法
    public void initViewPager(){
        list = new ArrayList<View>();
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.bg_guide_image_one);
        list.add(iv);
        ImageView iv1 = new ImageView(this);
        iv1.setImageResource(R.drawable.bg_guide_image_two);
        list.add(iv1);
        ImageView iv2 = new ImageView(this);
        iv2.setImageResource(R.drawable.bg_guide_image_three);
        list.add(iv2);
        welcomeGuidePager.setAdapter(new MyPagerAdapter());
        //监听ViewPager滑动效果
        welcomeGuidePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //页卡被选中的方法
            @Override
            public void onPageSelected(int arg0) {
                //如果是第三个页面
                if (arg0 == 2) {
                    welcomeGuideBtn.setVisibility(View.VISIBLE);
                } else {
                    welcomeGuideBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }
    //定义ViewPager的适配器
    class MyPagerAdapter extends PagerAdapter{
        //计算需要多少item显示
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==arg1;
        }
        //初始化item实例方法
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }
        //item销毁的方法
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }
    }
}
