package com.campray.lesswalletandroid.db.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.db.dao.DaoMaster;
import com.campray.lesswalletandroid.db.dao.DaoSession;
import com.campray.lesswalletandroid.db.dao.LocalizedPropertyDao;
import com.campray.lesswalletandroid.db.entity.LocalizedProperty;
import com.campray.lesswalletandroid.db.entity.User;

import java.util.List;

/**
 * 本地化资源表DAO业务处理服务类
 * Created by Phills on 10/30/2017.
 */

public class LocalizedPropertyDaoService {
    private DaoMaster.DevOpenHelper openHelper;
    private static LocalizedPropertyDaoService instance;
    public static synchronized LocalizedPropertyDaoService getInstance(Context context) {
        if (instance == null) {
            User user= LessWalletApplication.INSTANCE().getAccount();
            long userId=0;
            if(user!=null){
                userId=user.getId();
            }
            instance = new LocalizedPropertyDaoService(context,userId);
        }
        return instance;
    }

    private LocalizedPropertyDaoService(Context context,long userId){
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

    /**获取本地所有的本地化资源
     * @return List<LocalizedProperty>
     */
    public List<LocalizedProperty> getAllLocalizedProperties(){
        LocalizedPropertyDao dao =openReadableDb().getLocalizedPropertyDao();
        return dao.queryBuilder().list();
    }

    /**
     * 根据id获取本地化资源数据
     * @param id*
     * @return LocalizedProperty
     */
    public LocalizedProperty getLocalizedProperty(long id){
        LocalizedPropertyDao dao =openReadableDb().getLocalizedPropertyDao();
        return dao.queryBuilder().where(LocalizedPropertyDao.Properties.Id.eq(id)).build().unique();
    }

    /**
     * 根据指定表，指定记录id的所有字段对应的各语言本地化资源数据
     * @param tableName 表名
     * @param entityId 记录Id
     * @return List<LocalizedProperty>
     */
    public List<LocalizedProperty> getLocalizedProperty(String tableName,int entityId){
        LocalizedPropertyDao dao =openReadableDb().getLocalizedPropertyDao();
        return dao.queryBuilder().where(LocalizedPropertyDao.Properties.LocaleKeyGroup.eq(tableName))
                .where(LocalizedPropertyDao.Properties.EntityId.eq(entityId)).build().list();
    }

    /**
     * 根据指定表，指定记录id，指定语言id的对应的各表字段本地化资源数据
     * @param tableName
     * @param entityId
     * @param languageId
     * @return List<LocalizedProperty>
     */
    public List<LocalizedProperty> getLocalizedProperty(String tableName,int entityId,int languageId){
        LocalizedPropertyDao dao =openReadableDb().getLocalizedPropertyDao();
        return dao.queryBuilder().where(LocalizedPropertyDao.Properties.LocaleKeyGroup.eq(tableName))
                .where(LocalizedPropertyDao.Properties.EntityId.eq(entityId))
                .where(LocalizedPropertyDao.Properties.LanguageId.eq(languageId)).build().list();
    }

    /**
     * 根据指定表，指定记录id，指定语言id和指定表字段名的对应的各表字段本地化资源数据
     * @param tableName
     * @param entityId
     * @param languageId
     * @return LocalizedProperty
     */
    public LocalizedProperty getLocalizedProperty(String tableName,int entityId,int languageId,String fieldName){
        LocalizedPropertyDao dao =openReadableDb().getLocalizedPropertyDao();
        return dao.queryBuilder().where(LocalizedPropertyDao.Properties.LocaleKeyGroup.eq(tableName))
                .where(LocalizedPropertyDao.Properties.EntityId.eq(entityId))
                .where(LocalizedPropertyDao.Properties.LanguageId.eq(languageId))
                .where(LocalizedPropertyDao.Properties.LocaleKey.eq(fieldName)).build().unique();
    }

    /**创建或更新本地化资源数据
     * @param localizedProperty
     * @return long:返回插入或修改的id
     */
    public long insertOrUpdateLocalizedProperty(LocalizedProperty localizedProperty){
        LocalizedPropertyDao dao = openWritableDb().getLocalizedPropertyDao();
        return dao.insertOrReplace(localizedProperty);
    }

    /**创建本地化资源数据
     * @param localizedProperty
     * @return long:返回插入或修改的id
     */
    public long insertLocalizedProperty(LocalizedProperty localizedProperty){
        LocalizedPropertyDao dao = openWritableDb().getLocalizedPropertyDao();
        return dao.insert(localizedProperty);
    }

    /**更新本地化资源数据
     * @param localizedProperty
     * @return long:返回插入或修改的id
     */
    public void updateLocalizedProperty(LocalizedProperty localizedProperty){
        LocalizedPropertyDao dao = openWritableDb().getLocalizedPropertyDao();
        dao.update(localizedProperty);
    }

    /**
     * 删除指定的本地化资源数据
     * @param localizedProperty
     */
    public void deleteLocalizedProperty(LocalizedProperty localizedProperty){
        LocalizedPropertyDao dao = openWritableDb().getLocalizedPropertyDao();
        dao.delete(localizedProperty);
    }
}
