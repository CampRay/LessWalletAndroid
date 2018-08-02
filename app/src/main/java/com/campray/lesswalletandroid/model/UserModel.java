package com.campray.lesswalletandroid.model;

import android.text.TextUtils;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.db.entity.Currency;
import com.campray.lesswalletandroid.db.entity.Language;
import com.campray.lesswalletandroid.db.entity.User;

import com.campray.lesswalletandroid.db.service.CurrencyDaoService;
import com.campray.lesswalletandroid.db.service.LanguageDaoService;
import com.campray.lesswalletandroid.db.service.UserDaoService;
import com.campray.lesswalletandroid.listener.ApiHandleListener;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.EncryptionUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Locale;

/**
 * @author :Phills
 * @project:UserModel
 * @date :2017-08-22-18:09
 */
public class UserModel extends BaseModel {

    private static UserModel ourInstance = new UserModel();

    public static UserModel getInstance() {
        return ourInstance;
    }

    private UserModel() {
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @param listener
     */
    public void login(final String username, final String password, final boolean remember, final OperationListener<User> listener) {
        if (TextUtils.isEmpty(username)) {
            listener.done(null, new AppException("E_2005"));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            listener.done(null, new AppException("E_2007"));
            return;
        }
        //如果用户在本机数据库存在
        User localUser=UserDaoService.getInstance(getContext()).getUserByNameOrEmail(username);
        if(localUser!=null){
            if(EncryptionUtil.getHash2(password,"MD5").equals(localUser.getPassword())) {
                listener.done(localUser, null);
            }
            else{
                listener.done(null, new AppException("E_2000"));
            }
            return;
        }
        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("device",this.getDeviceId());
        jObj.addProperty("uname",username);
        jObj.addProperty("pwd",password);
        this.httpPostAPI(UserModel.URL_API_LOGIN, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                if (jArr.size() > 0) {
                                    Gson gson = new Gson();
                                    User user = gson.fromJson(jArr.get(0), User.class);
                                    user.setPassword(EncryptionUtil.getHash2(password,"MD5"));
                                    user.setRemember(remember);
                                    Locale locale = getContext().getResources().getConfiguration().locale;
                                    String languageCode =locale.getLanguage() ;
                                    Language language=LanguageDaoService.getInstance(getContext()).getLanguageByCode(languageCode);
                                    user.setLanguage(language);
                                    Currency currency= CurrencyDaoService.getInstance(getContext()).getDefaultCurrency();
//                                    if(language!=null) {
//                                        currency = language.getCurrency();
//                                    }
                                    user.setCurrenty(currency);
                                    try {
                                        UserDaoService.getInstance(getContext()).insertOrUpdateUser(user);
                                    }
                                    catch (Exception exe){
                                        listener.done(null, new AppException("E_1005"));
                                    }
                                    listener.done(user, null);
                                } else {
                                    listener.done(null, new AppException("E_2000"));
                                }
                            }

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
     * 新用户注册
     * @param username
     * @param email
     * @param password
     * @param listener
     */
    public void register(String username,String email, final String password,String firstname,String lastname,String address,String countryCode, final OperationListener<User> listener) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("device", this.getDeviceId());
        jObj.addProperty("uname", username);
        jObj.addProperty("email", email);
        jObj.addProperty("pwd", password);
        jObj.addProperty("firstname", firstname);
        jObj.addProperty("lastname", lastname);
        jObj.addProperty("address", address);
        jObj.addProperty("countrycode", countryCode);
        this.httpPostAPI(UserModel.URL_API_REGISTER, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                if (jArr.size() > 0) {
                                    Gson gson = new Gson();
                                    User user = gson.fromJson(jArr.get(0), User.class);
                                    user.setPassword(EncryptionUtil.getHash2(password,"MD5"));
                                    Locale locale = getContext().getResources().getConfiguration().locale;
                                    String languageCode = locale.getLanguage();
                                    Language language=LanguageDaoService.getInstance(getContext()).getLanguageByCode(languageCode);
                                    user.setLanguage(language);
                                    Currency currency=language.getCurrency();
                                    if(currency==null) {
                                        currency = CurrencyDaoService.getInstance(getContext()).getDefaultCurrency();
                                    }
                                    user.setCurrenty(currency);
                                    LessWalletApplication.INSTANCE().setAccount(user);
                                    long row = UserDaoService.getInstance(getContext()).insertOrUpdateUser(user);
                                    listener.done(user, null);
                                } else {
                                    listener.done(null, new AppException("E_1004"));
                                }
                            }

                        } else {
                            listener.done(null, new AppException("E_2010",obj.get("Errors").getAsString()));
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
     * 根据ID从服务端查找用户
     *  @param id
     * @param listener
     */
    public void searchUserById(long id, final OperationListener<User> listener) {
        if (id==0) {
            listener.done(null, new AppException("E_2011"));
        }

        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("device",this.getDeviceId());
        jObj.addProperty("id",id);
        this.httpPostAPI(UserModel.URL_API_GET_USER_BYID, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                Gson gson = new Gson();
                                User user = gson.fromJson(jArr.get(0), User.class);
                                listener.done(user, null);
                            }

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
     * 从服务端同步用户信息
     *  @param id
     * @param listener
     */
    public void updateUserFormServer(long id, final OperationListener<User> listener) {
        this.searchUserById(id, new OperationListener<User>() {
            @Override
            public void done(User obj, AppException exception) {
                if(obj!=null) {
                    try {
                        User user=UserDaoService.getInstance(getContext()).getUserById(obj.getId());
                        user.setEmail(obj.getEmail());
                        user.setMobile(obj.getMobile());
                        user.setAddress(obj.getAddress());
                        user.setFirstName(obj.getFirstName());
                        user.setLastName(obj.getLastName());
                        user.setAvatarUrl(obj.getAvatarUrl());
                        user.setStorePoints(obj.getStorePoints());
                        user.setStoreCash(obj.getStoreCash());
                        UserDaoService.getInstance(getContext()).insertOrUpdateUser(user);
                        LessWalletApplication.INSTANCE().setAccount(user);
                        listener.done(user, null);
                    } catch (Exception e) {
                        listener.done(null, new AppException("E_1000",e));
                    }
                }
                else{
                    listener.done(null, exception);
                }
            }
        });
    }

    /**
     * 退出登录
     */
    public void logout() {
        LessWalletApplication.INSTANCE().setAccount(null);
    }

    public User getUserById(long userId) {
        try {
           return UserDaoService.getInstance(getContext()).getUserById(userId);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 从服务端查找Paypal Braintree支付的Client Token
     * @param listener
     */
    public void getPaypalClientToken(final OperationListener<String> listener) {
        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("device",this.getDeviceId());
        this.httpPostAPI(UserModel.URL_API_GETCLIENTTOKEN, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors")==null) {
                            String clientToken=obj.get("Data").getAsString();
                            listener.done(clientToken,null);
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


}
