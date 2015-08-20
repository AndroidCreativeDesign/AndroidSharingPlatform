package life.z_turn.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.avos.avoscloud.AVUser;

/**
 * Created by daixiaodong on 15/8/16.
 */
public class SharedPreferencesUtil {


    public static void saveUserInfo(Context context) {
        AVUser user = AVUser.getCurrentUser();
        SharedPreferences preferences = context.getSharedPreferences("user.info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", user.getUsername());
        editor.putInt("college", user.getInt("college"));
        editor.putInt("gender", user.getInt("gender"));
        editor.putInt("year", user.getInt("year"));
        editor.apply();
    }

    public static void saveUserUsername(Context context, String username) {
        SharedPreferences preferences = context.getSharedPreferences("user.info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        LogUtil.i("username", username);
        editor.apply();
    }

    public static void saveUserGender(Context context, int gender) {
        SharedPreferences preferences = context.getSharedPreferences("user.info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("gender", gender);
        editor.apply();
    }

    public static void saveUserCollege(Context context, int college) {
        SharedPreferences preferences = context.getSharedPreferences("user.info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("college", college);
        editor.apply();
    }

    public static void saveUserYear(Context context, int year) {
        SharedPreferences preferences = context.getSharedPreferences("user.info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("year", year);
        editor.apply();
    }

    public static AVUser readUserInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user.info", Context.MODE_PRIVATE);
        String username = preferences.getString("username", null);
        int gender = preferences.getInt("gender", 1);
        int college = preferences.getInt("college", 1);
        int year = preferences.getInt("year", 1);
        String profilePhotoUrl = preferences.getString("profilePhotoUrl", null);
        AVUser user = new AVUser();
        user.setUsername(username);
        user.put("gender", gender);
        user.put("college", college);
        user.put("year", year);
        user.put("profilePhotoUrl", profilePhotoUrl);
        return user;
    }

    public static void clearUserInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user.info", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.apply();
    }


    public static void saveUserProfilePhotoUrl(Context context, String url) {
        SharedPreferences preferences = context.getSharedPreferences("user.info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("profilePhotoUrl", url);
        editor.apply();
    }
}
