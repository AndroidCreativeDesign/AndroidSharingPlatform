package cn.daixiaodong.myapp.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.MainActivity;
import cn.daixiaodong.myapp.activity.MainActivity_;
import cn.daixiaodong.myapp.bean.PushMessage;
import cn.daixiaodong.myapp.db.PushMessageDao;
import cn.daixiaodong.myapp.fragment.SettingsFragment;

/**
 * 接收推送消息的广播接收器
 */
public class PushBroadcastReceiver extends BroadcastReceiver {
    public static final String PUSH_ACTION = "cn.daixiaodong.myapp.push";

    public static final String BROAD_DATA_KEY = "broad_data";

    private static PushMessageDao dao = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        JSONObject json = null;
        String mMessage = null;

        // 处理用户不接受消息推送
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean message_push = sharedPref.getBoolean(SettingsFragment.KEY_MESSAGE_PUSH, true);
        if(!message_push){
            return;
        }

        dao = new PushMessageDao(context);
        if (PUSH_ACTION.equals(action)) {
            Log.i("action", action);
            try {
                String channel = intent.getExtras().getString("com.avos.avoscloud.Channel");
                //获取消息内容
                json = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));
                // Log.i("message", json.getString("message"));
                mMessage = json.getString("message");
                //Log.i("tag", "got action " + action + " on channel " + channel + " with:");
                Iterator itr = json.keys();
                while (itr.hasNext()) {
                    String key = (String) itr.next();
                    // Log.i("tag", "..." + key + " => " + json.getString(key));
                }
            } catch (JSONException e) {
                Log.i("tag", "JSONException: " + e.getMessage());
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentText("收到消息");
            builder.setContentTitle("消息标题");
            builder.setTicker("活动通知");
            builder.setAutoCancel(true);

            Intent resultIntent = new Intent(context, MainActivity_.class);
            resultIntent.setAction(MainActivity.BROAD_RECEIVER_ACTION);
            Bundle data = new Bundle();
            data.putString("title", "杀哈哈哈");
            resultIntent.putExtra(BROAD_DATA_KEY, data);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            builder.setContentIntent(resultPendingIntent);
            // Sets an ID for the notification
            int mNotificationId = 1000;
            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, builder.build());

            if (mMessage != null) {

                PushMessage pushmessage = new PushMessage();
                pushmessage.setMessage(mMessage);
                dao.addPushMessage(pushmessage);
            }
        }

    }
}
