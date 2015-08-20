package life.z_turn.app.view;

import android.text.TextPaint;
import android.text.style.UnderlineSpan;

/**
 * Created by daixiaodong on 15/8/6.
 */
public class NoUnderlineSpan extends UnderlineSpan {
    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
    }
}
