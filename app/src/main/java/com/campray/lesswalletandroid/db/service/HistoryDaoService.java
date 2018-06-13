package com.campray.lesswalletandroid.db.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.db.dao.HistoryDao;
import com.campray.lesswalletandroid.db.dao.DaoMaster;
import com.campray.lesswalletandroid.db.dao.DaoSession;
import com.campray.lesswalletandroid.db.dao.ProductDao;
import com.campray.lesswalletandroid.db.entity.History;
import com.campray.lesswalletandroid.db.entity.Product;
import com.campray.lesswalletandroid.db.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志DAO业务处理服务类
 * Created by Phills on 10/30/2017.
 */

public class HistoryDaoService {
    private DaoMaster.DevOpenHelper openHelper;
    private static HistoryDaoService instance;
    public static synchronized HistoryDaoService getInstance(Context context) {
        if (instance == null) {
            User user= LessWalletApplication.INSTANCE().getAccount();
            long userId=0;
            if(user!=null){
                userId=user.getId();
            }
            instance = new HistoryDaoService(context,userId);
        }
        return instance;
    }

    private HistoryDaoService(Context context, long userId){
        clear();
        String DBName = userId+"lesswallet";
        this.openHelper = new DaoMaster.DevOpenHelper(context.getApplicationContext(), DBName, null);
    }

    /**
     * 清空资源
     */
    public void clear() {
        if(openHelper !=null) {
            openHelper.close();
            openHelper = null;
        }
    }

    public DaoSession openReadableDb() {
        checkInit();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    public DaoSession openWritableDb(){
        checkInit();
        SQLiteDatabase db = openHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    private void checkInit(){
        if(openHelper ==null){
            throw new RuntimeException("请初始化db");
        }
    }

    //-------------------------------------------------------------

    /**获取所有的日志记录
     * @return
     */
    public List<History> getAllHistorys(){
        HistoryDao dao =openReadableDb().getHistoryDao();
        return dao.queryBuilder().list();
    }

    /**获取所有的日志记录
     * @param pageNum
     * @param pageSize
     */
    public List<History> getAllHistorys(int pageNum,int pageSize){
        HistoryDao dao =openReadableDb().getHistoryDao();
        return dao.queryBuilder().orderDesc(HistoryDao.Properties.CreatedTime).offset((pageNum-1)*pageSize).limit(pageSize).build().list();
    }

    /**
     * 根据id获取日志记录数据
     * @param id*
     * @return
     */
    public History getHistory(long id){
        HistoryDao dao =openReadableDb().getHistoryDao();
        return dao.queryBuilder().where(HistoryDao.Properties.Id.eq(id)).build().unique();
    }

    /**
     * 根据类型查询所有的日志记录对象
     * @param typeId*
     * @return
     */
    public List<History> getAllHistoryByType(int typeId){
        HistoryDao dao =openReadableDb().getHistoryDao();
        return dao.queryBuilder().where(HistoryDao.Properties.Type.eq(typeId)).build().list();
    }

    /**
     * 根据类型查询所有的分页日志记录对象
     * @param typeId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<History> getPageHistoryByType(int typeId,int pageNum,int pageSize){
        HistoryDao dao =openReadableDb().getHistoryDao();
        return dao.queryBuilder().where(HistoryDao.Properties.Type.eq(typeId)).orderDesc(HistoryDao.Properties.CreatedTime).offset((pageNum-1)*pageSize).limit(pageSize).build().list();
    }

    /**
     * 查询所有未读的日志记录
     * @return
     */
    public List<History> getUnreadCount(){
        HistoryDao dao =openReadableDb().getHistoryDao();
        return dao.queryBuilder().where(HistoryDao.Properties.Readed.eq(false)).build().list();
    }


    /**创建或更新日志记录信息
     * @param History
     * @return long:返回插入或修改的id
     */
    public long insertOrUpdateHistory(History History){
        HistoryDao dao = openWritableDb().getHistoryDao();
        return dao.insertOrReplace(History);
    }

    /**创建日志记录
     * @param History
     * @return long:返回插入或修改的id
     */
    public long insertHistory(History History){
        HistoryDao dao = openWritableDb().getHistoryDao();
        return dao.insert(History);
    }

    /**更新日志记录信息
     * @param History
     * @return long:返回插入或修改的id
     */
    public void updateHistory(History History){
        HistoryDao dao = openWritableDb().getHistoryDao();
        dao.update(History);
    }

    /**
     * 删除指定的日志记录
     * @param History
     */
    public void deleteHistory(History History){
        HistoryDao dao = openWritableDb().getHistoryDao();
        dao.delete(History);
    }

    /**
     * 删除指定的日志记录
     * @param HistoryId
     */
    public void deleteHistory(long HistoryId){
        HistoryDao dao = openWritableDb().getHistoryDao();
        dao.deleteByKey(HistoryId);
    }
}
