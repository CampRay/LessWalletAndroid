package com.campray.lesswalletandroid.event;

/**
 * Created by Administrator on 2016/4/28.
 */
public class RefreshEvent {
    private long id;
    public RefreshEvent(){}
    public RefreshEvent(long id){
        this.id=id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
