package com.campray.lesswalletandroid.db.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.campray.lesswalletandroid.db.dao.CurrencyDao;
import com.campray.lesswalletandroid.db.dao.DaoMaster;
import com.campray.lesswalletandroid.db.dao.DaoSession;
import com.campray.lesswalletandroid.db.entity.Currency;

import java.util.List;

/**
 * 货币表DAO业务处理服务类
 * Created by Phills on 10/30/2017.
 */

public class CurrencyDaoService {
    private DaoMaster.DevOpenHelper openHelper;
    private static CurrencyDaoService instance;
    public static synchronized CurrencyDaoService getInstance(Context context) {
        if (instance == null) {
            instance = new CurrencyDaoService(context);
        }
        return instance;
    }

    private CurrencyDaoService(Context context){
        clear();
        String DBName = "lesswallet";
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

    /**获取本地所有的货币对象
     * @return
     */
    public List<Currency> getAllCurrencies(){
        CurrencyDao dao =openReadableDb().getCurrencyDao();
        return dao.queryBuilder().list();
    }

    /**
     * 根据id获取货币数据
     * @param id*
     * @return
     */
    public Currency getCurrency(long id){
        CurrencyDao dao =openReadableDb().getCurrencyDao();
        return dao.queryBuilder().where(CurrencyDao.Properties.Id.eq(id)).build().unique();
    }

    /**
     * 根据ISO货币码查询货币对象
     * @param code*
     * @return
     */
    public Currency getCurrencyByCode(String code){
        CurrencyDao dao =openReadableDb().getCurrencyDao();
        return dao.queryBuilder().where(CurrencyDao.Properties.CurrencyCode.eq(code)).build().unique();
    }

    /**
     * 根据货币名称查询货币对象
     * @param name*
     * @return
     */
    public Currency getCurrencyByName(String name){
        CurrencyDao dao =openReadableDb().getCurrencyDao();
        return dao.queryBuilder().where(CurrencyDao.Properties.Name.eq(name)).build().unique();
    }

    /**创建或更新货币信息
     * @param currency
     * @return long:返回插入或修改的id
     */
    public long insertOrUpdateCurrency(Currency currency){
        CurrencyDao dao = openWritableDb().getCurrencyDao();
        return dao.insertOrReplace(currency);
    }

    /**创建货币信息
     * @param currency
     * @return long:返回插入或修改的id
     */
    public long insertCurrency(Currency currency){
        CurrencyDao dao = openWritableDb().getCurrencyDao();
        return dao.insert(currency);
    }

    /**更新货币信息
     * @param currency
     * @return long:返回插入或修改的id
     */
    public void updateCurrency(Currency currency){
        CurrencyDao dao = openWritableDb().getCurrencyDao();
        dao.update(currency);
    }

    /**
     * 删除指定的货币
     * @param currency
     */
    public void deleteCurrency(Currency currency){
        CurrencyDao dao = openWritableDb().getCurrencyDao();
        dao.delete(currency);
    }
}
