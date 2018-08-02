package com.campray.lesswalletandroid.db.entity;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Transient;

import com.campray.lesswalletandroid.db.converter.SpecAttrConverter;
import com.campray.lesswalletandroid.db.converter.UserAttrConverter;
import com.campray.lesswalletandroid.db.dao.DaoSession;
import com.campray.lesswalletandroid.db.dao.MerchantDao;
import com.campray.lesswalletandroid.db.dao.CurrencyDao;
import com.campray.lesswalletandroid.db.dao.ProductDao;
import com.campray.lesswalletandroid.model.CurrencyModel;
import com.campray.lesswalletandroid.util.TimeUtil;

import java.util.List;
import java.util.TimeZone;
import com.campray.lesswalletandroid.db.dao.CouponDao;

/**
 * 商家添加的卡卷类商品信息
 * Created by Phills on 11/22/2017.
 */
@Entity
public class Product {
    @Id(autoincrement =false )
    private Long productId;//对应的服务器产品表的商品ID
    @NotNull
    private int productTypeId;//卡卷类型：1 coupon; 2 Ticket; 3 Card;
    @NotNull
    private int productTemplateId;//卡卷样式模板ID:预先设计好的卡卷模板类型
    @NotNull
    private String title; //显示标题
    private String shortDesc; //快速描述
    private String fullDesc; //快速描述
    private String agreement;  //法律条款或协议
    private String numPrefix;//卡卷编号前缀
    private float price; //商品价格
    private String currencyCode ;//商品货币Code
    private String startTime;//生效时间
    private String endTime;//过期时间
    private boolean published;//是否已发布
    private boolean deleted;//是否已删除
    private int stockQuantity=0;//库存数量

//    private String specAttrStr;//所有规格属性的Json字符串
    @Convert(columnType = String.class,converter = SpecAttrConverter.class)
    private List<SpecAttr> specAttr;//样式属性
    @Convert(columnType = String.class,converter = UserAttrConverter.class)
    private List<UserAttr> userAttr;//用户选择项属性
    private Long merchantId; //商家ID
    @ToOne(joinProperty = "merchantId")
    private Merchant merchant;//关联的商家数据对象
    @ToMany(referencedJoinProperty = "productId")
    private List<Coupon> coupons;//对应用户的订单记录

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
    @Generated(hash = 694336451)
    private transient ProductDao myDao;

    @Generated(hash = 597044329)
    public Product(Long productId, int productTypeId, int productTemplateId, @NotNull String title, String shortDesc, String fullDesc, String agreement, String numPrefix,
            float price, String currencyCode, String startTime, String endTime, boolean published, boolean deleted, int stockQuantity, List<SpecAttr> specAttr,
            List<UserAttr> userAttr, Long merchantId) {
        this.productId = productId;
        this.productTypeId = productTypeId;
        this.productTemplateId = productTemplateId;
        this.title = title;
        this.shortDesc = shortDesc;
        this.fullDesc = fullDesc;
        this.agreement = agreement;
        this.numPrefix = numPrefix;
        this.price = price;
        this.currencyCode = currencyCode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.published = published;
        this.deleted = deleted;
        this.stockQuantity = stockQuantity;
        this.specAttr = specAttr;
        this.userAttr = userAttr;
        this.merchantId = merchantId;
    }

    @Generated(hash = 1890278724)
    public Product() {
    }

    @Generated(hash = 129747413)
    private transient Long merchant__resolvedKey;

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

    public String getPriceStr(){
        Currency currency= CurrencyModel.getInstance().getDefaultCurrency();
        String fmtStr="%s";
        if(!TextUtils.isEmpty(currency.getCustomFormatting())){
            fmtStr=currency.getCustomFormatting().replace("0.00","%.2f");
        }
        return String.format(fmtStr,price);
    }

