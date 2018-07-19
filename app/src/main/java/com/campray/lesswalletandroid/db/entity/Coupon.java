package com.campray.lesswalletandroid.db.entity;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Transient;

import com.campray.lesswalletandroid.db.converter.BenefitAttrConverter;
import com.campray.lesswalletandroid.db.dao.DaoSession;
import com.campray.lesswalletandroid.db.dao.ProductDao;
import com.campray.lesswalletandroid.db.dao.CouponDao;
import com.campray.lesswalletandroid.util.TimeUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


/**
 * 卡卷订单信息
 * Created by Phills on 10/24/2017.
 */
@Entity
public class Coupon {
    @Id(autoincrement =false )
    private Long orderId;//对应的服务端订单ID
    private Long productId;//对应的商品ID
    @ToOne(joinProperty="productId")
    private Product product;
    private String cid; //卡卷編號
    @NotNull
    private Long userId; //所属用户Id
    private float orderTotal;//总支付金额
    private int paymentStatus;//支付状态: 已支付 30
    @NotNull
    private String startTime;//支付时间
    private String endTime;//过期时间
    @NotNull
    private boolean deleted=false;//是否已使用
    @Convert(columnType = String.class,converter = BenefitAttrConverter.class)
    private HashMap<String,String> customValues;//是否已使用
    @Transient
    private List<UserAttrValue> userAttrValues; //用户选择或输入的属性的值

    @Transient
    private String startTimeLocal;//生效时间-显示用户时区
    @Transient
    private String endTimeLocal;//过期时间-显示用户时区
    @Transient
    private CouponStyle couponStyle;//Coupon显示样式
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 533881652)
    private transient CouponDao myDao;

    @Generated(hash = 1197477488)
    public Coupon(Long orderId, Long productId, String cid, @NotNull Long userId, float orderTotal, int paymentStatus, @NotNull String startTime, String endTime, boolean deleted,
            HashMap<String, String> customValues) {
        this.orderId = orderId;
        this.productId = productId;
        this.cid = cid;
        this.userId = userId;
        this.orderTotal = orderTotal;
        this.paymentStatus = paymentStatus;
        this.startTime = startTime;
        this.endTime = endTime;
        this.deleted = deleted;
        this.customValues = customValues;
    }

    @Generated(hash = 75265961)
    public Coupon() {
    }

    @Generated(hash = 587652864)
    private transient Long product__resolvedKey;

    public String getStartTimeLocal() {
        if (!TextUtils.isEmpty(startTime)) {
            startTimeLocal = TimeUtil.dateToString(TimeUtil.stringToDate(startTime, TimeUtil.FORMAT_DATE_TIME_SECOND, TimeZone.getTimeZone("UTC")), TimeUtil.FORMAT_DATE);
        }
        return startTimeLocal;
    }

    public void setStartTimeLocal(String startTimeLocal) {
        this.startTimeLocal = startTimeLocal;
    }

    public String getEndTimeLocal() {
        if (!TextUtils.isEmpty(endTime)) {
            endTimeLocal = TimeUtil.dateToString(TimeUtil.stringToDate(endTime, TimeUtil.FORMAT_DATE_TIME_SECOND, TimeZone.getTimeZone("UTC")), TimeUtil.FORMAT_DATE);
        }
        return endTimeLocal;
    }

    public void setEndTimeLocal(String endTimeLocal) {
        this.endTimeLocal = endTimeLocal;
    }

    public CouponStyle getCouponStyle() {
        if(this.getProduct()!=null) {
            if(couponStyle==null) {
                couponStyle = new CouponStyle();
                //循环遍历当前优惠卷的产品规格属性
                for (SpecAttr specAttr : this.getProduct().getSpecAttr()) {
                    //String selectValue = specAttr.getColorSquaresRgb();//用户通过下接选项选择的值
                    String value = specAttr.getValueRaw();//用户自已输入的值
                    //String value = (TextUtils.isEmpty(selectValue) && TextUtils.isEmpty(customValue)) ? specAttr.getSpecificationAttributeName() : (TextUtils.isEmpty(selectValue) ? customValue : selectValue);
                    if (specAttr.getSpecificationAttributeId() == 1) {
                        couponStyle.setBenefitOne(value);
                    } else if (specAttr.getSpecificationAttributeId() == 2) {
                        couponStyle.setBenefitPrepaidCash(value);
                    } else if (specAttr.getSpecificationAttributeId() == 3) {
                        couponStyle.setBenefitPrepaidService(value);
                    } else if (specAttr.getSpecificationAttributeId() == 4) {
                        couponStyle.setBenefitBuyNGetOne(value);
                    }else if (specAttr.getSpecificationAttributeId() == 5) {//如果是背景色
                        couponStyle.setBgColor(value);
                    } else if (specAttr.getSpecificationAttributeId() == 6) {//如果是底纹
                        couponStyle.setShadingUrl(TextUtils.isEmpty(specAttr.getFileUrl()) ? value : specAttr.getFileUrl());
                    } else if (specAttr.getSpecificationAttributeId() == 7) {//如果是自定义图片
                        couponStyle.setPictureUrl(TextUtils.isEmpty(specAttr.getFileUrl()) ? value : specAttr.getFileUrl());
                    } else if (specAttr.getSpecificationAttributeId() == 8) {//如果是商用家logo
                        couponStyle.setLogoUrl(TextUtils.isEmpty(specAttr.getFileUrl()) ? value : specAttr.getFileUrl());
                    } else if (specAttr.getSpecificationAttributeId() == 9) {//如果是商用会员卡级别
                        try {
                            couponStyle.setCardLevel(Integer.parseInt(value));
                        }catch (Exception e){}
                    } else if (specAttr.getSpecificationAttributeId() == 10) {//如果是有效期（日）
                        try {
                            couponStyle.setValidityDay(Integer.parseInt(value));
                        }catch (Exception e){}
                    } else if (specAttr.getSpecificationAttributeId() == 11) {//如果是有效期（月）
                        try {
                            couponStyle.setValidityMonth(Integer.parseInt(value));
                        }catch (Exception e){}
                    } else if (specAttr.getSpecificationAttributeId() == 12) {//如果是有效期（年）
                        try {
                            couponStyle.setValidityYear(Integer.parseInt(value));
                        }catch (Exception e){}
                    } else {
                    }
                }
            }
        }
        return couponStyle;
    }

    public void setCouponStyle(CouponStyle couponStyle) {
        this.couponStyle = couponStyle;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getCid() {
        return this.cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public float getOrderTotal() {
        return this.orderTotal;
    }

    public void setOrderTotal(float orderTotal) {
        this.orderTotal = orderTotal;
    }

    public int getPaymentStatus() {
        return this.paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public HashMap<String, String> getCustomValues() {
        return this.customValues;
    }

    public void setCustomValues(HashMap<String, String> customValues) {
        this.customValues = customValues;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1198864293)
    public Product getProduct() {
        Long __key = this.productId;
        if (product__resolvedKey == null || !product__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ProductDao targetDao = daoSession.getProductDao();
            Product productNew = targetDao.load(__key);
            synchronized (this) {
                product = productNew;
                product__resolvedKey = __key;
            }
        }
        return product;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 585838205)
    public void setProduct(Product product) {
        synchronized (this) {
            this.product = product;
            productId = product == null ? null : product.getProductId();
            product__resolvedKey = productId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1044558887)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCouponDao() : null;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    

}
