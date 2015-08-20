package life.z_turn.app.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.bmob.pay.tool.PayListener;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import life.z_turn.app.R;
import life.z_turn.app.activity.common.BaseActivity;
import life.z_turn.app.config.Constants;
import life.z_turn.app.utils.ExceptionUtil;
import life.z_turn.app.utils.ToastUtil;


@EActivity
public class PayActivity extends BaseActivity implements PayListener {


    @Extra(Constants.EXTRA_KEY_ASSOCIATION_STRING_OBJECT)
    String mAssociationStringObject;
/*

    @ViewById(R.id.llayout_pay_by_alipay)
    LinearLayout mLLayoutPayByAlipay;



    @Click(R.id.llayout_pay_by_alipay)
    void payByAlipay(){
        new BmobPay(this).pay(0.01, "会费", this);
    }
*/

    private AVObject mJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        try {
            mJoin = AVObject.parseAVObject(mAssociationStringObject);
        } catch (Exception e) {
            ExceptionUtil.printStackTrace(e);
            ToastUtil.showErrorMessageToastByException(this,e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pay, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public void orderId(String s) {
        mJoin.put("orderId", s);
        mJoin.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e != null) {
                    ExceptionUtil.printStackTrace(e);
                    ToastUtil.showErrorMessageToastByException(PayActivity.this, e);
                }
            }
        });
    }

    @Override
    public void succeed() {
        showToast("报名成功");
        mJoin.put("payStatus", 0);
        mJoin.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                setResult(RESULT_OK, getIntent());
                finish();
            }
        });

    }

    @Override
    public void fail(int code, String s) {

        switch (code) {
            case Constants.ERROR_USER_CANCEL_WEPAY:
            case Constants.ERROR_USER_CANCEL_ALIPAY:
                showToast("支付取消");

                break;
            case Constants.ERROR_NO_INSTALL_WEIXIN:
                showToast("未安装微信客户端");

                break;
            case Constants.ERROR_NO_INSTALL_PLUGIN:
                showToast("未安装微信支付插件");

        }
        showToast("支付失败");
        mJoin.put("payStatus", 1);
        mJoin.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {

            }
        });

    }

    @Override
    public void unknow() {
        showToast("发生未知错误");
        mJoin.put("payStatus", 2);
        mJoin.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                //setResult(RESULT_PAY_FAIL, getIntent());
                //finish();
            }
        });

    }
}
