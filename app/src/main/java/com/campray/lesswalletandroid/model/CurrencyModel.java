package com.campray.lesswalletandroid.model;

import com.campray.lesswalletandroid.db.entity.Currency;
import com.campray.lesswalletandroid.db.service.CurrencyDaoService;
import com.campray.lesswalletandroid.listener.ApiHandleListener;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.util.AppException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * 货币信息对象业务处理模型
 * @author :Phills
 * @project:CurrencyModel
 * @date :2017-08-22-18:09
 */
public class CurrencyModel extends BaseModel {

    private static CurrencyModel ourInstance = new CurrencyModel();

    public static CurrencyModel getInstance() {
        return ourInstance;
    }

    private CurrencyModel() {
    }

    /**
     * 从服务器获取所有国家信息数据
     * @param listener
     */
    public void getAllCurrenciesFromServer(final OperationListener<List<Currency>> listener) {
        this.httpPostAPI(CurrencyModel.URL_API_GETALLCURRENCIES, null,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                Gson gson = new Gson();
                                List<Currency> currencyList = gson.fromJson(jArr, new TypeToken<List<Currency>>() {}.getType());
                                for(Currency currency:currencyList){
//                                for(JsonElement item:jArr){
//                                    Currency currency = gson.fromJson(item, Currency.class);
                                    try {
                                        long row = CurrencyDaoService.getInstance(getContext()).insertOrUpdateCurrency(currency);
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
     * 查询所有货币数据对象
     * @return
     */
    public List<Currency> getAllCurrencies() {
        try {
            return CurrencyDaoService.getInstance(getContext()).getAllCurrencies();
        }
        catch (Exception e){ }
        return null;
    }


    /**
     * 根据记录ID查询货币数据对象
     * @param currencyId
     * @return
     */
    public Currency getCurrencyById(int currencyId) {
        try {
            return CurrencyDaoService.getInstance(getContext()).getCurrency(currencyId);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 根据货币名称查询货币数据对象
     * @param name
     * @return
     */
    public Currency getCurrencyByName(String name) {
        try {
            return CurrencyDaoService.getInstance(getContext()).getCurrencyByName(name);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 根据货币Code查询货币数据对象
     * @param currencyCode
     * @return
     */
    public Currency getCurrencyByCode(String currencyCode) {
        try {
            return CurrencyDaoService.getInstance(getContext()).getCurrencyByCode(currencyCode);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 查询默认主货币对象
     * @return
     */
    public Currency getDefaultCurrency(){
        return CurrencyDaoService.getInstance(getContext()).getDefaultCurrency();
    }

}
