package com.campray.lesswalletandroid.model;

import android.net.Uri;
import android.text.TextUtils;

import com.campray.lesswalletandroid.db.entity.Friend;
import com.campray.lesswalletandroid.db.entity.Language;
import com.campray.lesswalletandroid.db.service.FriendDaoService;
import com.campray.lesswalletandroid.db.service.LanguageDaoService;
import com.campray.lesswalletandroid.listener.ApiHandleListener;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.ImageUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 好友数据
 * @author :Phills
 * @project:UserModel
 * @date :2017-08-22-18:09
 */
public class FriendModel extends BaseModel {

    private static FriendModel ourInstance = new FriendModel();

    public static FriendModel getInstance() {
        return ourInstance;
    }

    private FriendModel() {
    }

    /**
     * 根据搜索字符从服务端查找用户名或手机号符合的用户
     *
     * @param searchStr
     * @param listener
     */
    public void searchUser(String searchStr, final OperationListener<Friend> listener) {
        if (TextUtils.isEmpty(searchStr)) {
            listener.done(null, new AppException("E_2011"));
            return;
        }

        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("device",this.getDeviceId());
        jObj.addProperty("search",searchStr);
        this.httpPostAPI(FriendModel.URL_API_SEARCH_USER, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                Gson gson = new Gson();
                                Friend friend = gson.fromJson(jArr.get(0), Friend.class);
                                Locale locale = getContext().getResources().getConfiguration().locale;
                                String languageCode = locale.getLanguage();
                                Language language=LanguageDaoService.getInstance(getContext()).getLanguageByCode(languageCode);
                                friend.setLanguage(language);
                                listener.done(friend, null);
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
     * 从服务端获取用户所有好友数据
     * @param listener
     */
    public List<Friend> getAllFriends(final OperationListener<List<Friend>> listener) {
        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("device",this.getDeviceId());
        this.httpPostAPI(FriendModel.URL_API_GET_FRIENDS, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                List<Friend> fList=new ArrayList<Friend>();
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                for (JsonElement jsonEle:jArr) {
                                    Gson gson = new Gson();
                                    final Friend friend = gson.fromJson(jsonEle, Friend.class);
                                    Locale locale = getContext().getResources().getConfiguration().locale;
                                    String languageCode = locale.getLanguage();
                                    Language language=LanguageDaoService.getInstance(getContext()).getLanguageByCode(languageCode);
                                    friend.setLanguage(language);
                                    FriendDaoService.getInstance(getContext()).insertOrUpdateFriend(friend);
                                    fList.add(friend);
                                    //保存图片到本地
                                    final String avatarUrl = friend.getAvatarUrl();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Uri uri = ImageUtil.saveImageToUri(avatarUrl);
                                            if (uri != null) {
                                                friend.setAvatorPath(uri.toString());
                                                FriendDaoService.getInstance(getContext()).insertOrUpdateFriend(friend);
                                            }
                                        }
                                    }).start();
                                }
                                listener.done(fList, null);
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
        return null;
    }

    public Friend getFriendById(long id) {
        try {
            return FriendDaoService.getInstance(getContext()).getFriend(id);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 根据名称查询本地数据库的好友
     * @param username
     */
    public Friend getFriendByName(String username) {
        try {
            return FriendDaoService.getInstance(getContext()).getFriendByName(username);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 根据名称或电话查询本地数据库的好友
     * @param searchStr
     */
    public Friend searchFriend(String searchStr) {
        try {
            return FriendDaoService.getInstance(getContext()).getFriendBySearch(searchStr);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 添加好友
     * @param username
     * @param listener
     */
    public void addFriend(String username,final OperationListener<Friend> listener) {
        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("device",this.getDeviceId());
        jObj.addProperty("username",username);
        this.httpPostAPI(FriendModel.URL_API_ADD_FRIEND, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                Gson gson = new Gson();
                                final Friend friend = gson.fromJson(jArr.get(0), Friend.class);
                                if(friend!=null) {
                                    Friend savedFriend=FriendDaoService.getInstance(getContext()).getFriendByName(friend.getUserName());
                                    if(savedFriend==null) {//如果本地数据库还没有添加为好友，则保存
                                        FriendDaoService.getInstance(getContext()).insertOrUpdateFriend(friend);
                                        //保存图片到本地
                                        final String avatarUrl = friend.getAvatarUrl();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Uri uri = ImageUtil.saveImageToUri(avatarUrl);
                                                if (uri != null) {
                                                    friend.setAvatorPath(uri.toString());
                                                    FriendDaoService.getInstance(getContext()).insertOrUpdateFriend(friend);
                                                }
                                            }
                                        }).start();
                                        listener.done(savedFriend, null);
                                    }
                                    else {
                                        listener.done(friend, null);
                                    }
                                }
                                else {
                                    listener.done(null, new AppException("E_1004"));
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
     * 删除好友
     * @param username
     * @param listener
     */
    public void delFriend(String username,final OperationListener<Friend> listener) {
        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("device",this.getDeviceId());
        jObj.addProperty("username",username);
        this.httpPostAPI(FriendModel.URL_API_DEL_FRIEND, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
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



}
