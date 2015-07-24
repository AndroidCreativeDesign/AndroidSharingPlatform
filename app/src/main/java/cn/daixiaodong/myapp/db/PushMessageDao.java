package cn.daixiaodong.myapp.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import cn.daixiaodong.myapp.bean.PushMessage;

/**
 * Created by daixiaodong on 15/7/23.
 */
public class PushMessageDao {

    private Context mContext;
    private Dao<PushMessage, Integer> mPushMessageDao;
    private DatabaseHelper mHelper;


    public PushMessageDao(Context context) {
        this.mContext = context;
        try {
            mHelper = DatabaseHelper.getInstance(context);
            mPushMessageDao = mHelper.getDao(PushMessage.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * 增加一个推送消息
     *
     * @param pushMessage 推送消息对象
     */
    public void addPushMessage(PushMessage pushMessage) {
        try {
            mPushMessageDao.create(pushMessage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<PushMessage> findPushMessages() {
        List<PushMessage> pushMessages = null;
        try {
            pushMessages = mPushMessageDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pushMessages;
    }
}