    public CouponStyle getCouponStyle() {
        if(couponStyle==null) {
            couponStyle=new CouponStyle();
            //循环遍历当前优惠卷的产品规格属性
            for (SpecAttr item : this.specAttr) {
                String value = item.getValueRaw();//用户自已输入的值
                if (item.getSpecificationAttributeId() == 1) {
                    couponStyle.setBenefitFree(value);
                } else if (item.getSpecificationAttributeId() == 2) {
                    couponStyle.setBenefitCash(value);
                } else if (item.getSpecificationAttributeId() == 3) {
                    couponStyle.setBenefitDiscount(value);
                } else if (item.getSpecificationAttributeId() == 4) {
                    couponStyle.setBenefitCustomized(value);
                }else if (item.getSpecificationAttributeId() == 5) {//如果是背景色
                    couponStyle.setBgColor(value);
                } else if (item.getSpecificationAttributeId() == 6) {//如果是底纹
                    couponStyle.setShadingUrl(TextUtils.isEmpty(item.getFileUrl()) ? value : item.getFileUrl());
                } else if (item.getSpecificationAttributeId() == 7) {//如果是自定义图片
                    couponStyle.setPictureUrl(TextUtils.isEmpty(item.getFileUrl()) ? value : item.getFileUrl());
                } else if (item.getSpecificationAttributeId() == 8) {//如果是商用家logo
                    couponStyle.setLogoUrl(TextUtils.isEmpty(item.getFileUrl()) ? value : item.getFileUrl());
                } else if (item.getSpecificationAttributeId() == 9) {//如果是商用会员卡级别
                    try {
                        couponStyle.setCardLevel(Integer.parseInt(value));
                    }catch (Exception e){}
                } else if (item.getSpecificationAttributeId() == 10) {//如果是有效期（日）
                    try {
                        couponStyle.setValidityDay(Integer.parseInt(value));
                    }catch (Exception e){}
                } else if (item.getSpecificationAttributeId() == 11) {//如果是有效期（月）
                    try {
                        couponStyle.setValidityMonth(Integer.parseInt(value));
                    }catch (Exception e){}
                } else if (item.getSpecificationAttributeId() == 12) {//如果是有效期（年）
                    try {
                        couponStyle.setValidityYear(Integer.parseInt(value));
                    }catch (Exception e){}
                } else {
                }
            }
        }
        return couponStyle;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getProductTypeId() {
        return this.productTypeId;
    }

    public void setProductTypeId(int productTypeId) {
        this.productTypeId = productTypeId;
    }

    public int getProductTemplateId() {
        return this.productTemplateId;
    }

    public void setProductTemplateId(int productTemplateId) {
        this.productTemplateId = productTemplateId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDesc() {
        return this.shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getFullDesc() {
        return this.fullDesc;
    }

    public void setFullDesc(String fullDesc) {
        this.fullDesc = fullDesc;
    }

    public String getAgreement() {
        return this.agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public String getNumPrefix() {
        return this.numPrefix;
    }

    public void setNumPrefix(String numPrefix) {
        this.numPrefix = numPrefix;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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

    public boolean getPublished() {
        return this.published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getStockQuantity() {
        return this.stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public List<SpecAttr> getSpecAttr() {
        return this.specAttr;
    }

    public void setSpecAttr(List<SpecAttr> specAttr) {
        this.specAttr = specAttr;
    }

    public List<UserAttr> getUserAttr() {
        return this.userAttr;
    }

    public void setUserAttr(List<UserAttr> userAttr) {
        this.userAttr = userAttr;
    }

    public Long getMerchantId() {
        return this.merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 890615869)
    public Merchant getMerchant() {
        Long __key = this.merchantId;
        if (merchant__resolvedKey == null || !merchant__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MerchantDao targetDao = daoSession.getMerchantDao();
            Merchant merchantNew = targetDao.load(__key);
            synchronized (this) {
                merchant = merchantNew;
                merchant__resolvedKey = __key;
            }
        }
        return merchant;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 53631488)
    public void setMerchant(Merchant merchant) {
        synchronized (this) {
            this.merchant = merchant;
            merchantId = merchant == null ? null : merchant.getId();
            merchant__resolvedKey = merchantId;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 157365863)
    public List<Coupon> getCoupons() {
        if (coupons == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CouponDao targetDao = daoSession.getCouponDao();
            List<Coupon> couponsNew = targetDao._queryProduct_Coupons(productId);
            synchronized (this) {
                if (coupons == null) {
                    coupons = couponsNew;
                }
            }
        }
        return coupons;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 981296410)
    public synchronized void resetCoupons() {
        coupons = null;
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
    @Generated(hash = 1171535257)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getProductDao() : null;
    }



}
