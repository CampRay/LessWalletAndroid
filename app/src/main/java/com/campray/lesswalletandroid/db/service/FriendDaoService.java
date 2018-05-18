package com.campray.lesswalletandroid.db.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.db.dao.FriendDao;
import com.campray.lesswalletandroid.db.dao.DaoMaster;
import com.campray.lesswalletandroid.db.dao.DaoSession;
import com.campray.lesswalletandroid.db.dao.ProductDao;
import com.campray.lesswalletandroid.db.entity.Friend;
import com.campray.lesswalletandroid.db.entity.Product;
import com.campray.lesswalletandroid.db.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 好友表DAO业务处理服务类
 * Created by Phills on 3/30/2018.
 */

public class FriendDaoService {
    private DaoMaster.DevOpenHelper openHelper;
    private static FriendDaoService instance;
    public static synchronized FriendDaoService getInstance(Context context) {
        if (instance == null) {
            User user= LessWalletApplication.INSTANCE().getAccount();
            long userId=0;
            if(user!=null){
                userId=user.getId();
            }
            instance = new FriendDaoService(context,userId);
        }
        return instance;
    }

    private FriendDaoService(Context context, long userId){
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

    /**获取所有的好友
     * @return
     */
    public List<Friend> getAllFriends(){
        FriendDao dao =openReadableDb().getFriendDao();
        return dao.queryBuilder().list();
    }

    /**
     * 根据id获取好友数据
     * @param id*
     * @return
     */
    public Friend getFriend(long id){
        FriendDao dao =openReadableDb().getFriendDao();
        return dao.queryBuilder().where(FriendDao.Properties.Id.eq(id)).build().unique();
    }

    /**
     * 根据名称查询的好友对象
     * @param name*
     * @return
     */
    public Friend getFriendByName(String name){
        FriendDao dao =openReadableDb().getFriendDao();
        return dao.queryBuilder().where(FriendDao.Properties.UserName.eq(name)).build().unique();
    }

    /**
     * 根据搜索符查询的名称或电话符合的好友对象
     * @param str*
     * @return
     */
    public Friend getFriendBySearch(String str){
        FriendDao dao =openReadableDb().getFriendDao();
        return dao.queryBuilder().whereOr(FriendDao.Properties.UserName.eq(str),FriendDao.Properties.Email.eq(str)).build().unique();
    }



    /**创建或更新好友信息
     * @param Friend
     * @return long:返回插入或修改的id
     */
    public long insertOrUpdateFriend(Friend Friend){
        FriendDao dao = openWritableDb().getFriendDao();
        return dao.insertOrReplace(Friend);
    }

    /**创建好友信息
     * @param Friend
     * @return long:返回插入或修改的id
     */
    public long insertFriend(Friend Friend){
        FriendDao dao = openWritableDb().getFriendDao();
        return dao.insert(Friend);
    }

    /**更新好友信息
     * @param Friend
     * @return long:返回插入或修改的id
     */
    public void updateFriend(Friend Friend){
        FriendDao dao = openWritableDb().getFriendDao();
        dao.update(Friend);
    }

    /**
     * 删除指定的好友
     * @param Friend
     */
    public void deleteFriend(Friend Friend){
        FriendDao dao = openWritableDb().getFriendDao();
        dao.delete(Friend);
    }

    /**
     * 删除指定的好友
     * @param FriendId
     */
    public void deleteFriend(long FriendId){
        FriendDao dao = openWritableDb().getFriendDao();
        dao.deleteByKey(FriendId);
    }
}
