package com.campray.lesswalletandroid.bean;

import com.campray.lesswalletandroid.db.entity.Country;
import com.campray.lesswalletandroid.db.entity.User;

/**
 * @author :Phills
 * @project:UserBean
 * @date :2017-10-22-18:11
 */
public class UserBean{

    private String userName;
    private String email;
    private String mobile;
    private String firstName;
    private String lastName;
    private String birthday;
    private long countryId;
    private CountryBean country;
    private String address;
    private String token;
    private long languageId;//当前用户最后使用的语言
    private LanguageBean language;//当前用户最后使用的语言
    private boolean remember;//下次是否自动登入

    public UserBean(){}

    public UserBean fromUser(User user){
        if(user!=null) {
            userName = user.getUserName();
            email = user.getEmail();
            mobile = user.getMobile();
            firstName = user.getFirstName();
            lastName = user.getLastName();
            birthday = user.getBirthday();
            countryId=user.getCountryId();
            address = user.getAddress();
            token = user.getToken();
            languageId=user.getLanguageId();
            remember = user.getRemember();
        }
        return this;
    }
    public UserBean fromUser(User user,boolean related){
        if(user!=null) {
            this.fromUser(user);
            country = new CountryBean().fromCountry(user.getCountry());
            language = new LanguageBean().fromLanguage(user.getLanguage());
        }
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public CountryBean getCountry() {
        return country;
    }

    public void setCountry(CountryBean country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LanguageBean getLanguage() {
        return language;
    }

    public void setLanguage(LanguageBean language) {
        this.language = language;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }

    public long getCountryId() {
        return countryId;
    }

    public void setCountryId(long countryId) {
        this.countryId = countryId;
    }

    public long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(long languageId) {
        this.languageId = languageId;
    }
}
