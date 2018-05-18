package com.campray.lesswalletandroid.util;

import android.content.Context;
import android.text.TextUtils;

/**
 * 应用自定义的异常类
 * Created by Phills on 10/27/2017.
 */
public class AppException extends Exception {
    private String code="E_1000";
    public AppException() {
    }

    public AppException(Throwable throwable) {
        super(throwable);
    }

    public AppException(String exceptionCode) {
        this.code = exceptionCode;
    }

    public AppException(String exceptionCode, String detailMessage) {
        super(detailMessage);
        this.code = exceptionCode;
    }

    public AppException(String exceptionCode, String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.code = exceptionCode;
    }

    public AppException(String exceptionCode, Throwable throwable) {
        super(throwable);
        this.code = exceptionCode;
    }

    public String getErrorCode() {
        return this.code;
    }

    public String toString() {
        return "errorCode:" + this.code + ",errorMsg:" + this.getMessage();
    }
    //返回错误码对应的多语言内容
    public String toString(Context context) {
        if(TextUtils.isEmpty(this.getMessage())){
            return context.getResources().getString(ResourcesUtils.getStringId(context,this.code));
        }
        else{
            return context.getResources().getString(ResourcesUtils.getStringId(context,this.code))+","+context.getResources().getString(ResourcesUtils.getStringId(context,"E_0000"))+": "+this.getMessage();
        }
    }
}
