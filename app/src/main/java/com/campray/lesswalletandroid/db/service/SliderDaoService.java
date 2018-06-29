package com.campray.lesswalletandroid.db.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.campray.lesswalletandroid.db.dao.SliderDao;
import com.campray.lesswalletandroid.db.dao.DaoMaster;
import com.campray.lesswalletandroid.db.dao.DaoSession;
import com.campray.lesswalletandroid.db.entity.Slider;

import java.util.List;

/**
 * 轮播图片类
 * Created by Phills on 5/30/2018.
 */

public class SliderDaoService {
    private DaoMaster.DevOpenHelper openHelper;
    private static SliderDaoService instance;
    public static synchronized SliderDaoService getInstance(Context context) {
        if (instance == null) {
            instance = new SliderDaoService(context);
        }
        return instance;
    }

    private SliderDaoService(Context context){
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

    /**获取本地所有的轮播图片
     * @return
     */
    public List<Slider> getAllSliders(){
        SliderDao dao =openReadableDb().getSliderDao();
        return dao.queryBuilder().list();
    }

    /**
     * 根据id获取轮播图片
     * @param id*
     * @return
     */
    public Slider getSlider(long id){
        SliderDao dao =openReadableDb().getSliderDao();
        return dao.queryBuilder().where(SliderDao.Properties.Id.eq(id)).build().unique();
    }



    /**创建或更新轮播图片信息
     * @param Slider
     * @return long:返回插入或修改的id
     */
    public long insertOrUpdateSlider(Slider Slider){
        SliderDao dao = openWritableDb().getSliderDao();
        return dao.insertOrReplace(Slider);
    }

    /**创建轮播图片信息
     * @param Slider
     * @return long:返回插入或修改的id
     */
    public long insertSlider(Slider Slider){
        SliderDao dao = openWritableDb().getSliderDao();
        return dao.insert(Slider);
    }

    /**更新轮播图片信息
     * @param Slider
     * @return long:返回插入或修改的id
     */
    public void updateSlider(Slider Slider){
        SliderDao dao = openWritableDb().getSliderDao();
        dao.update(Slider);
    }

    /**
     * 删除指定的轮播图片
     * @param Slider
     */
    public void deleteSlider(Slider Slider){
        SliderDao dao = openWritableDb().getSliderDao();
        dao.delete(Slider);
    }
}
