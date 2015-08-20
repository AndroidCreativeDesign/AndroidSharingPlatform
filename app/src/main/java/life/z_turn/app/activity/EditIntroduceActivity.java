package life.z_turn.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import life.z_turn.app.R;
import life.z_turn.app.config.Constants;

@EActivity
public class EditIntroduceActivity extends AppCompatActivity {


    private MenuItem mSaveMenuItem;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.edit_introduce)
    EditText mEditIntroduce;


    @AfterViews
    void initViews() {
        setUpToolbar();
        mEditIntroduce.setHint(R.string.activity_introduce);
        mEditIntroduce.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    enableSaveMenuItem(false);
                } else {
                    enableSaveMenuItem(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("编辑");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_introduce);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_introduce, menu);
        mSaveMenuItem = menu.findItem(R.id.action_save);
        updateSaveMenuItem();
        return true;
    }

    public void updateSaveMenuItem() {
        if (mEditIntroduce.getText().toString().trim().isEmpty()) {
            enableSaveMenuItem(false);
        } else {
            enableSaveMenuItem(true);
        }
    }

    private void enableSaveMenuItem(boolean enable) {
        if (mSaveMenuItem == null) {
            return;
        }
        if (enable) {
//            mSaveMenuItem.setIcon(R.drawable.ic_menu_ok);
            mSaveMenuItem.setEnabled(true);
        } else {
//            mSaveMenuItem.setIcon(R.drawable.ic_menu_ok_unable);
            mSaveMenuItem.setEnabled(false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {

            Intent intent = getIntent();
            intent.putExtra(Constants.EXTRA_KEY_INTRODUCE_CONTENT,mEditIntroduce.getText().toString());
            this.setResult(RESULT_OK,intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
