package com.campray.lesswalletandroid.db.converter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.HashMap;

/**
 * 转换器类：把storeCash属性的对象转换成Json字符串形式，以便保存到数据库中
 * Created by Phills on 11/29/2017.
 */

public class StoreCashConverter implements PropertyConverter<HashMap<Integer,Float>,String> {

    @Override
    public HashMap<Integer,Float> convertToEntityProperty(String databaseValue) {
        if(TextUtils.isEmpty(databaseValue)) {
            return null;
        }
        else{
            Gson gson=new Gson();
            HashMap<Integer,Float> cashMap = gson.fromJson(databaseValue, new TypeToken<HashMap<Integer,Float>>() {}.getType());
            return cashMap;
        }
    }

    @Override
    public String convertToDatabaseValue(HashMap<Integer,Float> entityProperty) {
        if(entityProperty==null||entityProperty.size()==0) {
            return "";
        }
        else{
            Gson gson=new Gson();
            return gson.toJson(entityProperty);
        }
    }
}
