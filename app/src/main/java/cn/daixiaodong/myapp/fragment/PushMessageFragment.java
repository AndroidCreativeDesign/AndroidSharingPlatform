package cn.daixiaodong.myapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.adapter.PushMessageAdapter;
import cn.daixiaodong.myapp.model.PushMessageModel;
import cn.daixiaodong.myapp.db.PushMessageDao;
import cn.daixiaodong.myapp.fragment.common.BaseFragment;

/**
 * 消息
 */
public class PushMessageFragment extends BaseFragment {

    private View mConvertView;
    private RecyclerView mRecyclerView;
    private PushMessageAdapter mAdapter;
    private List<PushMessageModel> mData;
    private PushMessageDao mDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mConvertView = inflater.inflate(R.layout.fragment_message, container, false);
        return mConvertView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        initData();
        loadData();
    }

    private void initData() {
        mDao = new PushMessageDao(getActivity());
    }

    private void loadData() {
        List<PushMessageModel> pushMessageModels = mDao.findPushMessages();
        if(pushMessageModels != null){
            mData.addAll(pushMessageModels);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) mConvertView.findViewById(R.id.id_rv_message_recycler_view);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        mData = new ArrayList<>();

        mAdapter = new PushMessageAdapter(getActivity(),mData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
