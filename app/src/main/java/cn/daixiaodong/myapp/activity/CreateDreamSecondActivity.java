package cn.daixiaodong.myapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.avos.avoscloud.AVObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;


@EActivity(R.layout.activity_create_dream_first_step)
//@OptionsMenu(R.menu.menu_create_dream_first_second)
public class CreateDreamSecondActivity extends BaseActivity {

    @Extra("dream")
    HashMap<String,Object> dream;

    @Extra("dreamBean")
    AVObject dreamBean;

    @ViewById(R.id.id_tb_toolbar)
    Toolbar mViewToolbar;


    @AfterViews
    void init(){
        initToolbar();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_dream_first_second, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_next:
                CreateDreamThirdActivity_.intent(this).start();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /* @OptionsItem(R.id.action_next)
    void next(){
        Log.i("second",dream.get("dream")+"");
        CreateDreamThirdActivity_.intent(this).start();
    }*/


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
