package com.campray.lesswalletandroid.model;

import android.util.Log;

import com.campray.lesswalletandroid.bean.CountryBean;
import com.campray.lesswalletandroid.db.entity.Country;
import com.campray.lesswalletandroid.db.service.CountryDaoService;
import com.campray.lesswalletandroid.listener.ApiHandleListener;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.util.AppException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * 国家信息对象业务处理模型
 * @author :Phills
 * @project:CountryModel
 * @date :2017-08-22-18:09
 */
public class CountryModel extends BaseModel {

    private static CountryModel ourInstance = new CountryModel();

    public static CountryModel getInstance() {
        return ourInstance;
    }

    private CountryModel() {
    }

    /**
     * 从服务器获取所有国家信息数据
     * @param listener
     */
    public void getAllCountriesFromServer(final OperationListener<List<Country>> listener) {
        this.httpPostAPI(CountryModel.URL_API_GETALLCOUNTRIES, null,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                Gson gson = new Gson();
                                List<Country> countryList = gson.fromJson(jArr, new TypeToken<List<Country>>() {}.getType());
                                for(Country country:countryList){
                                    try {
                                        long row = CountryDaoService.getInstance(getContext()).insertOrUpdateCountry(country);
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
     * 查询所有国家数据对象
     * @return
     */
    public List<Country> getAllCountries() {
        try {
            List<Country> countrys = CountryDaoService.getInstance(getContext()).getAllCountries();
            return countrys;
        }
        catch (Exception e){ }
        return null;
    }


    /**
     * 根据记录ID查询国家数据对象
     * @param countryId
     * @return
     */
    public Country getCountryById(int countryId) {
        try {
            return CountryDaoService.getInstance(getContext()).getCountry(countryId);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 根据国家名称查询国家数据对象
     * @param name
     * @return
     */
    public Country getCountryByName(String name) {
        try {
            return CountryDaoService.getInstance(getContext()).getCountryByName(name);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 根据国家Code查询国家数据对象
     * @param code
     * @return
     */
    public Country getCountryByCode(String code) {
        try {
            return  CountryDaoService.getInstance(getContext()).getCountryByCode(code);
        }
        catch (Exception e){ }
        return null;
    }


}
