package com.campray.lesswalletandroid.db.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.db.dao.DaoMaster;
import com.campray.lesswalletandroid.db.dao.DaoSession;
import com.campray.lesswalletandroid.db.dao.MerchantDao;
import com.campray.lesswalletandroid.db.entity.Merchant;
import com.campray.lesswalletandroid.db.entity.User;

import java.util.List;

/**
 * 商家表DAO业务处理服务类
 * Created by Phills on 10/30/2017.
 */

public class MerchantDaoService {
    private DaoMaster.DevOpenHelper openHelper;
    private static MerchantDaoService instance;
    public static synchronized MerchantDaoService getInstance(Context context) {
        if (instance == null) {
            User user= LessWalletApplication.INSTANCE().getAccount();
            long userId=0;
            if(user!=null){
                userId=user.getId();
            }
            instance = new MerchantDaoService(context,userId);
        }
        return instance;
    }

    private MerchantDaoService(Context context,long userId){
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

    /**获取本地所有的商家
     * @return
     */
    public List<Merchant> getAllMerchants(){
        MerchantDao dao =openReadableDb().getMerchantDao();
        return dao.queryBuilder().list();
    }

    /**
     * 根据id获取商家数据
     * @param id*
     * @return
     */
    public Merchant getMerchant(long id){
        MerchantDao dao =openReadableDb().getMerchantDao();
        return dao.queryBuilder().where(MerchantDao.Properties.Id.eq(id)).build().unique();
    }

    /**
     * 根据商家名称查询商家对象
     * @param name*
     * @return
     */
    public Merchant getMerchantByName(String name){
        MerchantDao dao =openReadableDb().getMerchantDao();
        return dao.queryBuilder().where(MerchantDao.Properties.Name.eq(name)).build().unique();
    }

    /**创建或更新商家信息
     * @param merchant
     * @return long:返回插入或修改的id
     */
    public long insertOrUpdateMerchant(Merchant merchant){
        MerchantDao dao = openWritableDb().getMerchantDao();
        return dao.insertOrReplace(merchant);
    }

    /**创建商家信息
     * @param merchant
     * @return long:返回插入或修改的id
     */
    public long insertMerchant(Merchant merchant){
        MerchantDao dao = openWritableDb().getMerchantDao();
        return dao.insert(merchant);
    }

    /**更新商家信息
     * @param merchant
     * @return long:返回插入或修改的id
     */
    public void updateMerchant(Merchant merchant){
        MerchantDao dao = openWritableDb().getMerchantDao();
        dao.update(merchant);
    }

    /**
     * 删除指定的商家
     * @param merchant
     */
    public void deleteMerchant(Merchant merchant){
        MerchantDao dao = openWritableDb().getMerchantDao();
        dao.delete(merchant);
    }
}
