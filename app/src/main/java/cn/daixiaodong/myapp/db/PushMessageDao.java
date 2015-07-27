package cn.daixiaodong.myapp.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import cn.daixiaodong.myapp.model.PushMessageModel;

/**
 * Created by daixiaodong on 15/7/23.
 */
public class PushMessageDao {

    private Context mContext;
    private Dao<PushMessageModel, Integer> mPushMessageDao;
    private DatabaseHelper mHelper;


    public PushMessageDao(Context context) {
        this.mContext = context;
        try {
            mHelper = DatabaseHelper.getInstance(context);
            mPushMessageDao = mHelper.getDao(PushMessageModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * 增加一个推送消息
     *
     * @param pushMessageModel 推送消息对象
     */
    public void addPushMessage(PushMessageModel pushMessageModel) {
        try {
            mPushMessageDao.create(pushMessageModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<PushMessageModel> findPushMessages() {
        List<PushMessageModel> pushMessageModels = null;
        try {
            pushMessageModels = mPushMessageDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pushMessageModels;
    }
}
