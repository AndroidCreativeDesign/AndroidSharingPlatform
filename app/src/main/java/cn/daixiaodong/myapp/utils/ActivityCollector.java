package cn.daixiaodong.myapp.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daixiaodong on 15/7/17.
 */
public class ActivityCollector {

    public static List<Activity> collector = new ArrayList<>();

    public static Map<String, Activity> activities = new HashMap<>();


    public static void addActivity(Activity activity) {
        collector.add(activity);
    }

    public static void addActivity(String classSimpleName, Activity activity) {
        activities.put(classSimpleName, activity);
    }

    public static void removeActivity(Activity activity) {
        collector.remove(activity);
    }

    public static void removeActivity(String classSimpleName, Activity activity) {
        activities.remove(classSimpleName);
    }


    public static void finishActivity(String classSimpleName) {
        if (activities.get(classSimpleName) != null) {
            activities.get(classSimpleName).finish();
            activities.remove(classSimpleName);
        }
    }

    public static void finishAllActivities() {
        for (Map.Entry<String, Activity> entry : activities.entrySet()) {
            entry.getValue().finish();
        }
    }

    public static void finishAll() {
        for (Activity activity : collector) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

}
