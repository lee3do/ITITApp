package io.itit.db;

import android.content.Context;

import java.util.List;

import cn.trinea.android.common.util.StringUtils;
import de.greenrobot.dao.query.QueryBuilder;

public class DBHelper {

    private static DBHelper instance;
    private static Context mContext;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    private DataDao dataDao;

    private DBHelper() {
    }

    public static DBHelper getInstance() {
        if (instance == null) {
            init(mContext);
        }
        return instance;
    }

    /**
     * 取得DaoMaster
     *
     * @param context
     * @return
     */
    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "itit.db", null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * 取得DaoSession
     *
     * @param context
     * @return
     */
    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public static void init(Context context) {
        mContext = context;
        instance = new DBHelper();
        // 数据库对象
        DaoSession daoSession = getDaoSession(mContext);
        instance.setDataDao(daoSession.getDataDao());
    }

    public static void insertValue(String key, String value) {
        if (StringUtils.isEmpty(key) || value == null) return;
        DataDao dao = DBHelper.getInstance().getDataDao();
        Data data = getDataByKey(key);
        if (data != null) {
            data.setValue(value);
            dao.update(data);
        } else {
            data = new Data();
            data.setKey(key);
            data.setValue(value);
            dao.insert(data);
        }
    }

    public static Data getDataByKey(String key) {
        QueryBuilder<Data> qb = DBHelper.getInstance().getDataDao().queryBuilder();
        qb.where(DataDao.Properties.Key.eq(key));
        List<Data> list = qb.list();
        if (list == null || list.size() == 0) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public static String getUserName() {
        //TODO
        return "";
    }

    public static String getPassword() {
        //TODO
        return "";
    }

    public DataDao getDataDao() {
        return dataDao;
    }

    public void setDataDao(DataDao dataDao) {
        this.dataDao = dataDao;
    }
}