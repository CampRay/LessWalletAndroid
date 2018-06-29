package com.campray.lesswalletandroid.model;

import com.campray.lesswalletandroid.db.entity.Merchant;
import com.campray.lesswalletandroid.db.service.MerchantDaoService;
import com.campray.lesswalletandroid.listener.ApiHandleListener;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.util.AppException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * 商家对象业务处理模型
 * @author :Phills
 * @project:MerchantModel
 * @date :2017-08-22-18:09
 */
public class MerchantModel extends BaseModel {

    private static MerchantModel ourInstance = new MerchantModel();

    public static MerchantModel getInstance() {
        return ourInstance;
    }

    private MerchantModel() {
    }

    private void saveMerchant(){

    }

    /**
     * 根据商家ID从服务器获取所有国家信息数据
     * @param min
     * @param listener
     */
    public void getMerchantFromServer(final long min,final OperationListener<Merchant> listener) {
        //封装请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("id",min);
        jObj.addProperty("device",this.getDeviceId());
        this.httpPostAPI(MerchantModel.URL_API_GETMERCHANT, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                Gson gson = new Gson();
                                List<Merchant> MerchantList = gson.fromJson(jArr, new TypeToken<List<Merchant>>() {}.getType());
                                for(Merchant merchant:MerchantList){
                                    try {
                                        //保存数据
                                        long row = MerchantDaoService.getInstance(getContext()).insertOrUpdateMerchant(merchant);
                                        listener.done(merchant, null);
                                    }
                                    catch (Exception exe){
                                        listener.done(null, new AppException("E_1005"));
                                        break;
                                    }
                                }
                            }
                            listener.done(null, null);
                        } else {
                            listener.done(null, new AppException(obj.get("Errors").getAsString()));
                        }
                    }
                    catch (Exception e){
                        listener.done(null, new AppException("E_1004"));
                    }
                } else {
                    listener.done(null, exception);
                }
            }
        });
    }

    /**
     * 查询所有商家数据对象
     * @return
     */
    public List<Merchant> getAllMerchants() {
        try {
            return MerchantDaoService.getInstance(getContext()).getAllMerchants();
        }
        catch (Exception e){ }
        return null;
    }


    /**
     * 根据记录ID查询商家数据对象
     * @param MerchantId
     * @return
     */
    public Merchant getMerchantById(int MerchantId) {
        try {
            return MerchantDaoService.getInstance(getContext()).getMerchant(MerchantId);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 根据商家名称查询商家数据对象
     * @param name
     * @return
     */
    public Merchant getMerchantByName(String name) {
        try {
            return MerchantDaoService.getInstance(getContext()).getMerchantByName(name);
        }
        catch (Exception e){ }
        return null;
    }

}
