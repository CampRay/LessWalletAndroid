package com.campray.lesswalletandroid.adapter;


import android.content.Context;
import android.content.DialogInterface;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.adapter.base.BaseRecyclerAdapter;
import com.campray.lesswalletandroid.adapter.base.BaseRecyclerHolder;
import com.campray.lesswalletandroid.adapter.base.ILayoutItem;
import com.campray.lesswalletandroid.db.entity.History;
import com.campray.lesswalletandroid.db.entity.User;
import com.campray.lesswalletandroid.model.HistoryModel;
import com.campray.lesswalletandroid.util.ResourcesUtils;
import com.campray.lesswalletandroid.view.CustomDialog;

import java.util.Collection;


/**
 * 列表控件的数据适配器类(历史消息)
 * @author Phills
 */
public class HistoryAdapter extends BaseRecyclerAdapter<History> {
    public HistoryAdapter(Context context, int layoutResourceId, Collection<History> datas) {
        super(context,layoutResourceId,datas);
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
            String[] strArr=history.getComment().split(":");
            final User user= LessWalletApplication.INSTANCE().getAccount();
            final String fmt=user.getCurrenty().getCustomFormatting().replace("0.00","");
            switch (history.getType()) {
                case 137://发送Coupon给好友成功消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_coupon_sent"));
                    desc = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_coupon_sent_text"));
                    break;
                case 138://收到好友送来Coupon的消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_coupon_received"));
                    desc = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_coupon_received_text"));
                    break;
                case 148://积分增加成功消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_point_add"));
                    desc = String.format(this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_point_add_text")),strArr[1].replace("-",""));
                    break;
                case 149://积分减少的消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_point_redeemed"));
                    desc = String.format(this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_point_redeemed_text")),strArr[1].replace("-",""));
                    break;
                case 150://服务次数增加成功消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_service_add"));
                    desc = String.format(this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_service_add_text")),strArr[2].replace("-",""));

                    break;
                case 151://服务次数减少的消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_service_redeemed"));
                    desc = String.format(this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_service_redeemed_text")),strArr[2].replace("-",""));
                    break;
                case 152://金额增加的消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_cash_add"));
                    desc = String.format(this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_cash_add_text")),fmt+strArr[2].replace("-",""));

                    break;
                case 153://金额减少的消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_cash_redeemed"));
                    desc = String.format(this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_cash_redeemed_text")),fmt+strArr[2].replace("-",""));
                    break;
                case 154://收到商户赠送的Coupon消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_coupon_received"));
                    desc = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_coupon_received_text"));

                    break;
                case 155://收到商户赠送的Card消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_card_received"));
                    desc = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_card_received_text"));
                    break;
                case 156://Coupon被用掉的消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_coupon_used"));
                    desc = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_coupon_used_text"));
                    break;
                case 157://收到商家赠送Card消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_card_received"));
                    desc = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_card_received_text"));
                    break;
                case 158://Coupon使用移除消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_coupon_used"));
                    desc = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_coupon_used_text"));
                    break;
                case 159://购买次数增加消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_buy_add"));
                    desc = String.format(this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_cash_add_text")),strArr[2].replace("-",""));
                    break;
                case 160://购买次数减少消息
                    title = this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_buy_redeemed"));
                    desc = String.format(this.context.getResources().getString(ResourcesUtils.getStringId(this.context, "notification_user_cash_redeemed_text")),strArr[2].replace("-",""));
                    break;
                default:
                    break;
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