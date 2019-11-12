package com.campray.lesswalletandroid.db.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.db.dao.CouponDao;
import com.campray.lesswalletandroid.db.dao.DaoMaster;
import com.campray.lesswalletandroid.db.dao.DaoSession;
import com.campray.lesswalletandroid.db.dao.ProductDao;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.db.entity.Merchant;
import com.campray.lesswalletandroid.db.entity.Product;
import com.campray.lesswalletandroid.db.entity.User;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * 卡卷DAO业务处理服务类
 * Created by Phills on 10/30/2017.
 */

public class CouponDaoService {
    private DaoMaster.DevOpenHelper openHelper;
    private static CouponDaoService instance;
    public static synchronized CouponDaoService getInstance(Context context) {
        if (instance == null) {
            User user= LessWalletApplication.INSTANCE().getAccount();
            long userId=0;
            if(user!=null){
                userId=user.getId();
            }
            instance = new CouponDaoService(context,userId);
        }
        return instance;
    }

    private CouponDaoService(Context context,long userId){
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

    /**获取所有的卡卷
     * @return
     */
    public List<Coupon> getAllCoupons(){
        CouponDao dao =openReadableDb().getCouponDao();
        return dao.queryBuilder().list();
    }

    /**
     * 根据id获取卡卷数据
     * @param id*
     * @return
     */
    public Coupon getCoupon(long id){
        CouponDao dao =openReadableDb().getCouponDao();
        return dao.queryBuilder().where(CouponDao.Properties.OrderId.eq(id)).build().unique();
    }

    /**
     * 根据类型查询所有的卡卷对象
     * @param typeId*
     * @return
     */
    public List<Coupon> getAllCouponByType(int typeId){
        CouponDao dao =openReadableDb().getCouponDao();
        ProductDao productDao=openReadableDb().getProductDao();
        List<Product> productList=productDao.queryBuilder().where(ProductDao.Properties.ProductTypeId.eq(typeId)).build().list();
        List<Long> idList=new ArrayList<Long>();
        if(productList!=null) {
            for (Product product : productList) {
                idList.add(product.getProductId());
            }
        }
        return dao.queryBuilder().where(CouponDao.Properties.ProductId.in(idList)).orderDesc(CouponDao.Properties.OrderId).build().list();
    }

    /**
     * 根据类型查询所有的分页卡卷对象
     * @param typeId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<Coupon> getPageCouponByType(int typeId,int pageNum,int pageSize){
//        ProductDao productDao=openReadableDb().getProductDao();
//        List<Product> productList=productDao.queryBuilder().where(ProductDao.Properties.ProductTypeId.eq(typeId)).build().listLazy();
//        List<Long> idList=new ArrayList<Long>();
//        if(productList!=null) {
//            for (Product product : productList) {
//                idList.add(product.getProductId());
//            }
//        }
//        CouponDao dao =openReadableDb().getCouponDao();
//        return dao.queryBuilder().where(CouponDao.Properties.ProductId.in(idList)).orderDesc(CouponDao.Properties.OrderId).offset((pageNum-1)*pageSize).limit(pageSize).build().list();

        CouponDao dao =openReadableDb().getCouponDao();
        return dao.queryBuilder().where(new WhereCondition.StringCondition("T.PRODUCT_ID IN " +"(SELECT P._id FROM PRODUCT P WHERE P._id = T.PRODUCT_ID and P.PRODUCT_TYPE_ID="+typeId+")"))
                .offset((pageNum-1)*pageSize).limit(pageSize).build().list();
        //return dao.queryDeep("T0.PRODUCT_TYPE_ID=? limit 5 offset 0",new String[]{typeId+"",pageSize+"",((pageNum-1)*pageSize)+""});
    }


    /**创建或更新卡卷信息
     * @param coupon
     * @return long:返回插入或修改的id
     */
    public long insertOrUpdateCoupon(Coupon coupon){
        CouponDao dao = openWritableDb().getCouponDao();
        return dao.insertOrReplace(coupon);
    }

    /**创建卡卷信息
     * @param coupon
     * @return long:返回插入或修改的id
     */
    public long insertCoupon(Coupon coupon){
        CouponDao dao = openWritableDb().getCouponDao();
        return dao.insert(coupon);
    }

    /**更新卡卷信息
     * @param coupon
     * @return long:返回插入或修改的id
     */
    public void updateCoupon(Coupon coupon){
        CouponDao dao = openWritableDb().getCouponDao();
        dao.update(coupon);
    }

    /**
     * 删除指定的卡卷
     * @param coupon
     */
    public void deleteCoupon(Coupon coupon){
        CouponDao dao = openWritableDb().getCouponDao();
        dao.delete(coupon);
    }

    /**
     * 删除指定的卡卷
     * @param couponId
     */
    public void deleteCoupon(long couponId){
        CouponDao dao = openWritableDb().getCouponDao();
        dao.deleteByKey(couponId);
    }
}
