package com.campray.lesswalletandroid.model;

import android.net.Uri;
import android.text.TextUtils;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.db.entity.CouponStyle;
import com.campray.lesswalletandroid.db.entity.Product;
import com.campray.lesswalletandroid.db.entity.SpecAttr;
import com.campray.lesswalletandroid.db.entity.UserAttr;
import com.campray.lesswalletandroid.db.entity.UserValue;
import com.campray.lesswalletandroid.db.service.CountryDaoService;
import com.campray.lesswalletandroid.db.service.CouponDaoService;
import com.campray.lesswalletandroid.db.service.ProductDaoService;
import com.campray.lesswalletandroid.listener.ApiHandleListener;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.ImageUtil;
import com.campray.lesswalletandroid.util.TimeUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 卡卷对象业务处理模型
 * @author :Phills
 * @project:CouponModel
 * @date :2017-08-22-18:09
 */
public class CouponModel extends BaseModel {

    private static CouponModel ourInstance = new CouponModel();

    public static CouponModel getInstance() {
        return ourInstance;
    }

    private CouponModel() {
    }

    /**
     * 从服务器获取所有用戶卡卷信息数据
     * @param listener
     */
    public void getAllCouponsFromServer(final OperationListener<List<Coupon>> listener) {
        this.httpPostAPI(CouponModel.URL_API_GETALLCOUPONS, null,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                Gson gson = new Gson();
                                List<Coupon> couponList=new ArrayList<Coupon>();
                                if(jArr!=null) {
                                    for (JsonElement item : jArr) {
                                        final Coupon coupon = gson.fromJson(item, Coupon.class);
                                        try {
                                            CouponDaoService.getInstance(getContext()).insertOrUpdateCoupon(coupon);
                                            getProduct(coupon);
                                            couponList.add(coupon);
                                        } catch (Exception exe) {
                                            listener.done(null, new AppException("E_1005"));
                                            break;
                                        }
                                    }
                                }
                                listener.done(couponList, null);
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
     * 确认并获取免费卡卷
     * @param pid 产品ID
     * @param quantity  购买数量
     * @param form 用户填写的表单，json格式字符串
     * @param listener
     */
    public void confirmCoupon(long pid,int quantity,String form,final OperationListener<Coupon> listener){
        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("productId",pid);
        jObj.addProperty("quantity",quantity);
        jObj.addProperty("form",form);
        this.httpPostAPI(CouponModel.URL_API_CREATEORDER, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                Gson gson = new Gson();
                                if(jArr!=null) {
                                    for (JsonElement item : jArr) {
                                        final Coupon coupon = gson.fromJson(item, Coupon.class);
                                        try {
                                            CouponDaoService.getInstance(getContext()).insertOrUpdateCoupon(coupon);
                                            getProduct(coupon);
                                            listener.done(coupon, null);
                                            break;
                                        } catch (Exception exe) {
                                            listener.done(null, new AppException("E_1005"));
                                            break;
                                        }
                                    }
                                }
                            }
                            else {
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
     * 确认并支付卡卷
     * @param pid
     * @param listener
     */
    public void paypalCoupon(long pid,String nonce,int quantity,String form,final OperationListener<Coupon> listener){
        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("productId",pid);
        jObj.addProperty("nonce",nonce);
        jObj.addProperty("quantity",quantity);
        jObj.addProperty("form",form);
        this.httpPostAPI(CouponModel.URL_API_PAYPALCOUPON, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                Gson gson = new Gson();
                                if(jArr!=null) {
                                    for (JsonElement item : jArr) {
                                        final Coupon coupon = gson.fromJson(item, Coupon.class);
                                        try {
                                            CouponDaoService.getInstance(getContext()).insertOrUpdateCoupon(coupon);
                                            getProduct(coupon);
                                            listener.done(coupon, null);
                                        } catch (Exception e) {
                                            listener.done(null, new AppException("E_1005"));
                                        }
                                    }
                                }
                            }
                            else {
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
     * 根据订单ID从服务端获取到卡卷
     * @param oid 订单ID
     * @param listener
     */
    public void getCouponFromServer(long oid,final OperationListener<Coupon> listener){
        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("orderId",oid);
        this.httpPostAPI(CouponModel.URL_API_GETCOUPON, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            if (obj.get("Data").isJsonArray()) {
                                JsonArray jArr = obj.get("Data").getAsJsonArray();
                                Gson gson = new Gson();
                                if(jArr!=null) {
                                    for (JsonElement item : jArr) {
                                        final Coupon coupon = gson.fromJson(item, Coupon.class);
                                        try {
                                            CouponDaoService.getInstance(getContext()).insertOrUpdateCoupon(coupon);
                                            getProduct(coupon);
                                            listener.done(coupon, null);
                                            break;
                                        } catch (Exception exe) {
                                            listener.done(null, new AppException("E_1005"));
                                            break;
                                        }
                                    }
                                }
                            }
                            else {
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
     * 从服务器删除指定的卡卷数据
     * @param ids 要删除的卡卷ID字符串
     * @param listener
     */
    public void delCouponsFromServer(String ids, final OperationListener<Coupon> listener) {
        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("orderIds",ids);
        jObj.addProperty("isvendor",false);
        this.httpPostAPI(CouponModel.URL_API_DELCOUPONS, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //在本地数据库删除服务器已经被移除的记录
                        JsonArray jArr = obj.get("Data").getAsJsonArray();
                        if(jArr!=null) {
                            for (JsonElement item : jArr) {
                                int deletedCouponId = item.getAsInt();
                                Coupon coupon = getCouponById(deletedCouponId);
                                Product product = coupon.getProduct();
                                if (product.getCoupons().size() <= 1) {
                                    ProductModel.getInstance().deleteProduct(product);
                                }
                                deleteCouponById(deletedCouponId);
                            }
                        }

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

    /**
     * 转送卡卷给好友
     * @param couponId 卡卷ID
     * @param friendId 好友ID
     * @param listener
     */
    public void sentCouponsToFriend(final long couponId,final long friendId, final OperationListener<Coupon> listener) {
        //封装登录请求参数
        JsonObject jObj=new JsonObject();
        jObj.addProperty("orderId",couponId);
        jObj.addProperty("friendId",friendId);
        this.httpPostAPI(CouponModel.URL_API_SENT_COUPON, jObj,new ApiHandleListener<JsonObject>() {
            @Override
            public void done(JsonObject obj, AppException exception) {
                if (exception == null) {
                    try {
                        //如果返回结果没有异常
                        if (obj.get("Errors").isJsonNull()) {
                            //在本地数据库删除服务器已经被修改成好友的订单记录
                            Coupon coupon=getCouponById(couponId);
                            Product product=coupon.getProduct();
                            if(product.getCoupons().size()<=1){
                                ProductModel.getInstance().deleteProduct(product);
                            }
                            deleteCouponById(couponId);
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
     * 查询所有卡卷数据对象
     * @return
     */
    public List<Coupon> getAllCoupons() {
        try {
            return CouponDaoService.getInstance(getContext()).getAllCoupons();
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 根据类型查询所有的卡卷对象
     * @param typeId
     * @return
     */
    public List<Coupon> getAllCouponByType(int typeId) {
        try {
            return CouponDaoService.getInstance(getContext()).getAllCouponByType(typeId);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 分页显示信息
     * @param typeId          卡卷类别
     * @param pageNum         当前页数
     * @param pageSize        每页显示数
     * @return                信息列表
     */
    public List<Coupon> getCouponsPageByType(int typeId,int pageNum,int pageSize)
    {
        try {
            return CouponDaoService.getInstance(getContext()).getPageCouponByType(typeId,pageNum,pageSize);
        }
        catch (Exception e){ }
        return null;
    }

    /**
     * 根据记录ID查询卡卷数据对象
     * @param couponId
     * @return
     */
    public Coupon getCouponById(long couponId) {
        try {
            return CouponDaoService.getInstance(getContext()).getCoupon(couponId);
        }
        catch (Exception e){ }
        return null;
    }



    /**
     * 修改卡卷对象
     * @param coupon
     * @return
     */
    public boolean updateCoupon(Coupon coupon) {
        try {
            CouponDaoService.getInstance(getContext()).updateCoupon(coupon);
            return true;
        }
        catch (Exception e){ return false;}
    }

    /**
     * 删除卡卷对象
     * @param coupon
     * @return
     */
    public boolean deleteCoupon(Coupon coupon) {
        try {
           CouponDaoService.getInstance(getContext()).deleteCoupon(coupon);
            return true;
        }
        catch (Exception e){ return false;}
    }

    /**
     * 删除卡卷对象
     * @param couponId
     * @return
     */
    public boolean deleteCouponById(long couponId) {
        try {
            CouponDaoService.getInstance(getContext()).deleteCoupon(couponId);
            return true;
        }
        catch (Exception exe){
            return false;
        }
    }


    public void getProduct(final Coupon coupon){
        try {
            //如果卡卷商品在手机上没有保存过
            if (!ProductDaoService.getInstance(getContext()).hasProduct(coupon.getProductId())) {
                ProductModel.getInstance().getProductFromServer(coupon.getProductId(), new OperationListener<Product>() {
                    @Override
                    public void done(final Product product, AppException exception) {
                        if (exception == null) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        //保存优惠卷相关图片到手机
                                        boolean needUpdate = false;
                                        List<SpecAttr> specAttrBeanList = product.getSpecAttr();
                                        if(specAttrBeanList!=null) {
                                            for (SpecAttr specAttrBean : specAttrBeanList) {
                                                //如果是底纹,样式图片或logo
                                                if (specAttrBean.getSpecificationAttributeId() == 6 || specAttrBean.getSpecificationAttributeId() == 7 || specAttrBean.getSpecificationAttributeId() == 8) {
                                                    if (TextUtils.isEmpty(specAttrBean.getFileUrl())) {
                                                        String imgUrl = specAttrBean.getValueRaw();
                                                        File folder = LessWalletApplication.INSTANCE().getPrivateDir();
                                                        File picFile = new File(folder, "p_" + product.getProductId() + "_" + specAttrBean.getSpecificationAttributeId());
                                                        //保存网络图片到手机存储空间,并返回uri
                                                        Uri imgUri = ImageUtil.saveImageToUri(imgUrl, picFile);
                                                        specAttrBean.setFileUrl(imgUri.toString());
                                                        needUpdate = true;
                                                    }
                                                }
                                            }
                                        }
                                        if (needUpdate) {
                                            ProductModel.getInstance().updaeProduct(product);
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }).start();

                            try {
                                CouponStyle couponStyle = product.getCouponStyle();
                                if (couponStyle.getValidityYear() > 0 || couponStyle.getValidityMonth() > 0 || couponStyle.getValidityDay() > 0) {
                                    String endDateStr = TimeUtil.modifyDateStr(coupon.getStartTime(), TimeUtil.FORMAT_DATE_TIME_SECOND, TimeZone.getTimeZone("UTC"), couponStyle.getValidityYear(), couponStyle.getValidityMonth(), couponStyle.getValidityDay());
                                    coupon.setEndTime(endDateStr);
                                    CouponDaoService.getInstance(getContext()).updateCoupon(coupon);
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                });
            } else {
                CouponStyle couponStyle = coupon.getCouponStyle();
                if (couponStyle.getValidityYear() > 0 || couponStyle.getValidityMonth() > 0 || couponStyle.getValidityDay() > 0) {
                    String endDateStr = TimeUtil.modifyDateStr(coupon.getStartTime(), TimeUtil.FORMAT_DATE_TIME_SECOND, TimeZone.getTimeZone("UTC"), couponStyle.getValidityYear(), couponStyle.getValidityMonth(), couponStyle.getValidityDay());
                    coupon.setEndTime(endDateStr);
                    CouponDaoService.getInstance(getContext()).updateCoupon(coupon);
                }

            }
        }
        catch (Exception exe){}
    }

    //如果是电子票，要计算用户选择的时间做为结束时间
    private String ticketEndTime(Coupon coupon){
        if(coupon.getProduct().getProductTypeId()==2)
        {
            List<UserAttr> userAttrList = coupon.getProduct().getUserAttr();
            coupon.getProduct().getUserAttrMap();
            List<UserValue> userValueList =coupon.getUserValues();
            if (userAttrList != null) {
                for (UserAttr userAttr : userAttrList) {
                    long attrId = userAttr.getProductAttributeId();
                    if (attrId == 7) {

                    } else if (attrId == 8) {

                    } else if (attrId == 9) {

                    }
                }
            }
        }
        return "";
    }

}
