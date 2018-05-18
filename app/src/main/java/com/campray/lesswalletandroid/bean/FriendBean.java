package com.campray.lesswalletandroid.bean;

/**好友表
 * @author Phills
 * @project Friend
 * @date 2017-10-26
 */
public class FriendBean{

    private UserBean user;
    private UserBean friendUser;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public UserBean getFriendUser() {
        return friendUser;
    }

    public void setFriendUser(UserBean friendUser) {
        this.friendUser = friendUser;
    }
}
