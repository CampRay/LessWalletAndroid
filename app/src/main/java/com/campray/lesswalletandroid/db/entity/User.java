package com.campray.lesswalletandroid.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import com.campray.lesswalletandroid.db.dao.DaoSession;
import com.campray.lesswalletandroid.db.dao.LanguageDao;
import com.campray.lesswalletandroid.db.dao.CountryDao;
import com.campray.lesswalletandroid.db.dao.UserDao;
import com.campray.lesswalletandroid.db.dao.CurrencyDao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户表对应的实体对象
 * @author :Phills
 * @project:User
 * @date :2017-10-22-18:11
 */
@Entity
public class User {
    @Id(autoincrement = false)
    private Long id;//对应的用户表的ID
    @Unique
    private String userName;
    @NotNull
    private String email;
    @NotNull
    private String password;
    private String mobile;
    private String firstName;
    private String lastName;
    private String birthday;
    private long countryId;
    @ToOne(joinProperty = "countryId")
    private Country country;
    private String address;
    private String token;
    private Long languageId;//当前用户最后使用的语言
    @ToOne(joinProperty = "languageId")
    private Language language;
    private Long currencyId;//当前用户货币
    @ToOne(joinProperty = "currencyId")
    private  Currency currenty;

    private boolean remember;//下次是否自动登入
    //头像URL
    private String avatarUrl;
    //头像在本地存储路径
    private String avatorPath;

    @Transient
    private String fullName;

    public String getFullName() {
        //全是字母的正则表达式
        String regex = "^[A-Za-z]+$";
        Pattern p = Pattern.compile(regex);
        //判断Str是否匹配，返回匹配结果
        Matcher m1 = p.matcher(firstName);
        if(p.matcher(firstName).find()&&p.matcher(lastName).find()){
            return firstName+" "+lastName;
        }
        else{
            return lastName+" "+firstName;
        }
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1507654846)
    private transient UserDao myDao;
    @Generated(hash = 1515643813)
    public User(Long id, String userName, @NotNull String email, @NotNull String password,
            String mobile, String firstName, String lastName, String birthday, long countryId,
            String address, String token, Long languageId, Long currencyId, boolean remember,
            String avatarUrl, String avatorPath) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.countryId = countryId;
        this.address = address;
        this.token = token;
        this.languageId = languageId;
        this.currencyId = currencyId;
        this.remember = remember;
        this.avatarUrl = avatarUrl;
        this.avatorPath = avatorPath;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getMobile() {
        return this.mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getBirthday() {
        return this.birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public long getCountryId() {
        return this.countryId;
    }
    public void setCountryId(long countryId) {
        this.countryId = countryId;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getToken() {
        return this.token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Long getLanguageId() {
        return this.languageId;
    }
    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }
    public boolean getRemember() {
        return this.remember;
    }
    public void setRemember(boolean remember) {
        this.remember = remember;
    }
    public String getAvatarUrl() {
        return this.avatarUrl;
    }
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    public String getAvatorPath() {
        return this.avatorPath;
    }
    public void setAvatorPath(String avatorPath) {
        this.avatorPath = avatorPath;
    }
    @Generated(hash = 1591299782)
    private transient Long country__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 240571258)
    public Country getCountry() {
        long __key = this.countryId;
        if (country__resolvedKey == null || !country__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CountryDao targetDao = daoSession.getCountryDao();
            Country countryNew = targetDao.load(__key);
            synchronized (this) {
                country = countryNew;
                country__resolvedKey = __key;
            }
        }
        return country;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 617812975)
    public void setCountry(@NotNull Country country) {
        if (country == null) {
            throw new DaoException(
                    "To-one property 'countryId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.country = country;
            countryId = country.getId();
            country__resolvedKey = countryId;
        }
    }
    @Generated(hash = 1911749295)
    private transient Long language__resolvedKey;
    @Generated(hash = 239476790)
    private transient Long currenty__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1246715636)
    public Language getLanguage() {
        Long __key = this.languageId;
        if (language__resolvedKey == null || !language__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LanguageDao targetDao = daoSession.getLanguageDao();
            Language languageNew = targetDao.load(__key);
            synchronized (this) {
                language = languageNew;
                language__resolvedKey = __key;
            }
        }
        return language;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2141598877)
    public void setLanguage(Language language) {
        synchronized (this) {
            this.language = language;
            languageId = language == null ? null : language.getId();
            language__resolvedKey = languageId;
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
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Long getCurrencyId() {
        return this.currencyId;
    }
    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1731906640)
    public Currency getCurrenty() {
        Long __key = this.currencyId;
        if (currenty__resolvedKey == null || !currenty__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CurrencyDao targetDao = daoSession.getCurrencyDao();
            Currency currentyNew = targetDao.load(__key);
            synchronized (this) {
                currenty = currentyNew;
                currenty__resolvedKey = __key;
            }
        }
        return currenty;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 271255894)
    public void setCurrenty(Currency currenty) {
        synchronized (this) {
            this.currenty = currenty;
            currencyId = currenty == null ? null : currenty.getId();
            currenty__resolvedKey = currencyId;
        }
    }



}
