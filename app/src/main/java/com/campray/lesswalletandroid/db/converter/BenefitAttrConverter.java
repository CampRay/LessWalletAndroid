package com.campray.lesswalletandroid.db.converter;

import android.text.TextUtils;

import com.campray.lesswalletandroid.db.entity.SpecAttr;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.HashMap;
import java.util.List;

/**
 * 转换器类：把规格属性的Benefit对象转换成Json字符串形式，以便保存到数据库中
 * Created by Phills on 11/29/2017.
 */

public class BenefitAttrConverter implements PropertyConverter<HashMap<String,String>,String> {

    @Override
    public HashMap<String,String> convertToEntityProperty(String databaseValue) {
        if(TextUtils.isEmpty(databaseValue)) {
            return null;
        }
        else{
            Gson gson=new Gson();
            HashMap<String,String> benefitMap = gson.fromJson(databaseValue, new TypeToken<HashMap<String,String>>() {}.getType());
            return benefitMap;
        }
    }

    @Override
    public String convertToDatabaseValue(HashMap<String,String> entityProperty) {
        if(entityProperty==null||entityProperty.size()==0) {
            return "";
        }
        else{
            Gson gson=new Gson();
            return gson.toJson(entityProperty);
        }
    }
}
