package life.z_turn.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

import life.z_turn.app.R;
import life.z_turn.app.adapter.PhotosAdapter;
import life.z_turn.app.config.Constants;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.ToastUtil;


public class DepartmentPhotosFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View mConvertView;


    private List<AVObject> mDataSet;
    private String mDepartmentId;
    private View mProgressLoad;

    private RecyclerView mRecyclerView;
    private PhotosAdapter mAdapter;


    public static DepartmentPhotosFragment newInstance(String param1, String param2) {
        DepartmentPhotosFragment fragment = new DepartmentPhotosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DepartmentPhotosFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDepartmentId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mConvertView = inflater.inflate(R.layout.fragment_association_photos, container, false);
        return mConvertView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
        loadData();
    }

    private void loadData() {
        AVQuery<AVObject> query = new AVQuery<>(Constants.TABLE_DEPARTMENT_PHOTO);
        query.whereEqualTo(Constants.COLUMN_DEPARTMENT_ID, mDepartmentId);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mProgressLoad.setVisibility(View.GONE);
                if (e == null) {
                    if (!list.isEmpty()) {
                        mProgressLoad.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mDataSet.addAll(list);
                        mAdapter.notifyDataSetChanged();

                    }
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(getActivity(), e);
                }
            }
        });
    }

    private void initViews() {
        mProgressLoad = mConvertView.findViewById(R.id.progress_load);
        mRecyclerView = (RecyclerView) mConvertView.findViewById(R.id.rv_photos);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mDataSet = new ArrayList<>();
        mAdapter = new PhotosAdapter(getActivity(), mDataSet);
        mRecyclerView.setAdapter(mAdapter);
    }
}
