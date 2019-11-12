package com.campray.lesswalletandroid.model;

import com.campray.lesswalletandroid.db.entity.Merchant;
import com.campray.lesswalletandroid.db.entity.Product;
import com.campray.lesswalletandroid.db.service.MerchantDaoService;
import com.campray.lesswalletandroid.db.service.ProductDaoService;
import com.campray.lesswalletandroid.listener.ApiHandleListener;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.util.AppException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * 卡卷商口对象业务处理模型
 * @author :Phills
 * @project:ProductModel
 * @date :2017-08-22-18:09
 */
public class ProductModel extends BaseModel {

    private static ProductModel ourInstance = new ProductModel();

    public static ProductModel getInstance() {
        return ourInstance;
    }

    private ProductModel() {
    }

    /**
     * 根据商品ID从服务器获取卡卷商品信息数据
     * @param listener
     */
    public void getProductFromServer(long pid,final OperationListener<Product> listener) {
        //封装请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("productId",(int)pid);
        this.httpPostAPI(ProductModel.URL_API_GETPRODUCT, jObj, new ApiHandleListener<JsonObject>() {
            @Override
            public void  done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                Gson gson = new Gson();
                                try {
                                    JsonObject item=jArr.get(0).getAsJsonObject();
                                    Product product = gson.fromJson(item, Product.class);
                                    Merchant merchant=gson.fromJson(item.get("merchant"),Merchant.class);
                                    product.setMerchant(merchant);
                                    if(product.getMerchantId()>0){
                                        //如果商家数据在本地不存在，则保存数据
                                        Merchant dbMerchant=MerchantDaoService.getInstance(getContext()).getMerchant(product.getMerchantId());
                                        if(dbMerchant==null){
                                            MerchantDaoService.getInstance(getContext()).insertOrUpdateMerchant(merchant);
                                        }
                                    }
                                    ProductDaoService.getInstance(getContext()).insertOrUpdateProduct(product);
                                    listener.done(product, null);
                                }
                                catch (Exception exe){
                                    listener.done(null, new AppException("E_1005"));
                                }
                            }
                            else{
                                listener.done(null, null);
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
     * 从服务端查询指定商品的价格和库存信息
     * @param pid 商品ID
     * @param form 用户选择的表单数据
     * @param listener
     */
    public void getPriceAndStockFromServer(long pid,String form,final OperationListener<JsonObject> listener) {
        //封装请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("productId",(int)pid);
        jObj.addProperty("form",form);
        this.httpPostAPI(ProductModel.URL_API_GET_PRICE_STOCK, jObj, new ApiHandleListener<JsonObject>() {
            @Override
            public void  done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (!obj.get("ExtraData").isJsonNull()) {
                                JsonObject res = obj.get("ExtraData").getAsJsonObject();
                                listener.done(res, null);
                            }
                            else{
                                listener.done(null, null);
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
     * 根据记录ID查询卡卷商品数据对象
     * @param ProductId
     * @return
     */
    public Product getProductById(long ProductId) {
        try {
            return ProductDaoService.getInstance(getContext()).getProduct(ProductId);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 根据商家ID查询所有的卡卷对象
     * @param merchantId
     * @return
     */
    public List<Product> getAllProductByMerchantId(int merchantId) {
        try {
            return ProductDaoService.getInstance(getContext()).getAllProductByMerchant(merchantId);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 根据类型查询所有的卡卷对应的商品对象
     * @param typeId
     * @return
     */
    public List<Product> getAllProductByType(int typeId) {
        try {
            return ProductDaoService.getInstance(getContext()).getAllProductByType(typeId);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 根据类型查询分页显示信息
     * @param typeId          卡卷类别
     * @param pageNum         当前页数
     * @param pageSize        每页显示数
     * @return                信息列表
     */
    public List<Product> getProductPageByType(int typeId,int pageNum,int pageSize) {
        try {
            return ProductDaoService.getInstance(getContext()).getPageProductByType(typeId,pageNum,pageSize);
        }
        catch (Exception e){ }
        return null;
    }


    /**
     * 修改卡卷对应的商品对象
     * @param product
     * @return
     */
    public boolean updaeProduct(Product product) {
        try {
            ProductDaoService.getInstance(getContext()).updateProduct(product);
            return true;
        }
        catch (Exception e){ return false;}
    }


    /**
     * 删除卡卷对应的商品对象
     * @param product
     * @return
     */
    public boolean deleteProduct(Product product) {
        try {
            ProductDaoService.getInstance(getContext()).deleteProduct(product);
            return true;
        }
        catch (Exception e){ return false;}
    }

    /**
     * 删除卡卷对应的商品对象
     * @param productId
     * @return
     */
    public boolean deleteProductById(long productId) {
        try {
            ProductDaoService.getInstance(getContext()).deleteProduct(productId);
            return true;
        }
        catch (Exception e){ return false;}
    }

}
