package com.campray.lesswalletandroid.db.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.campray.lesswalletandroid.db.dao.CountryDao;
import com.campray.lesswalletandroid.db.dao.DaoMaster;
import com.campray.lesswalletandroid.db.dao.DaoSession;
import com.campray.lesswalletandroid.db.entity.Country;

import java.util.List;

/**
 * 国家表DAO业务处理服务类
 * Created by Phills on 10/30/2017.
 */

public class CountryDaoService {
    private DaoMaster.DevOpenHelper openHelper;
    private static CountryDaoService instance;
    public static synchronized  CountryDaoService getInstance(Context context) {
        if (instance == null) {
            instance = new CountryDaoService(context);
        }
        return instance;
    }

    private CountryDaoService(Context context){
        clear();
        String DBName = "lesswallet";
        this.openHelper = new DaoMaster.DevOpenHelper(context.getApplicationContext(), DBName);
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

    /**获取本地所有的国家
     * @return
     */
    public List<Country> getAllCountries(){
        CountryDao dao =openReadableDb().getCountryDao();
        return dao.queryBuilder().list();
    }

    /**
     * 根据id获取国家数据
     * @param id*
     * @return
     */
    public Country getCountry(long id){
        CountryDao dao =openReadableDb().getCountryDao();
        return dao.queryBuilder().where(CountryDao.Properties.Id.eq(id)).build().unique();
    }

    /**
     * 根据二位的ISO国家码查询国家对象
     * @param twoLetterIsoCode*
     * @return
     */
    public Country getCountryByCode(String twoLetterIsoCode){
        CountryDao dao =openReadableDb().getCountryDao();
        return dao.queryBuilder().where(CountryDao.Properties.TwoLetterIsoCode.eq(twoLetterIsoCode)).build().unique();
    }

    /**
     * 根据国家名称查询国家对象
     * @param name*
     * @return
     */
    public Country getCountryByName(String name){
        CountryDao dao =openReadableDb().getCountryDao();
        return dao.queryBuilder().where(CountryDao.Properties.Name.eq(name)).build().unique();
    }

    /**创建或更新国家信息
     * @param country
     * @return long:返回插入或修改的id
     */
    public long insertOrUpdateCountry(Country country){
        CountryDao dao = openWritableDb().getCountryDao();
        return dao.insertOrReplace(country);
    }

    /**创建国家信息
     * @param country
     * @return long:返回插入或修改的id
     */
    public long insertCountry(Country country){
        CountryDao dao = openWritableDb().getCountryDao();
        return dao.insert(country);
    }

    /**更新国家信息
     * @param country
     * @return long:返回插入或修改的id
     */
    public void updateCountry(Country country){
        CountryDao dao = openWritableDb().getCountryDao();
        dao.update(country);
    }

    /**
     * 删除指定的国家
     * @param country
     */
    public void deleteCountry(Country country){
        CountryDao dao = openWritableDb().getCountryDao();
        dao.delete(country);
    }
}
