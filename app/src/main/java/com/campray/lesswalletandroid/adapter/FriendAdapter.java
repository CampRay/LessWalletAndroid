package com.campray.lesswalletandroid.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.Friend;
import com.campray.lesswalletandroid.util.ImageLoaderFactory;

import java.util.List;

import butterknife.BindView;

/**
 * 此类没有使用
 * Created by Phills on 4/11/2018.
 */

public class FriendAdapter extends ArrayAdapter {
    private final int resourceId;
    /**
     * 重写ArrayAdapter构造函数
     * @param context
     * @param resource
     * @param objects
     */
    public FriendAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Friend> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Friend friend = (Friend) getItem(position); // 获取当前列表项的Friend数据实例
        View view = LayoutInflater.from(this.getContext()).inflate(resourceId, null);//实例化一个对象
        ImageView avatarImage = (ImageView) view.findViewById(R.id.iv_avatar);//获取该布局内的图片视图
        TextView usernameView = (TextView) view.findViewById(R.id.tv_name);//获取该布局内的文本视图
        TextView emailView = (TextView) view.findViewById(R.id.tv_email);
        TextView mobileView = (TextView) view.findViewById(R.id.tv_mobile);
        //为图片视图设置好友头像，如果没有则显示默认头像图片
        ImageLoaderFactory.getLoader().load(avatarImage,friend.getAvatorPath(),R.mipmap.icon_account,null);
        usernameView.setText(friend.getUserName());//为文本视图设置文本内容
        emailView.setText(friend.getEmail());
        mobileView.setText(friend.getMobile());
        return view;
    }
}
