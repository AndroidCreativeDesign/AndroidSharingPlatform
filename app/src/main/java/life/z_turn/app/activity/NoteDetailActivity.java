package life.z_turn.app.activity;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import life.z_turn.app.R;
import life.z_turn.app.config.Constants;


@EActivity(R.layout.activity_note_detail)
public class NoteDetailActivity extends AppCompatActivity implements Html.ImageGetter {

    @Extra(Constants.EXTRA_KEY_OBJECT_STRING)
    String mObjectString;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.text_content)
    TextView mTextContent;

    @AfterViews
    void init() {
        setUpToolbar();
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        try {
            AVObject object = AVObject.parseAVObject(mObjectString);
            mTextContent.setText(Html.fromHtml("dlskfjldfj<br/>2222dsfsf",this,null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                finish();
                break;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Drawable getDrawable(String source) {



        return null;
    }
}
