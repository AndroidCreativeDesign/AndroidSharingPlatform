package life.z_turn.app.activity.common;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by daixiaodong on 15/7/14.
 */
public abstract class ToolbarActivity extends AppCompatActivity {

    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private boolean isHidden;


    protected abstract int getLayoutResId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        initViews();
        if (mAppBarLayout == null || mToolbar == null) {
            throw new NullPointerException("no AppBarLayout or Toolbar");
        }
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolbarClick();
            }
        });
        setSupportActionBar(mToolbar);
        if (Build.VERSION.SDK_INT >= 21) {
            mAppBarLayout.setElevation(10.6f);
        }
    }

    protected void onToolbarClick() {

    }

    private void initViews() {
       // mAppBarLayout = (AppBarLayout) findViewById(R.id.id_app_bar_layout);
       // mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
    }

    protected void setAppBarAlpha(float alpha) {
        mAppBarLayout.setAlpha(alpha);
    }

    protected void hideOrShowToolbar() {

        if (isHidden) {
           /* ObjectAnimator.ofFloat(mAppBarLayout, "translationY", -mAppBarLayout.getHeight(), 0)
                    .start();*/
            mAppBarLayout.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(2))
                    .start();

        } else {
            ObjectAnimator.ofFloat(mAppBarLayout, "translationY", 0, -mAppBarLayout.getHeight())
                    .start();
        }

        isHidden = !isHidden;
    }

}
