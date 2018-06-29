package com.campray.lesswalletandroid.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import com.campray.lesswalletandroid.db.dao.DaoSession;
import com.campray.lesswalletandroid.db.dao.CurrencyDao;
import com.campray.lesswalletandroid.db.dao.LanguageDao;

/**
 * Created by Phills on 10/25/2017.
 */
@Entity
public class Language {
    @Id(autoincrement = false)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String languageCulture;
    @NotNull
    private String uniqueSeoCode;
    @NotNull
    private int displayOrder;
    @NotNull
    private Long defaultCurrencyId;//语言默认使用货币
    @ToOne(joinProperty="defaultCurrencyId")
    private Currency currency;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1882712897)
    private transient LanguageDao myDao;
    @Generated(hash = 1489923924)
    private transient Long currency__resolvedKey;

    @Generated(hash = 19771401)
    public Language(Long id, @NotNull String name, @NotNull String languageCulture,
            @NotNull String uniqueSeoCode, int displayOrder,
            @NotNull Long defaultCurrencyId) {
        this.id = id;
        this.name = name;
        this.languageCulture = languageCulture;
        this.uniqueSeoCode = uniqueSeoCode;
        this.displayOrder = displayOrder;
        this.defaultCurrencyId = defaultCurrencyId;
    }
    @Generated(hash = 1478671802)
    public Language() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLanguageCulture() {
        return this.languageCulture;
    }
    public void setLanguageCulture(String languageCulture) {
        this.languageCulture = languageCulture;
    }
    public String getUniqueSeoCode() {
        return this.uniqueSeoCode;
    }
    public void setUniqueSeoCode(String uniqueSeoCode) {
        this.uniqueSeoCode = uniqueSeoCode;
    }
    public int getDisplayOrder() {
        return this.displayOrder;
    }
    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
    public Long getDefaultCurrencyId() {
        return this.defaultCurrencyId;
    }
    public void setDefaultCurrencyId(Long defaultCurrencyId) {
        this.defaultCurrencyId = defaultCurrencyId;
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 174641453)
    public Currency getCurrency() {
        Long __key = this.defaultCurrencyId;
        if (currency__resolvedKey == null || !currency__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CurrencyDao targetDao = daoSession.getCurrencyDao();
            Currency currencyNew = targetDao.load(__key);
            synchronized (this) {
                currency = currencyNew;
                currency__resolvedKey = __key;
            }
        }
        return currency;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 686179686)
    public void setCurrency(@NotNull Currency currency) {
        if (currency == null) {
            throw new DaoException(
                    "To-one property 'defaultCurrencyId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.currency = currency;
            defaultCurrencyId = currency.getId();
            currency__resolvedKey = defaultCurrencyId;
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
    @Generated(hash = 732273607)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLanguageDao() : null;
    }


}
