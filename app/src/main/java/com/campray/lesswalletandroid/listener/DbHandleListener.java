package com.campray.lesswalletandroid.listener;

import com.campray.lesswalletandroid.util.AppException;

/**
 * Created by Phills on 9/5/2017.
 */

public abstract class DbHandleListener<T> implements IListener {
    public abstract void done(T obj, AppException exception);
}
