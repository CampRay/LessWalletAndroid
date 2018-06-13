package com.campray.lesswalletandroid.adapter;


import android.content.Context;
import android.content.DialogInterface;

import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.adapter.base.BaseRecyclerAdapter;
import com.campray.lesswalletandroid.adapter.base.BaseRecyclerHolder;
import com.campray.lesswalletandroid.adapter.base.IMutlipleItem;
import com.campray.lesswalletandroid.db.entity.History;
import com.campray.lesswalletandroid.model.HistoryModel;
import com.campray.lesswalletandroid.util.ResourcesUtils;
import com.campray.lesswalletandroid.view.CustomDialog;

import java.util.Collection;


/**
 * 列表控件的数据适配器类(历史消息)
 * @author Phills
 */
public class HistoryAdapter extends BaseRecyclerAdapter<History> {
    public HistoryAdapter(Context context, IMutlipleItem<History> items, Collection<History> datas) {
        super(context,items,datas);
    }

    /**
     * 根据指定消息id，获取列表中对应消息的位置
     * @param targetId
     * @return
     */
    public int findPosition(String targetId) {
        int index = this.getCount();
        int position = -1;
        while(index-- > 0) {
            if((getItem(index)).getId().equals(targetId)) {
                position = index;
                break;
            }
        }
        return position;
    }

    /**
     * 绑定显示消息到列表view对象
     * @param holder
     * @param history
     * @param position
     */
    @Override
    public void bindView(final BaseRecyclerHolder holder, History history, final int position) {
        if (history != null) {
            String title = "";
            String desc ="";
            switch (history.getType()) {
                case 137://发送Coupon给好友成功消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_coupon_sent"));
                    desc = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_coupon_sent_text"));

                    break;
                case 138://收到好友送来Coupon的消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_coupon_received"));
                    desc = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_coupon_received_text"));
                    break;
                default:
                    title = "";
            }
            holder.setText(R.id.tv_title, title);
            holder.setText(R.id.tv_desc, desc);
            holder.setText(R.id.tv_time, history.getCreatedTimeLocal());

        }
    }

    /**
     * 显示移除对话框
     * @param holder
     * @param position
     */
    private void showDialog(final BaseRecyclerHolder holder,final int position){
        final CustomDialog.Builder normalDialog =
                new CustomDialog.Builder(HistoryAdapter.this.context);
        normalDialog.setIcon(R.mipmap.face_sad);
        //normalDialog.setTitle("Title");
        normalDialog.setMessage("Do you want to remove this message?");
        normalDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        final History delHistory=HistoryAdapter.this.getItem(position);
                        HistoryModel.getInstance().delHistory(delHistory);
                    }
                });
        normalDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        // 显示
        normalDialog.show();
    }

}