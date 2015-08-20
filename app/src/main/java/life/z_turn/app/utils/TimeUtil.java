package life.z_turn.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by daixiaodong on 15/8/7.
 */
public class TimeUtil {


    public static String date2String(Date date) {
        String template = "yyyy-MM-dd HH:mm";
        SimpleDateFormat format = new SimpleDateFormat(template, Locale.CHINA);
        return format.format(date);
    }
}
