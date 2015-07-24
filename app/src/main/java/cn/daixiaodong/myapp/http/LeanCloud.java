package cn.daixiaodong.myapp.http;


import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LeanCloud {
    public static void save(String tableName, HashMap<String, Object> data, SaveCallback callback) {
        if (tableName == null || data == null || callback == null) {
            throw new IllegalArgumentException();
        }
        AVObject object = new AVObject(tableName);
        Set<Map.Entry<String, Object>> entries = data.entrySet();
        for (Map.Entry entry : entries) {
            object.put((String) entry.getKey(), entry.getValue());
        }
        object.saveInBackground(callback);

    }


}
