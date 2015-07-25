package cn.daixiaodong.myapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import cn.daixiaodong.myapp.model.PushMessageModel;

/**
 * Created by daixiaodong on 15/7/21.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {






    private static final String TABLE_NAME = "push_message";


    private static final String DATABASE_NAME = "data.db";

    private Map<String, Dao> mDaos = new HashMap<>();

    private DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, PushMessageModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, PushMessageModel.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context, null, null, 1);
                }
            }
        }
        return instance;
    }

    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();
        if(mDaos.containsKey(className)){
            dao = mDaos.get(className);
        }
        if(dao == null){
            dao = super.getDao(clazz);
            mDaos.put(className, dao);
        }
        return dao;
    }




    @Override
    public void close() {
        super.close();
        for(String key : mDaos.keySet()){
            Dao dao = mDaos.get(key);
            dao = null;
        }
    }
}
