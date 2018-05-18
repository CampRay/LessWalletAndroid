package com.campray.lesswalletandroid.bean;

import android.text.TextUtils;

import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.db.service.CouponDaoService;
import com.campray.lesswalletandroid.util.TimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Phills on 10/24/2017.
 */
public class CouponBean {
    private long orderId;//对应的服务端订单ID
    private String cid; //卡卷編號
    private long productId;//对应的商品ID
    private ProductBean product;//卡卷商品信息对象
    private float orderTotal;//总支付金额
    private int paymentStatus;//支付状态: 已支付 30
    private String userName ;//所属用户名
    private String startTime;//生效时间
    private String endTime;//过期时间
    private String startTimeLocal;//生效时间-显示用户时区
    private String endTimeLocal;//过期时间-显示用户时区
    private boolean deleted=false;//是否已使用

    public CouponBean fromCoupon(Coupon coupon){
        if(coupon!=null) {
            startTime = coupon.getStartTime();
            endTime = coupon.getEndTime();
            try {
                if (!TextUtils.isEmpty(startTime)) {
                    startTimeLocal = TimeUtil.dateToString(TimeUtil.stringToDate(startTime, TimeUtil.FORMAT_DATE_TIME_SECOND, TimeZone.getTimeZone("UTC")), TimeUtil.FORMAT_DATE);
                }
                if (!TextUtils.isEmpty(endTime)) {
                    endTimeLocal = TimeUtil.dateToString(TimeUtil.stringToDate(endTime, TimeUtil.FORMAT_DATE_TIME_SECOND, TimeZone.getTimeZone("UTC")), TimeUtil.FORMAT_DATE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            orderId = coupon.getOrderId();
            cid = coupon.getCid();
            productId =coupon.getProductId();
            orderTotal = coupon.getOrderTotal();
            paymentStatus = coupon.getPaymentStatus();
            userName = coupon.getUserName();
            deleted = coupon.getDeleted();
            product=new ProductBean().fromProduct(coupon.getProduct());
        }
        return this;
    }

    public Coupon tomCoupon(){
        if(orderId==0) {
            return null;
        }
        Coupon coupon=new Coupon();
        coupon.setOrderId(orderId);
        coupon.setCid(cid);
        coupon.setProductId(productId);
        coupon.setOrderTotal(orderTotal);
        coupon.setPaymentStatus(paymentStatus);
        coupon.setUserName(userName);
        coupon.setStartTime(startTime);
        coupon.setEndTime(endTime);
        coupon.setDeleted(deleted);
        return coupon;
    }

    public CouponBean fromCoupon(Coupon coupon,boolean related){
        if(coupon!=null) {
            this.fromCoupon(coupon);
            product = new ProductBean().fromProduct(coupon.getProduct());
        }
        return this;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public float getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(float orderTotal) {
        this.orderTotal = orderTotal;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getStartTimeLocal() {
        return startTimeLocal;
    }

    public void setStartTimeLocal(String startTimeLocal) {
        this.startTimeLocal = startTimeLocal;
    }

    public String getEndTimeLocal() {
        return endTimeLocal;
    }

    public void setEndTimeLocal(String endTimeLocal) {
        this.endTimeLocal = endTimeLocal;
    }
}
