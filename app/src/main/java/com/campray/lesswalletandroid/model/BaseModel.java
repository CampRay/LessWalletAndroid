package com.campray.lesswalletandroid.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.db.entity.User;
import com.campray.lesswalletandroid.listener.ApiHandleListener;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.DeviceUuidFactory;
import com.campray.lesswalletandroid.util.NetWorkUtils;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.concurrent.Future;

import static com.campray.lesswalletandroid.LessWalletApplication.INSTANCE;

/**
 * 业务模型基类（业务模型主要处理所有数据Bean的业务逻辑）
 * @author :Phills
 * @project:BaseModel
 * @date :2017-08-23-10:37
 */
public abstract class BaseModel {
    public String deviceId=null;
//    public static final String HOST="http://192.168.1.4:15536";
    public static final String HOST="http://192.168.1.4:8096";
//    public static final String HOST="http://47.106.94.90:8888";
    public static final String URL_API_LOGIN=HOST+"/Plugins/API/Login";
    public static final String URL_API_REGISTER=HOST+"/Plugins/API/Register";
    public static final String URL_API_GETPRODUCT=HOST+"/Plugins/API/GetProduct";
    public static final String URL_API_GET_PRICE_STOCK=HOST+"/Plugins/API/GetProductPriceAndStock";
    public static final String URL_API_EDIT_USER=HOST+"Plugins/API/EditUser";

    public static final String URL_API_CREATEORDER=HOST+"/Plugins/API/CreateOrder";
    public static final String URL_API_GETCOUPON=HOST+"/Plugins/API/GetCoupon";
    public static final String URL_API_GETALLCOUPONS=HOST+"/Plugins/API/GetAllCoupons";
    public static final String URL_API_DELCOUPONS=HOST+"/Plugins/API/DeleteCoupons";
    public static final String URL_API_RESTORE_COUPONS=HOST+"/Plugins/API/RestoreCoupons";

    public static final String URL_API_GETALLCOUNTRIES=HOST+"/Plugins/API/GetAllCountries";
    public static final String URL_API_GETALLLANGUAGES=HOST+"/Plugins/API/GetAllLanguages";
    public static final String URL_API_GETALLCURRENCIES=HOST+"/Plugins/API/GetAllCurrencyes";
    public static final String URL_API_GETALLUSERATTRS=HOST+"/Plugins/API/GetAllUserAttrs";
    public static final String URL_API_GETMERCHANT=HOST+"/Plugins/API/GetMerchant";
    public static final String URL_API_GETALLHISTORIES=HOST+"/Plugins/API/GetAllLogs";

    public static final String URL_API_GET_USER_BYID=HOST+"/Plugins/API/GeUserById";
    public static final String URL_API_SEARCH_USER=HOST+"/Plugins/API/SearchUsers";
    public static final String URL_API_GET_FRIENDS=HOST+"/Plugins/API/GetAllFriends";
    public static final String URL_API_ADD_FRIEND=HOST+"/Plugins/API/AddFriend";
    public static final String URL_API_DEL_FRIEND=HOST+"/Plugins/API/DelFriend";
    public static final String URL_API_SENT_COUPON=HOST+"/Plugins/API/SentCoupon";

    public static final String URL_API_GET_ALL_ATTENTION=HOST+"/Plugins/API/GetAllAttention";
    public static final String URL_API_ADD_ATTENTION=HOST+"/Plugins/API/AddAttention";
    public static final String URL_API_DEL_ATTENTION=HOST+"/Plugins/API/DelAttention";


    public static final String URL_API_MSGSYNC=HOST+"/Plugins/API/MsgSync";
    public static final String URL_API_DEL_HISTORIES=HOST+"/Plugins/API/DelLogs";

    public static final String URL_API_GETALLSLIDERS=HOST+"/Plugins/API/GetSliderData";
    public static final String URL_API_GETCLIENTTOKEN=HOST+"/Plugins/PaymentPayPalStandard/GetClientToken";
    public static final String URL_API_PAYPALCOUPON=HOST+"/Plugins/PaymentPayPalStandard/CreateTransaction";

    private Handler apiHandler;
    public Context getContext(){
        return INSTANCE();
    }

    public String getDeviceId(){
        if(TextUtils.isEmpty(deviceId)) {
            deviceId= new DeviceUuidFactory(getContext()).getDeviceUuid().toString();
        }
        return deviceId;
    }

    //带参数的http请求
    public void httpPostAPI(String url, JsonObject json, final ApiHandleListener<JsonObject> apiListener){
        if(!NetWorkUtils.isNetworkConnected(getContext())){
            apiListener.done(null,new AppException("E_1001"));
        }
        else{
            User user= LessWalletApplication.INSTANCE().getAccount();
            String token="";
            if(user!=null){
                token=user.getToken();
            }
            if(json==null) {
                json = new JsonObject();
            }
            Ion.with(getContext()).load(url)
                .addHeader("Authorization",token)
                .addHeader("Device",getDeviceId())
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception exe, JsonObject result) {
                        if (exe == null) {
                            apiListener.done(result, null);
                        } else {
                            apiListener.done(result, new AppException("E_1003", exe));
                        }
                    }
                });
        }
    }

    /**
     * 获取网络http请求的图片（同步方法，不用开启异步线程）
     * @param path
     * @return Drawable
     */
    public Drawable httpRequestImage(String path){
        try {
            Future<Bitmap> bitmapFuture = Ion.with(getContext()).load(path).asBitmap();
            Bitmap bitmap = bitmapFuture.get();
            return new BitmapDrawable(getContext().getResources(),bitmap);
        } catch (Exception e) {}
        return null;
    }

}
