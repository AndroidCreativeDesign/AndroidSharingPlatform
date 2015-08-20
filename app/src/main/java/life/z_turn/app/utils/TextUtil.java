package life.z_turn.app.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by daixiaodong on 15/8/6.
 */
public class TextUtil {
    public static boolean isEmail(String email) {


        return Pattern.compile("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$").matcher(email).matches();

        /*if(email == null || email.isEmpty()){
            return false;
        }
        // 邮箱验证规则
        String regEx = "^[a-zA-Z][\\\\w\\\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\\\w\\\\.-]*[a-zA-Z0-9]\\\\.[a-zA-Z][a-zA-Z\\\\.]*[a-zA-Z]$";
        // 编译正则表达式
        Pattern pattern = Pattern.compile("^\\\\w+((-\\\\w+)|(\\\\.\\\\w+))*\\\\@[A-Za-z0-9]+((\\\\.|-)[A-Za-z0-9]+)*\\\\.[A-Za-z0-9]+$");
        // 忽略大小写的写法
        //Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        Log.i("sdfsdfds",matcher.matches()+"");
        return matcher.matches();*/
    }


    public static boolean isMobilePhoneNum(String mobilePhoneNumber) {
        Pattern pattern = Pattern.compile("^1[3|4|5|8][0-9]\\d{8}$");
        Matcher matcher = pattern.matcher(mobilePhoneNumber);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isUsername(String username){
        Pattern pattern = Pattern.compile("^[a-zA-Z\\u4e00-\\u9fa5][a-zA-Z\\d\\u4e00-\\u9fa5]{1,7}$|^[A-Za-z_]{1,14}$");
        Matcher matcher = pattern.matcher(username);
        if(!matcher.matches()){
            return true;
        }
        return false;
    }
}

