package cn.daixiaodong.myapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

import cn.daixiaodong.myapp.R;

public class WelcomeGuideActivity extends Activity {
    @ViewInject(R.id.welcome_guide_btn)
    private Button btn;
    @ViewInject(R.id.welcome_pager)
    private ViewPager pager; //绑定ViewPager
    private List<View> list; //填充ImageView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_guide);
        ViewUtils.inject(this);//填充This
        initViewPager();
    }

    @OnClick(R.id.welcome_guide_btn)
    public void click(View view){
        //页面的跳转
        startActivity(new Intent(getBaseContext(), MainActivity_.class));
        finish();//结束当前页面
    }

    //初始化ViewPager
    public void initViewPager(){
        list = new ArrayList<View>();
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.guide_01);
        list.add(iv);
        ImageView iv1 = new ImageView(this);
        iv1.setImageResource(R.drawable.guide_02);
        list.add(iv1);
        ImageView iv2 = new ImageView(this);
        iv2.setImageResource(R.drawable.guide_03);
        list.add(iv2);
        pager.setAdapter(new MyPagerAdapter());
        //监听ViewPager滑动效果
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //页卡被选中的方法
            @Override
            public void onPageSelected(int arg0) {
                //如果是第三个页面
                if (arg0 == 2) {
                    btn.setVisibility(View.VISIBLE);
                } else {
                    btn.setVisibility(View.GONE);
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

    //定义Viewpager的适配器
    class MyPagerAdapter extends PagerAdapter{
        //计算需要多少item显示
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==arg1;//如果是同一个对象，则返回为true
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
			//super.destroyItem(container, position, object);
            container.removeView(list.get(position));
        }
    }
}
