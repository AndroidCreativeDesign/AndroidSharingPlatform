package cn.daixiaodong.myapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVUser;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.PublishIdeaActivity_;
import cn.daixiaodong.myapp.activity.SignInActivity;
import cn.daixiaodong.myapp.adapter.ViewPagerAdapter;
import cn.daixiaodong.myapp.fragment.common.BaseFragment;


/**
 * 首页Fragment  嵌套3个子Fragment
 */
public class HomeFragment extends BaseFragment {

    TabLayout tabLayout;

    ViewPager mViewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    void init() {
        tabLayout = (TabLayout) getActivity().findViewById(R.id.id_tl_tab_layout);
        mViewPager = (ViewPager) getActivity().findViewById(R.id.id_vp_view_pager);
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);
        /*tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showToast(getActivity(),tab.getPosition()+"");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            /*case R.id.action_create:
                AVAnalytics.onEvent(getActivity(), "create_dream");
                if (!checkUserStatus()) {
                    SignInActivity_.intent(getActivity()).extra("log_in_toward", 0).start();
                } else {
                    PublishIdeaActivity_.intent(getActivity()).start();
                }
                break;*/
        /*    case R.id.action_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;*/
        }
        return true;
    }

    private Boolean checkUserStatus() {
        AVUser user = AVUser.getCurrentUser();
        return user != null;
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setOffscreenPageLimit(3);
        adapter.addFragment(new IdeaListFragment(), "首页");
        adapter.addFragment(new UserJoinListFragment(), "我参加的");
        adapter.addFragment(new UserPublishListFragment(), "我发起的");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("parentFragment",""+resultCode+"");
        if(requestCode == SignInActivity.SIGN_IN_REQUEST_CODE){
            if(resultCode == SignInActivity.SIGN_IN_SUCCESS_RESULT_CODE){
                PublishIdeaActivity_.intent(getActivity()).start();
            }
        }
    }
}
