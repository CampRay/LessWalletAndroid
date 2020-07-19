package com.campray.lesswalletandroid.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.adapter.base.BaseRecyclerAdapter;
import com.campray.lesswalletandroid.adapter.base.BaseRecyclerHolder;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.db.entity.CouponStyle;
import com.campray.lesswalletandroid.db.entity.Merchant;
import com.campray.lesswalletandroid.db.entity.Product;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.CouponModel;
import com.campray.lesswalletandroid.model.MerchantModel;
import com.campray.lesswalletandroid.model.ProductModel;
import com.campray.lesswalletandroid.ui.CouponSendActivity;
import com.campray.lesswalletandroid.ui.CouponUseActivity;
import com.campray.lesswalletandroid.ui.TicketSendActivity;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.ResourcesUtils;
import com.campray.lesswalletandroid.view.CustomDialog;
import com.campray.lesswalletandroid.view.InnerCornerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * 使用进一步封装的Coupon
 * @author Phills
 */
public class WiTicketAdapter extends BaseRecyclerAdapter<Product> {
    public WiTicketAdapter(Context context, int layoutResourceId, Collection<Product> datas) {
        super(context,layoutResourceId,datas);
    }

    /**
     * 根据指定卡卷id，获取卡卷在列表中的位置
     * @param targetId
     * @return
     */
    public int findPosition(String targetId) {
        int index = this.getCount();
        int position = -1;
        while(index-- > 0) {
            Product item=getItem(index);
            if(item!=null&&item.getProductId().equals(targetId)) {
                position = index;
                break;
            }
        }
        return position;
    }

