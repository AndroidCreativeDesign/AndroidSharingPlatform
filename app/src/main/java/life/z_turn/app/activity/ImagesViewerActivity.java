package life.z_turn.app.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import life.z_turn.app.R;
import life.z_turn.app.config.Constants;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.ToastUtil;
import uk.co.senab.photoview.PhotoView;


@EActivity
public class ImagesViewerActivity extends AppCompatActivity {

    public List<AVObject> mDataSet;

    @Extra(Constants.EXTRA_KEY_OBJECT_ID)
    String mAssociationId;

    @Extra(Constants.EXTRA_KEY_POSITION)
    int mPosition;


    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.view_pager)
    ViewPager mViewPager;
    private SamplePagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_viewer);
        setToolbar();
        mDataSet = new ArrayList<>();
        mAdapter = new SamplePagerAdapter();
        mViewPager.setAdapter(mAdapter);
        loadData();
    }

    private void setToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SamplePagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {

            return mDataSet.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            Picasso.with(ImagesViewerActivity.this).load(mDataSet.get(position).getString("url")).into(photoView);
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }


    private void loadData() {
        AVQuery<AVObject> query = new AVQuery<>(Constants.TABLE_ASSOCIATION_PHOTO);
        query.whereEqualTo(Constants.COLUMN_ASSOCIATION_ID, mAssociationId);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

                if (e == null) {
                    if (!list.isEmpty()) {
                        mDataSet.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        mViewPager.setCurrentItem(mPosition);
                    }
                } else {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(ImagesViewerActivity.this, e);
                }
            }
        });
    }
}
