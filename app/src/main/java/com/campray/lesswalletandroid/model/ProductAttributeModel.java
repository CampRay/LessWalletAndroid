package com.campray.lesswalletandroid.model;

import com.campray.lesswalletandroid.db.entity.ProductAttribute;
import com.campray.lesswalletandroid.db.entity.ProductAttribute;
import com.campray.lesswalletandroid.db.service.ProductAttributeDaoService;
import com.campray.lesswalletandroid.db.service.ProductAttributeDaoService;
import com.campray.lesswalletandroid.listener.ApiHandleListener;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.util.AppException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * 商品属性表对象业务处理模型
 * @author :Phills
 * @project:ProductAttributeModel
 * @date :2017-08-22-18:09
 */
public class ProductAttributeModel extends BaseModel {

    private static ProductAttributeModel ourInstance = new ProductAttributeModel();

    public static ProductAttributeModel getInstance() {
        return ourInstance;
    }

    private ProductAttributeModel() {
    }

    /**
     * 从服务器获取所有商品属性表信息数据
     * @param listener
     */
    public void getAllProdAttrFromServer(final OperationListener<List<ProductAttribute>> listener) {
        this.httpPostAPI(ProductAttributeModel.URL_API_GETALLUSERATTRS, null,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                Gson gson = new Gson();
                                List<ProductAttribute> productAttributeList = gson.fromJson(jArr, new TypeToken<List<ProductAttribute>>() {}.getType());
                                for(ProductAttribute productAttribute:productAttributeList){
                                    try {
                                        long row = ProductAttributeDaoService.getInstance(getContext()).insertOrUpdateProductAttribute(productAttribute);
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
     * 查询所有商品属性对象
     * @return
     */
    public List<ProductAttribute> getAllProdAttrs() {
        try {
            List<ProductAttribute> ProductAttributes = ProductAttributeDaoService.getInstance(getContext()).getAllProdAttrs();
            return ProductAttributes;
        }
        catch (Exception e){ }
        return null;
    }


    /**
     * 根据记录ID查询商品属性数据对象
     * @param id
     * @return
     */
    public ProductAttribute getProductAttributeById(int id) {
        try {
            return ProductAttributeDaoService.getInstance(getContext()).getProductAttribute(id);
        }
        catch (Exception e){ }
        return null;
    }

}
