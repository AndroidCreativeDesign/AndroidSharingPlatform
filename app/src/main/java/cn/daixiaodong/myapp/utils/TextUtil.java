package cn.daixiaodong.myapp.utils;

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
}

