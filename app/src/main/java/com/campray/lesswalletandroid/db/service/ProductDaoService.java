package com.campray.lesswalletandroid.db.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.db.dao.CountryDao;
import com.campray.lesswalletandroid.db.dao.CouponDao;
import com.campray.lesswalletandroid.db.dao.ProductDao;
import com.campray.lesswalletandroid.db.dao.DaoMaster;
import com.campray.lesswalletandroid.db.dao.DaoSession;
import com.campray.lesswalletandroid.db.dao.UserDao;
import com.campray.lesswalletandroid.db.entity.Product;
import com.campray.lesswalletandroid.db.entity.Product;
import com.campray.lesswalletandroid.db.entity.User;

import org.greenrobot.greendao.query.Query;

import java.util.List;

/**
 * 卡卷DAO业务处理服务类
 * Created by Phills on 10/30/2017.
 */

public class ProductDaoService {
    private DaoMaster.DevOpenHelper openHelper;
    private static ProductDaoService instance;
    public static synchronized ProductDaoService getInstance(Context context) {
        if (instance == null) {
            User user=LessWalletApplication.INSTANCE().getAccount();
            long userId=0;
            if(user!=null){
                userId=user.getId();
            }
            instance = new ProductDaoService(context,userId);
        }
        return instance;
    }

    /**
     * 用当前登录用户的ID创建数据库，这样数据库中的数据就只是当前用户，方便关联表查询时需要判断用户ID的条件
     * @param context
     * @param userId
     */
    private ProductDaoService(Context context,long userId){
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
    
    /**
     * 根据id获取卡卷商品数据
     * @param id*
     * @return
     */
    public Product getProduct(long id){
        ProductDao dao =openReadableDb().getProductDao();
        return dao.queryBuilder().where(ProductDao.Properties.ProductId.eq(id)).build().unique();
    }

    /**
     * 查询指定id的卡卷商品是否存在
     * @param id*
     * @return boolean
     */
    public boolean hasProduct(long id){
        ProductDao dao =openReadableDb().getProductDao();
        return dao.queryBuilder().where(ProductDao.Properties.ProductId.eq(id)).build().unique()!=null;
    }

    /**
     * 根据类型查询所有的卡卷商品对象
     * @param typeId*
     * @return
     */
    public List<Product> getAllProductByType(int typeId){
        ProductDao dao =openReadableDb().getProductDao();
        return dao.queryBuilder().where(ProductDao.Properties.ProductTypeId.eq(typeId)).build().list();
    }

    /**
     * 根据类型查询所有的卡卷商品对象
     * @param typeId*
     * @return
     */
    public List<Product> getPageProductByType(int typeId,int pageNum,int pageSize){
        ProductDao dao =openReadableDb().getProductDao();
        return dao.queryBuilder().where(ProductDao.Properties.ProductTypeId.eq(typeId)).offset((pageNum-1)*pageSize).limit(pageSize).build().list();
    }


    /**
     * 根据商家ID查询所有的卡卷商品对象
     * @param merchantId*
     * @return
     */
    public List<Product> getAllProductByMerchant(long merchantId){
        ProductDao dao =openReadableDb().getProductDao();
        return dao.queryBuilder().where(ProductDao.Properties.MerchantId.eq(merchantId)).build().list();
    }

    /**创建或更新卡卷商品信息
     * @param Product
     * @return long:返回插入或修改的id
     */
    public long insertOrUpdateProduct(Product Product){
        ProductDao dao = openWritableDb().getProductDao();
        return dao.insertOrReplace(Product);
    }

    /**创建卡卷商品信息
     * @param Product
     * @return long:返回插入或修改的id
     */
    public long insertProduct(Product Product){
        ProductDao dao = openWritableDb().getProductDao();
        return dao.insert(Product);
    }

    /**更新卡卷商品信息
     * @param Product
     * @return long:返回插入或修改的id
     */
    public void updateProduct(Product Product){
        ProductDao dao = openWritableDb().getProductDao();
        dao.update(Product);
    }

    /**
     * 删除指定的卡卷商品
     * @param Product
     */
    public void deleteProduct(Product Product){
        ProductDao dao = openWritableDb().getProductDao();
        dao.delete(Product);
    }

    /**
     * 删除指定的卡卷商品
     * @param ProductId
     */
    public void deleteProduct(long ProductId){
        ProductDao dao = openWritableDb().getProductDao();
        dao.deleteByKey(ProductId);
    }
}