    /**
     * 重载获取当前位置的列表对象的自定义的视图类型
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        Product product=getItem(position);
        int count = 0;
        for (Coupon citem:product.getCoupons()) {
            count+=citem.getQuantity();
        }
        if(product!=null&&count>1){
            return 7;
        }else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public BaseRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch(viewType){
            case TYPE_FIRST_NULL:
            case TYPE_EMPTY:
                layoutId= R.layout.item_empty;
                break;
            case TYPE_LAST_NULL:
                layoutId= R.layout.item_progressbar;
                break;
            case 7:
                layoutId= R.layout.item_ticket_muti;
                break;
            default:
                layoutId= R.layout.item_ticket;
                break;
        }
        return super.onCreateViewHolder(parent,viewType);
    }

    /**
     * 绑定显示卡卷信息到列表view对象
     * @param holder
     * @param product
     * @param position
     */
    @Override
    public void bindView(final BaseRecyclerHolder holder, final Product product, final int position) {
        if (product != null && product.getCoupons().size() > 0) {
            final Coupon coupon = product.getCoupons().get(0);
            String expired = (TextUtils.isEmpty(coupon.getStartTimeLocal()) ? "" : coupon.getStartTimeLocal()) + (TextUtils.isEmpty(coupon.getEndTimeLocal()) ? " -" : " - " + coupon.getEndTimeLocal());
            holder.setText(R.id.tv_title, product.getTitle());
            holder.setText(R.id.tv_shortdesc, product.getShortDesc());
            holder.setText(R.id.tv_merchant, product.getMerchant().getName());
            holder.setText(R.id.tv_expired, expired);
            holder.setText(R.id.tv_number, coupon.getCid());
            holder.setText(R.id.tv_uservalue, Html.fromHtml(coupon.getUserValuesInfo()).toString());
            //不允许转送
            if(!product.getCanBeForwarded()) {
                holder.setVisible(R.id.btn_give, View.GONE);
            }

            if (coupon.getPrice() > 0) {
                holder.setText(R.id.tv_price, coupon.getPriceStr());
            } else {
                holder.setText(R.id.tv_price, this.context.getResources().getString(R.string.coupon_free));
            }

            CouponStyle couponStyle = coupon.getCouponStyle();
            if (couponStyle != null) {
                //设置背景色
                try {
                    int topColor=Color.parseColor(couponStyle.getBgColor());
                    View view = holder.getView(R.id.ll_top_layout);
                    LayerDrawable drawable = (LayerDrawable) view.getBackground();
                    GradientDrawable item = (GradientDrawable)drawable.findDrawableByLayerId(R.id.top_item);
                    item.setColor(topColor);
                    holder.setBackgroundColor(R.id.tv_top_bg, topColor);
                    InnerCornerView leftCorner = (InnerCornerView)holder.getView(R.id.icv_tleft);
                    InnerCornerView rightCorner = (InnerCornerView)holder.getView(R.id.icv_tright);
                    leftCorner.setCornerColor(topColor);
                    rightCorner.setCornerColor(topColor);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                if (!TextUtils.isEmpty(couponStyle.getPictureUrl())) {
                    holder.setImageView(R.id.iv_ticket_img, couponStyle.getPictureUrl(), 0);
                } else {
                    if (!TextUtils.isEmpty(couponStyle.getLogoUrl())) {
                        holder.setImageView(R.id.iv_ticket_img, couponStyle.getLogoUrl(), 0);
                    } else {
                        holder.setVisible(R.id.iv_ticket_img, View.GONE);
                    }
                }

            }

            if(layoutId==R.layout.item_ticket_muti) {
                int count = 0;
                for (Coupon citem:product.getCoupons()) {
                    count+=citem.getQuantity();
                }
                holder.setText(R.id.tv_coupon_count, count + "");
            }

            holder.setOnClickListener(R.id.ll_top_layout, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.setVisibleToggle(R.id.ll_form);
                }
            });

            if(LessWalletApplication.INSTANCE().getAttentions().contains(product.getMerchantId())) {
                holder.setBackgroundResource(R.id.iv_love, R.mipmap.heart_hover);
            }
            else{
                holder.setBackgroundResource(R.id.iv_love, R.mipmap.heart);
            }


            //设置点赞事件
            holder.setOnClickListener(R.id.iv_love, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //判断是否已经添加关注
                    if(LessWalletApplication.INSTANCE().getAttentions().contains(product.getMerchantId())) {
                        AlertDialog dialog = new AlertDialog.Builder(WiTicketAdapter.this.context)
                                .setTitle("取消收藏此商家")//设置对话框的标题
                                .setMessage("确定要取消收藏当前商家吗？取消收藏此商家后您将会不会接收到此商家发布的最新卡卷的消息通知！")//设置对话框的内容
                                //设置对话框的按钮
                                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton(R.string.button_confirm, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        MerchantModel.getInstance().addAttention(product.getMerchantId(), new OperationListener<Merchant>() {
                                            @Override
                                            public void done(Merchant obj, AppException exception) {
                                                holder.setBackgroundResource(R.id.iv_love, R.mipmap.heart);
                                            }
                                        });
                                        dialog.dismiss();
                                    }
                                }).create();
                        dialog.show();
                    }
                    else{
                        AlertDialog dialog = new AlertDialog.Builder(WiTicketAdapter.this.context)
                                .setTitle("收藏此商家")//设置对话框的标题
                                .setMessage("确定要收藏当前商家吗？收藏此商家后您将会接收到此商家发布的最新卡卷的消息通知！")//设置对话框的内容
                                //设置对话框的按钮
                                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton(R.string.button_confirm, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        MerchantModel.getInstance().addAttention(product.getMerchantId(), new OperationListener<Merchant>() {
                                            @Override
                                            public void done(Merchant obj, AppException exception) {
                                                holder.setBackgroundResource(R.id.iv_love, R.mipmap.heart_hover);
                                            }
                                        });
                                        dialog.dismiss();
                                    }
                                }).create();
                        dialog.show();
                    }
                }
            });

            final long orderId = coupon.getOrderId();
            //点击使用Coupon按钮
            holder.setOnClickListener(R.id.btn_use, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(WiTicketAdapter.this.context, CouponUseActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("coupon_id", orderId);
                    intent.putExtra(WiTicketAdapter.this.context.getPackageName(), bundle);
                    WiTicketAdapter.this.context.startActivity(intent);
                }
            });
            //设置送出事件
            holder.setOnClickListener(R.id.btn_give, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(WiTicketAdapter.this.context, TicketSendActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("coupon_id", orderId);
                    intent.putExtra(WiTicketAdapter.this.context.getPackageName(), bundle);
                    WiTicketAdapter.this.context.startActivity(intent);
                }
            });
            //设置移除事件
            holder.setOnClickListener(R.id.btn_discard, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //显示移除对话框
                    showDialog(holder, position);
                }
            });

        }
        else{
            if(product.getCoupons().size()==0){
                ProductModel.getInstance().deleteProductById(product.getProductId());
                remove(position);
            }
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
        normalDialog.setMessage("Do you wish to discard this ticket?");
        normalDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        List<Long> idList=new ArrayList<Long>();
                        final Product delProduct=WiTicketAdapter.this.getItem(position);
                        if(delProduct!=null) {
                            for (Coupon item : delProduct.getCoupons()) {
                                idList.add(item.getOrderId());
                            }
                            //final int couponSize=idList.size();
                            String idsStr = TextUtils.join(",", idList.toArray());

                            CouponModel.getInstance().delCouponsFromServer(idsStr, new OperationListener<Coupon>() {
                                @Override
                                public void done(Coupon obj, AppException exception) {
                                    if (exception == null) {
                                        WiTicketAdapter.this.remove(position);
                                        dialog.cancel();
                                        toast("This ticket has been discarded successfully.");
                                    } else {
                                        toast("Failed to discard the ticket, Error Message: " + ResourcesUtils.getString(WiTicketAdapter.this.context, exception.getErrorCode()));
                                    }
                                }
                            });
                        }
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