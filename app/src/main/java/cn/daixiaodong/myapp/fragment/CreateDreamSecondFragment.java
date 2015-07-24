package cn.daixiaodong.myapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.fragment.common.BaseFragment;


public class CreateDreamSecondFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_dream_second_step,container,false);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_dream_first_second, menu);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
}
