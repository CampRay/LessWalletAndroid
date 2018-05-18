package com.campray.lesswalletandroid.listener;

import android.os.Handler;
import android.os.Looper;

import com.campray.lesswalletandroid.util.AppException;

/**
 * Created by Phills on 6/7/2017.
 * UI操作处理侦听器
 */

public abstract class OperationListener<T> implements IListener{
    public OperationListener() {
    }

    public void internalStart(final T obj) {
        if(Looper.myLooper() != Looper.getMainLooper()) {
            (new Handler(Looper.getMainLooper())).post(new Runnable() {
                @Override
                public void run() {
                    OperationListener.this.postStart(obj);
                }
            });
        } else {
            this.postStart(obj);
        }
    }

    public void internalFinish() {
        if(Looper.myLooper() != Looper.getMainLooper()) {
            (new Handler(Looper.getMainLooper())).post(new Runnable() {
                @Override
                public void run() {
                    OperationListener.this.postFinish();
                }
            });
        } else {
            this.postFinish();
        }
    }

    public void internalProgress(final int index) {
        if(Looper.myLooper() != Looper.getMainLooper()) {
            (new Handler(Looper.getMainLooper())).post(new Runnable() {
                @Override
                public void run() {
                    OperationListener.this.postProgress(index);
                }
            });
        } else {
            this.postProgress(index);
        }
    }

    /**
     * 操作开始时处理方法
     * @param obj
     */
    public void postStart(T obj) {
    }

    /**
     * 操作完成后处理方法
     */
    public void postFinish() {
    }

    /**
     * 操作进度处理方法
     * @param index
     */
    public void postProgress(int index) {
    }

    public abstract void done(T obj, AppException exception);
}