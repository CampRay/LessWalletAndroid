package com.campray.lesswalletandroid.db.converter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.HashMap;

/**
 * 转换器类：把storePoints属性的对象转换成Json字符串形式，以便保存到数据库中
 * Created by Phills on 11/29/2017.
 */

public class StorePointConverter implements PropertyConverter<HashMap<Integer,Integer>,String> {

    @Override
    public HashMap<Integer,Integer> convertToEntityProperty(String databaseValue) {
        if(TextUtils.isEmpty(databaseValue)) {
            return null;
        }
        else{
            Gson gson=new Gson();
            HashMap<Integer,Integer> pointMap = gson.fromJson(databaseValue, new TypeToken<HashMap<Integer,Integer>>() {}.getType());
            return pointMap;
        }
    }

    @Override
    public String convertToDatabaseValue(HashMap<Integer,Integer> entityProperty) {
        if(entityProperty==null||entityProperty.size()==0) {
            return "";
        }
        else{
            Gson gson=new Gson();
            return gson.toJson(entityProperty);
        }
    }
}
