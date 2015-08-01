package cn.daixiaodong.myapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.adapter.PhotosAdapter;
import cn.daixiaodong.myapp.model.PhotoModel;


public class AssociationPhotosFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View mConvertView;

    private String mParam1;
    private String mParam2;
    private List<PhotoModel> mPhotosDataSet;


    private String[] photos = {"http://ww2.sinaimg.cn/large/610dc034gw1eqnjfdn45qj20h30mk443.jpg",
            "http://ww2.sinaimg.cn/large/610dc034gw1eqoqwkyy8cj20h20h10wz.jpg",
            "http://ww4.sinaimg.cn/large/610dc034gw1eqpx7qtursj20go0go779.jpg",
            "http://ww3.sinaimg.cn/large/610dc034gw1eqr2vp3xtcj20m80m8jwd.jpg",
            "http://ww3.sinaimg.cn/large/610dc034gw1eqs82kt4e9j20m80tnwjo.jpg",
            "http://ww3.sinaimg.cn/large/610dc034gw1eqwuw1t94yj20ga0ib405.jpg",
            "http://ww4.sinaimg.cn/large/610dc034gw1eqxzn23bc3j20m80etjuo.jpg",
            "http://ww2.sinaimg.cn/large/610dc034gw1eqz66m9qctj20go0okdif.jpg",
            "http://ww1.sinaimg.cn/large/610dc034gw1er0c8agag2j20m80bjt93.jpg",
            "http://ww2.sinaimg.cn/large/610dc034jw1er3t0hhn8dj20m80tn76d.jpg",
            "http://ww3.sinaimg.cn/large/610dc034gw1er4yt4dy15j20m80etwff.jpg",
            "http://ww1.sinaimg.cn/large/610dc034gw1er645i2y90j20hb0kimyr.jpg",
            "http://ww3.sinaimg.cn/large/610dc034gw1er79vdrfvqj20b40jraao.jpg",
            "http://ww2.sinaimg.cn/large/610dc034gw1er8fhea7vnj20b40deq3i.jpg",
            "http://ww3.sinaimg.cn/large/610dc034gw1erbum2ltm6j20go0caab3.jpg",
            "http://ww3.sinaimg.cn/large/610dc034gw1erd1rhreacj20m80efjsd.jpg",
            "http://ww2.sinaimg.cn/large/610dc034gw1ere7awhfj0j20go0b3dg8.jpg",
            "http://ww4.sinaimg.cn/large/610dc034gw1erfcxwxlvuj20m80gzmyw.jpg",
            "http://ww4.sinaimg.cn/large/610dc034gw1ergiue1xlbj20m80gq757.jpg",
            "http://ww3.sinaimg.cn/large/610dc034gw1erjtx9odarj20m80e2mxw.jpg",
            "http://ww3.sinaimg.cn/large/610dc034gw1erm9yr0v83j20m80snjt2.jpg",
            "http://ww4.sinaimg.cn/large/610dc034gw1erng5ktg5ij20m80eumy7.jpg",
            "http://ww1.sinaimg.cn/large/610dc034gw1ers1ue9tizj20m80euq3k.jpg",
            "http://ww3.sinaimg.cn/large/610dc034jw1erudbbww3xj20go0p075j.jpg",
            "http://ww1.sinaimg.cn/large/610dc034gw1ervje0eqqbj20b40gmjry.jpg",
            "http://ww3.sinaimg.cn/large/610dc034gw1erwpilp4kjj20go0p00tr.jpg",
            "http://ww1.sinaimg.cn/large/610dc034jw1es0jgf2v91j20go0p2ab1.jpg",
            "http://ww2.sinaimg.cn/large/610dc034gw1es1dap6rvgj20m80eugmi.jpg",
            "http://ww1.sinaimg.cn/large/610dc034jw1es2hkc090aj20go0p0dgu.jpg",
            "http://ww3.sinaimg.cn/large/610dc034jw1es3mty6nm2j20go0n60t9.jpg",
            "http://ww2.sinaimg.cn/large/610dc034gw1es4si7kzebj20m80eu0te.jpg",
            "http://ww3.sinaimg.cn/large/610dc034gw1es89uzch20j20pw0xcadb.jpg"};


    public static AssociationPhotosFragment newInstance(String param1, String param2) {
        AssociationPhotosFragment fragment = new AssociationPhotosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AssociationPhotosFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
    }

    private void initViews() {
        mPhotosDataSet = new ArrayList<>();
        PhotoModel photoModel;
        for (String url : photos) {
            photoModel = new PhotoModel(url);
            mPhotosDataSet.add(photoModel);
        }
        RecyclerView photos = (RecyclerView) mConvertView.findViewById(R.id.rv_photos);
        photos.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        photos.setItemAnimator(new DefaultItemAnimator());

        PhotosAdapter adapter = new PhotosAdapter(getActivity(), mPhotosDataSet);
        photos.setAdapter(adapter);
    }
}
