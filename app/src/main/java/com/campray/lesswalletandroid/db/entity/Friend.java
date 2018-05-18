package com.campray.lesswalletandroid.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.DaoException;
import com.campray.lesswalletandroid.db.dao.DaoSession;
import com.campray.lesswalletandroid.db.dao.LanguageDao;
import com.campray.lesswalletandroid.db.dao.CountryDao;
import com.campray.lesswalletandroid.db.dao.FriendDao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 好友表对应的实体对象
 * @author :Phills
 * @project:friend
 * @date 2018-4-8
 */
@Entity
public class Friend {
    @Id(autoincrement = false)
    private Long id;
    @Unique
    private String userName;
    @NotNull
    private String email;
    private String mobile;
    private String firstName;
    private String lastName;
    private String birthday;
    private long countryId;
    @ToOne(joinProperty = "countryId")
    private Country country;
    private String address;
    private Long languageId;//使用的语言
    @ToOne(joinProperty = "languageId")
    private Language language;
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
    @Generated(hash = 76285035)
    private transient FriendDao myDao;
    @Generated(hash = 811935300)
    public Friend(Long id, String userName, @NotNull String email, String mobile,
            String firstName, String lastName, String birthday, long countryId,
            String address, Long languageId, String avatarUrl, String avatorPath) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.mobile = mobile;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.countryId = countryId;
        this.address = address;
        this.languageId = languageId;
        this.avatarUrl = avatarUrl;
        this.avatorPath = avatorPath;
    }
    @Generated(hash = 287143722)
    public Friend() {
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
    public Long getLanguageId() {
        return this.languageId;
    }
    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
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
    @Generated(hash = 1516049992)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFriendDao() : null;
    }

}
