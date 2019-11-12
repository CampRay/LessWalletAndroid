package com.campray.lesswalletandroid.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.adapter.base.BaseRecyclerAdapter;
import com.campray.lesswalletandroid.adapter.base.BaseRecyclerHolder;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.db.entity.Product;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.CouponModel;
import com.campray.lesswalletandroid.model.ProductModel;
import com.campray.lesswalletandroid.ui.CouponSendActivity;
import com.campray.lesswalletandroid.ui.CouponUseActivity;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.ResourcesUtils;
import com.campray.lesswalletandroid.view.CustomDialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * 使用进一步封装的Coupon
 * @author Phills
 */
public class ProductAdapter extends BaseRecyclerAdapter<Product> {

    public ProductAdapter(Context context, int layoutResourceId, Collection<Product> datas) {
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
            Product item=getItem(index);
            if(item!=null&&item.getProductId().equals(targetId)) {
                position = index;
                break;
            }
        }
        return position;
    }

    /**
     * 绑定显示卡卷信息到列表view对象
     * @param holder
     * @param product
     * @param position
     */
    @Override
    public void bindView(final BaseRecyclerHolder holder, Product product, final int position) {
        if (product != null && product.getCoupons().size() > 0) {
            final Coupon coupon = product.getCoupons().get(0);
            String expired = (TextUtils.isEmpty(coupon.getStartTimeLocal()) ? "" : coupon.getStartTimeLocal()) + (TextUtils.isEmpty(coupon.getEndTimeLocal()) ? "" : " - " + coupon.getEndTimeLocal());
            holder.setText(R.id.tv_title, coupon.getProduct().getTitle());
            holder.setText(R.id.tv_desc, coupon.getProduct().getTitle());
            holder.setText(R.id.tv_merchant, coupon.getProduct().getMerchant().getName());
            holder.setText(R.id.tv_expired, expired);
            holder.setText(R.id.tv_number, coupon.getProduct().getNumPrefix()+coupon.getCid().substring(10));

            if (coupon.getCouponStyle() != null) {
                holder.setText(R.id.tv_benefit, "");
                holder.setBackgroundColor(R.id.gl_coupon_top, coupon.getCouponStyle().getBgColor());
                if (!TextUtils.isEmpty(coupon.getCouponStyle().getShadingUrl())) {
                    holder.setImageView(R.id.iv_coupon_shading, coupon.getCouponStyle().getShadingUrl(), 0);
                } else {
                    holder.setVisible(R.id.iv_coupon_shading, View.GONE);
                }
                if (!TextUtils.isEmpty(coupon.getCouponStyle().getPictureUrl())) {
                    holder.setImageView(R.id.iv_coupon_img, coupon.getCouponStyle().getPictureUrl(), 0);
                } else {
                    holder.setVisible(R.id.iv_coupon_img, View.GONE);
                }
                if (!TextUtils.isEmpty(coupon.getCouponStyle().getLogoUrl())) {
                    holder.setImageView(R.id.iv_coupon_logo, coupon.getCouponStyle().getLogoUrl(), 0);
                } else {
                    holder.setVisible(R.id.iv_coupon_logo, View.GONE);
                }
            }
            int count = product.getCoupons().size();
            if (count > 1) {
                holder.setText(R.id.tv_coupon_count, count + "");
            }
            holder.setOnClickListener(R.id.gl_coupon_top, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.setVisibleToggle(R.id.ll_desc);
                }
            });
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
                    intent.setClass(ProductAdapter.this.context, CouponUseActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("coupon_id", coupon.getOrderId());
                    intent.putExtra(ProductAdapter.this.context.getPackageName(), bundle);
                    ProductAdapter.this.context.startActivity(intent);
                }
            });
            //设置送出事件
            holder.setOnClickListener(R.id.btn_give, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(ProductAdapter.this.context, CouponSendActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("coupon_id", coupon.getOrderId());
                    intent.putExtra(ProductAdapter.this.context.getPackageName(), bundle);
                    ProductAdapter.this.context.startActivity(intent);
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
                new CustomDialog.Builder(ProductAdapter.this.context);
        normalDialog.setIcon(R.mipmap.face_sad);
        //normalDialog.setTitle("Title");
        normalDialog.setMessage("Do you wish to discard this coupon?");
        normalDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        final Product delProduct=ProductAdapter.this.getItem(position);
                        if(delProduct!=null) {
                            List<Long> idList = new ArrayList<Long>();
                            for (Coupon item : delProduct.getCoupons()) {
                                idList.add(item.getOrderId());
                            }
                            String idsStr = TextUtils.join(",", idList.toArray());
                            CouponModel.getInstance().delCouponsFromServer(idsStr, new OperationListener<Coupon>() {
                                @Override
                                public void done(Coupon obj, AppException exception) {
                                    if (exception == null) {
                                        ProductModel.getInstance().deleteProductById(delProduct.getProductId());
                                        toast("This coupon has been deleted discarded successfully.");
                                        holder.setVisibleToggle(R.id.ll_desc);
                                        int startPos = holder.getAdapterPosition();
                                        ProductAdapter.this.remove(startPos);
                                        dialog.cancel();
                                    } else {

                                        toast("Failed to discard the coupon, Error Message: " + ResourcesUtils.getString(ProductAdapter.this.context, exception.getErrorCode()));
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