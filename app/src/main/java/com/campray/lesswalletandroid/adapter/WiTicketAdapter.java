package com.campray.lesswalletandroid.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.adapter.base.BaseRecyclerAdapter;
import com.campray.lesswalletandroid.adapter.base.BaseRecyclerHolder;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.CouponModel;
import com.campray.lesswalletandroid.model.ProductModel;
import com.campray.lesswalletandroid.ui.CouponSendActivity;
import com.campray.lesswalletandroid.ui.CouponUseActivity;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.ResourcesUtils;
import com.campray.lesswalletandroid.view.CustomDialog;
import com.campray.lesswalletandroid.view.InnerCornerView;

import java.util.Collection;


/**
 * 使用进一步封装的Coupon
 * @author Phills
 */
public class WiTicketAdapter extends BaseRecyclerAdapter<Coupon> {
    public WiTicketAdapter(Context context, int layoutResourceId, Collection<Coupon> datas) {
        super(context,layoutResourceId,datas);
    }

    /**
     * 根据指定卡卷类型和卡卷id，获取卡卷在列表中的位置
     * @param targetId
     * @return
     */
    public int findPosition(String targetId) {
        int index = this.getCount();
        int position = -1;
        while(index-- > 0) {
            if((getItem(index)).getProductId().equals(targetId)) {
                position = index;
                break;
            }
        }
        return position;
    }

    /**
     * 绑定显示卡卷信息到列表view对象
     * @param holder
     * @param coupon
     * @param position
     */
    @Override
    public void bindView(final BaseRecyclerHolder holder, final Coupon coupon, final int position) {
        if (coupon != null) {
            String expired = (TextUtils.isEmpty(coupon.getStartTimeLocal()) ? "" : coupon.getStartTimeLocal()) + (TextUtils.isEmpty(coupon.getEndTimeLocal()) ? "" : " - " + coupon.getEndTimeLocal());
            holder.setText(R.id.tv_title, coupon.getProduct().getTitle());
            holder.setText(R.id.tv_desc, coupon.getProduct().getTitle());
            //holder.setText(R.id.tv_merchant, coupon.getProduct().getMerchant().getName());
            holder.setText(R.id.tv_date, coupon.getStartTimeLocal());
            holder.setText(R.id.tv_number, coupon.getProduct().getNumPrefix()+coupon.getCid().substring(10));

            if (coupon.getCouponStyle() != null) {
                //holder.setText(R.id.tv_price, "");
                int topColor=Color.parseColor(coupon.getCouponStyle().getBgColor());
                InnerCornerView leftInnerCornerView=(InnerCornerView)holder.getView(R.id.icv_tleft);
                leftInnerCornerView.setCornerColor(topColor);
                InnerCornerView rightInnerCornerView=(InnerCornerView)holder.getView(R.id.icv_tright);
                rightInnerCornerView.setCornerColor(topColor);
                View topLayout=holder.getView(R.id.ll_top_layout);
                GradientDrawable drawable = (GradientDrawable) topLayout.getBackground();
                drawable.setColor(topColor);
//                holder.setBackgroundColor(R.id.top_layout, topColor);
                holder.setBackgroundColor(R.id.tv_top_bg, topColor);

                //海报图片
                if (!TextUtils.isEmpty(coupon.getCouponStyle().getPictureUrl())) {
                    holder.setImageView(R.id.iv_ticket_poster, coupon.getCouponStyle().getPictureUrl(), 0);
                } else {
                    holder.setVisible(R.id.iv_ticket_poster, View.GONE);
                }

            }

//            holder.setOnClickListener(R.id.top_layout, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    holder.setVisibleToggle(R.id.ll_desc);
//                }
//            });
            //设置点赞事件
            holder.setOnClickListener(R.id.iv_love, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.setBackgroundResource(R.id.iv_love, R.mipmap.heart_hover);
                }
            });
            //点击使用Coupon按钮
            holder.setOnClickListener(R.id.btn_use, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(WiTicketAdapter.this.context, CouponUseActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("coupon_id", coupon.getOrderId());
                    intent.putExtra(WiTicketAdapter.this.context.getPackageName(), bundle);
                    WiTicketAdapter.this.context.startActivity(intent);
                }
            });
            //设置送出事件
            holder.setOnClickListener(R.id.btn_give, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(WiTicketAdapter.this.context, CouponSendActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("coupon_id", coupon.getOrderId());
                    intent.putExtra(WiTicketAdapter.this.context.getPackageName(), bundle);
                    WiTicketAdapter.this.context.startActivity(intent);
                }
            });
            //设置移除事件
            holder.setOnClickListener(R.id.btn_discard, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //显示移除对话框
                    showDialog(holder,position);
                }
            });

        }
    }

    /**
     * 显示移除对话框
     * @param holder
     * @param position
     */
    private void showDialog(final BaseRecyclerHolder holder,final int position){
        final CustomDialog.Builder normalDialog =
                new CustomDialog.Builder(WiTicketAdapter.this.context);
        normalDialog.setIcon(R.mipmap.face_sad);
        //normalDialog.setTitle("Title");
        normalDialog.setMessage("Do you wish to discard this coupon?");
        normalDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        final Coupon delCoupon=WiTicketAdapter.this.getItem(position);
                        CouponModel.getInstance().delCouponsFromServer(delCoupon.getOrderId()+"", new OperationListener<Coupon>() {
                            @Override
                            public void done(Coupon obj, AppException exception) {
                                if(exception==null) {
                                    ProductModel.getInstance().deleteProductById(delCoupon.getProductId());
                                    toast("This coupon has been deleted discarded successfully.");
                                    holder.setVisibleToggle(R.id.ll_desc);
                                    int startPos = holder.getAdapterPosition();
                                    WiTicketAdapter.this.remove(startPos);
                                    dialog.cancel();
                                }
                                else{
                                    toast("Failed to discard the coupon, Error Message: "+ ResourcesUtils.getString(WiTicketAdapter.this.context,exception.getErrorCode()));
                                }
                            }
                        });
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