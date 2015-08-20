package life.z_turn.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import life.z_turn.app.R;
import life.z_turn.app.activity.PublishIdeaActivity_;
import life.z_turn.app.activity.SignInActivity;
import life.z_turn.app.adapter.ViewPagerAdapter;
import life.z_turn.app.fragment.common.BaseFragment;


/**
 * 首页Fragment  嵌套4个子Fragment
 */
public class HomeFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

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
        initViews();
    }


    void initViews() {
        tabLayout = (TabLayout) getActivity().findViewById(R.id.id_tl_tab_layout);
        mViewPager = (ViewPager) getActivity().findViewById(R.id.id_vp_view_pager);
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);
    }



    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setOffscreenPageLimit(4);
        adapter.addFragment(new IdeaListFragment(), getResources().getString(R.string.tab_name_home));
        adapter.addFragment(new DiscoveryFragment(), getResources().getString(R.string.tab_name_discover));
        adapter.addFragment(new UserJoinListFragment(), getResources().getString(R.string.tab_name_my_join));
        adapter.addFragment(new UserPublishListFragment(), getResources().getString(R.string.tab_name_my_publish));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SignInActivity.SIGN_IN_REQUEST_CODE
                && resultCode == SignInActivity.SIGN_IN_SUCCESS_RESULT_CODE) {
            PublishIdeaActivity_.intent(getActivity()).start();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
