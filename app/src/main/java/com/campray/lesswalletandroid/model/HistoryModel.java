package com.campray.lesswalletandroid.model;

import android.net.Uri;
import android.text.TextUtils;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.db.entity.History;
import com.campray.lesswalletandroid.db.entity.Language;
import com.campray.lesswalletandroid.db.entity.History;
import com.campray.lesswalletandroid.db.entity.User;
import com.campray.lesswalletandroid.db.service.HistoryDaoService;
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
 * 日志数据
 * @author :Phills
 * @project:UserModel
 * @date :2018-05-18-18:09
 */
public class HistoryModel extends BaseModel {

    private static HistoryModel ourInstance = new HistoryModel();

    public static HistoryModel getInstance() {
        return ourInstance;
    }

    private HistoryModel() {
    }

    /**
     * 从服务端查找用户的日志记录
     *
     * @param listener
     */
    public void getAllHistorysFromServer(final OperationListener<List<History>> listener) {
        this.httpPostAPI(HistoryModel.URL_API_GETALLHISTORIES, null,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                List<History> list=new ArrayList<History>();
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                for (JsonElement jsonEle:jArr) {
                                    Gson gson = new Gson();
                                    final History History = gson.fromJson(jsonEle, History.class);
                                    HistoryDaoService.getInstance(getContext()).insertOrUpdateHistory(History);
                                    list.add(History);
                                }
                                listener.done(list, null);
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
     * 从服务端获取用户所有未读日志数据
     * @param listener
     */
    public List<History> getAllUnreadedHistories(final OperationListener<List<History>> listener) {
        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("isvendor",false);
        this.httpPostAPI(HistoryModel.URL_API_MSGSYNC, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                List<History> hList=new ArrayList<History>();
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                for (JsonElement jsonEle:jArr) {
                                    Gson gson = new Gson();
                                    final History History = gson.fromJson(jsonEle, History.class);
                                    HistoryDaoService.getInstance(getContext()).insertOrUpdateHistory(History);
                                    hList.add(History);
                                }
                                listener.done(hList, null);
                            }
                            int points=obj.get("ExtraData").getAsInt();

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

    public History getHistoryById(long id) {
        try {
            return HistoryDaoService.getInstance(getContext()).getHistory(id);
        }
        catch (Exception e){ }
        return null;
    }


    /**
     * 删除记录
     * @param id
     * @param listener
     */
    public void delHistory(final long id,final OperationListener<History> listener) {
        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("ids",id+"");
        this.httpPostAPI(HistoryModel.URL_API_DEL_HISTORIES, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            HistoryDaoService.getInstance(getContext()).deleteHistory(id);
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
     * 查询所有日志记录
     */
    public List<History> getAllHistories() {
        try {
            return HistoryDaoService.getInstance(getContext()).getAllHistorys();
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 查询所有日志记录
     * @param pageNum         当前页数
     * @param pageSize        每页显示数
     * @return                信息列表
     */
    public List<History> getAllHistories(int pageNum,int pageSize) {
        try {
            return HistoryDaoService.getInstance(getContext()).getAllHistorys(pageNum,pageSize);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 查询所有未读日志记录
     */
    public List<History> getUnread() {
        try {
            return HistoryDaoService.getInstance(getContext()).getUnreadCount();
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 查询所有未读日志记录的数量
     */
    public int getUnreadCount() {
        try {
            return HistoryDaoService.getInstance(getContext()).getUnreadCount().size();
        }
        catch (Exception e){ }
        return 0;
    }

    /**
     * 添加日志记录
     */
    public long addHistories(History history) {
        try {
            return HistoryDaoService.getInstance(getContext()).insertOrUpdateHistory(history);
        }
        catch (Exception e){ }
        return 0;
    }

    /**
     * 刪除日志记录
     */
    public boolean updateHistory(History history) {
        try {
            HistoryDaoService.getInstance(getContext()).updateHistory(history);
            return true;
        }
        catch (Exception e){ }
        return false;
    }

    /**
     * 刪除日志记录
     */
    public boolean delHistory(History history) {
        try {
            HistoryDaoService.getInstance(getContext()).deleteHistory(history);
            return true;
        }
        catch (Exception e){ }
        return false;
    }

    /**
     * 刪除日志记录
     */
    public boolean delHistory(long id) {
        try {
            HistoryDaoService.getInstance(getContext()).deleteHistory(id);
            return true;
        }
        catch (Exception e){ }
        return false;
    }

}
