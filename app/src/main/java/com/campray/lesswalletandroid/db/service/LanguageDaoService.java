package com.campray.lesswalletandroid.db.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.campray.lesswalletandroid.db.dao.CountryDao;
import com.campray.lesswalletandroid.db.dao.DaoMaster;
import com.campray.lesswalletandroid.db.dao.DaoSession;
import com.campray.lesswalletandroid.db.dao.LanguageDao;
import com.campray.lesswalletandroid.db.entity.Language;

import java.util.List;

/**
 * 语言表DAO业务处理服务类
 * Created by Phills on 10/30/2017.
 */

public class LanguageDaoService {
    private DaoMaster.DevOpenHelper openHelper;
    private static LanguageDaoService instance;
    public static synchronized LanguageDaoService getInstance(Context context) {
        if (instance == null) {
            instance = new LanguageDaoService(context);
        }
        return instance;
    }

    private LanguageDaoService(Context context){
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

    /**获取本地所有的语言
     * @return
     */
    public List<Language> getAllLanguages(){
        LanguageDao dao =openReadableDb().getLanguageDao();
        return dao.queryBuilder().list();
    }

    /**
     * 根据id获取语言数据
     * @param id*
     * @return
     */
    public Language getLanguage(long id){
        LanguageDao dao =openReadableDb().getLanguageDao();
        return dao.queryBuilder().where(CountryDao.Properties.Id.eq(id)).build().unique();
    }

    /**
     * 根据语言码查询语言对象
     * @param code*
     * @return
     */
    public Language getLanguageByCode(String code){
        LanguageDao dao =openReadableDb().getLanguageDao();
        return dao.queryBuilder().where(LanguageDao.Properties.UniqueSeoCode.eq(code)).build().unique();
    }

    /**
     * 根据语言码查询语言对象
     * @param culture*
     * @return
     */
    public Language getLanguageByCulture(String culture){
        LanguageDao dao =openReadableDb().getLanguageDao();
        return dao.queryBuilder().where(LanguageDao.Properties.LanguageCulture.eq(culture)).build().unique();
    }

    /**
     * 根据国家名称查询语言对象
     * @param name*
     * @return
     */
    public Language geLanguageByName(String name){
        LanguageDao dao =openReadableDb().getLanguageDao();
        return dao.queryBuilder().where(LanguageDao.Properties.Name.eq(name)).build().unique();
    }

    /**创建或更新语言信息
     * @param language
     * @return long:返回插入或修改的id
     */
    public long insertOrUpdateLanguage(Language language){
        LanguageDao dao = openWritableDb().getLanguageDao();
        return dao.insertOrReplace(language);
    }

    /**创建语言信息
     * @param language
     * @return long:返回插入或修改的id
     */
    public long insertLanguage(Language language){
        LanguageDao dao = openWritableDb().getLanguageDao();
        return dao.insert(language);
    }

    /**更新语言信息
     * @param language
     * @return long:返回插入或修改的id
     */
    public void updateLanguage(Language language){
        LanguageDao dao = openWritableDb().getLanguageDao();
        dao.update(language);
    }

    /**
     * 删除指定的语言
     * @param language
     */
    public void deleteLanguage(Language language){
        LanguageDao dao = openWritableDb().getLanguageDao();
        dao.delete(language);
    }
}
