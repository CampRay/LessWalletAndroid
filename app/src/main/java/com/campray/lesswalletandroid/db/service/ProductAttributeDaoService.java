package com.campray.lesswalletandroid.db.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.campray.lesswalletandroid.db.dao.ProductAttributeDao;
import com.campray.lesswalletandroid.db.dao.DaoMaster;
import com.campray.lesswalletandroid.db.dao.DaoSession;
import com.campray.lesswalletandroid.db.entity.ProductAttribute;
import com.campray.lesswalletandroid.db.entity.ProductAttribute;

import java.util.List;

/**
 * 商品属性表DAO业务处理服务类
 * Created by Phills on 10/30/2017.
 */

public class ProductAttributeDaoService {
    private DaoMaster.DevOpenHelper openHelper;
    private static ProductAttributeDaoService instance;
    public static synchronized ProductAttributeDaoService getInstance(Context context) {
        if (instance == null) {
            instance = new ProductAttributeDaoService(context);
        }
        return instance;
    }

    private ProductAttributeDaoService(Context context){
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

    /**获取本地所有的商品属性
     * @return
     */
    public List<ProductAttribute> getAllProdAttrs(){
        ProductAttributeDao dao =openReadableDb().getProductAttributeDao();
        return dao.queryBuilder().list();
    }

    /**
     * 根据id获取商品属性数据
     * @param id*
     * @return
     */
    public ProductAttribute getProductAttribute(long id){
        ProductAttributeDao dao =openReadableDb().getProductAttributeDao();
        return dao.queryBuilder().where(ProductAttributeDao.Properties.Id.eq(id)).build().unique();
    }

    /**创建或更新商品属性信息
     * @param ProductAttribute
     * @return long:返回插入或修改的id
     */
    public long insertOrUpdateProductAttribute(ProductAttribute ProductAttribute){
        ProductAttributeDao dao = openWritableDb().getProductAttributeDao();
        return dao.insertOrReplace(ProductAttribute);
    }

    /**创建商品属性信息
     * @param ProductAttribute
     * @return long:返回插入或修改的id
     */
    public long insertProductAttribute(ProductAttribute ProductAttribute){
        ProductAttributeDao dao = openWritableDb().getProductAttributeDao();
        return dao.insert(ProductAttribute);
    }

    /**更新商品属性信息
     * @param ProductAttribute
     * @return long:返回插入或修改的id
     */
    public void updateProductAttribute(ProductAttribute ProductAttribute){
        ProductAttributeDao dao = openWritableDb().getProductAttributeDao();
        dao.update(ProductAttribute);
    }

    /**
     * 删除指定的商品属性
     * @param ProductAttribute
     */
    public void deleteProductAttribute(ProductAttribute ProductAttribute){
        ProductAttributeDao dao = openWritableDb().getProductAttributeDao();
        dao.delete(ProductAttribute);
    }
}
