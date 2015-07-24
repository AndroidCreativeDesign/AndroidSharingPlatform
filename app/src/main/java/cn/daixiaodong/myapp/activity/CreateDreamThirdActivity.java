package cn.daixiaodong.myapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;
import cn.daixiaodong.myapp.utils.ActivityCollector;

/**
 * Created by daixiaodong on 15/7/16.
 */
@EActivity(R.layout.activity_create_dream_third_step)
@OptionsMenu(R.menu.menu_create_dream_third)
public class CreateDreamThirdActivity extends BaseActivity {


    @ViewById(R.id.id_tb_toolbar)
    Toolbar mViewToolbar;

    @Extra("dream")
    HashMap<String,Object> dream;


    @AfterViews
    void init(){
        initToolbar();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @AfterExtras
    void getDream(){
        Log.i("third", dream.get("title") + "");

    }

    @OptionsItem(R.id.action_done)
    void done(){
        ActivityCollector.finishActivity(CreateDreamActivity_.class.getSimpleName());
        ActivityCollector.finishActivity(CreateDreamSecondActivity_.class.getSimpleName());
        finish();
    }

    private void initToolbar() {
        setSupportActionBar(mViewToolbar);
        mViewToolbar.setTitle("创建");
        mViewToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_48dp);

        mViewToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mViewToolbar.setTitleTextColor(Color.WHITE);
    }




}
