package com.campray.lesswalletandroid.adapter;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.adapter.base.BaseRecyclerAdapter;
import com.campray.lesswalletandroid.adapter.base.BaseRecyclerHolder;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.db.entity.CouponStyle;
import com.campray.lesswalletandroid.db.entity.Product;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.CouponModel;
import com.campray.lesswalletandroid.model.ProductModel;
import com.campray.lesswalletandroid.ui.CardActivity;
import com.campray.lesswalletandroid.ui.CardUseActivity;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.ResourcesUtils;
import com.campray.lesswalletandroid.view.CustomDialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * 使用进一步封装的Card
 * @author Phills
 */
public class WiCardAdapter extends BaseRecyclerAdapter<Coupon> {
    private int screenWidth =0;
    public WiCardAdapter(Context context, int layoutResourceId, Collection<Coupon> datas) {
        super(context,layoutResourceId,datas);
        DisplayMetrics dm = this.context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
    }

    /**
     * 根据指定卡卷id，获取卡卷在列表中的位置
     * @param targetId
     * @return
     */
    public int findPosition(long targetId) {
        int index = this.getCount();
        int position = -1;
        while(index-- > 0) {
            if((getItem(index)).getOrderId()==targetId) {
                position = index;
                break;
            }
        }
        return position;
    }

    @Override
    public long getItemId(int position) {
        Coupon coupon=getItem(position);
        if (coupon== null) {
            return super.getItemId(position);
        }
        else{
            return coupon.getOrderId();
        }
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
            RelativeLayout cardView = (RelativeLayout)holder.getView(R.id.layout_card);
            ViewGroup.LayoutParams params= (ViewGroup.LayoutParams) cardView.getLayoutParams();
            //获取当前控件的布局对象
            params.width=screenWidth*6/7;//设置当前控件显示的宽度
            cardView.setLayoutParams(params);
            holder.setTag(R.id.layout_card,coupon.getOrderId());

            final Product product = coupon.getProduct();
            holder.setText(R.id.tv_title, product.getTitle());
            holder.setText(R.id.tv_shortdesc, product.getShortDesc());
            holder.setText(R.id.tv_merchant, product.getMerchant().getName());
            holder.setText(R.id.tv_number, coupon.getCid());
            if (product.getPrice() > 0) {
                holder.setText(R.id.tv_price, product.getPriceStr());
            } else {
                holder.setText(R.id.tv_price, this.context.getResources().getString(R.string.coupon_free));
            }

            CouponStyle couponStyle = coupon.getCouponStyle();
            if (couponStyle != null) {
                if (couponStyle.getValidityDay() > 0) {
                    holder.setText(R.id.tv_validity, couponStyle.getValidityDay() + this.context.getResources().getString(R.string.coupon_validity_day));
                } else if (couponStyle.getValidityMonth() > 0) {
                    holder.setText(R.id.tv_validity, couponStyle.getValidityMonth() + this.context.getResources().getString(R.string.coupon_validity_month));
                } else if (couponStyle.getValidityYear() > 0) {
                    holder.setText(R.id.tv_validity, couponStyle.getValidityYear() + this.context.getResources().getString(R.string.coupon_validity_year));
                } else {
                    holder.setText(R.id.tv_validity, this.context.getResources().getString(R.string.coupon_validity_nolimit));
                }
                holder.setText(R.id.tv_class, couponStyle.getCardLevel()+"");

                //设置背景色
                View view = holder.getView(R.id.rl_card_top);
                GradientDrawable gd = (GradientDrawable) view.getBackground();
                gd.setColor(Color.parseColor(couponStyle.getBgColor()));

                View backview = holder.getView(R.id.rl_card_back);
                GradientDrawable backgd = (GradientDrawable) backview.getBackground();
                backgd.setColor(Color.parseColor("#f2e4cf"));
                if (!TextUtils.isEmpty(couponStyle.getShadingUrl())) {
                    holder.setImageView(R.id.iv_card_shading, couponStyle.getShadingUrl(), 0);
                } else {
                    holder.setVisible(R.id.iv_card_shading, View.GONE);
                }
                if (!TextUtils.isEmpty(couponStyle.getPictureUrl())) {
                    holder.setImageView(R.id.iv_card_img, couponStyle.getPictureUrl(), 0);
                } else {
                    holder.setVisible(R.id.iv_card_img, View.GONE);
                }
                if (!TextUtils.isEmpty(couponStyle.getLogoUrl())) {
                    holder.setImageView(R.id.iv_card_logo, couponStyle.getLogoUrl(), 0);
                } else {
                    holder.setVisible(R.id.iv_card_logo, View.GONE);
                }
            }

            //coard的对象点击事件,处理实现翻转动画
            holder.setOnClickListener(R.id.ll_card_front, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout cardBack = (LinearLayout) holder.getView(R.id.ll_card_back);
                    flipAnimatorXViewShow(v, cardBack, 200);
                }
            });
            holder.setOnClickListener(R.id.ll_card_back, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout cardFront=(LinearLayout)holder.getView(R.id.ll_card_front);
                    flipAnimatorXViewShow(v,cardFront,200);
                }
            });
            holder.setOnClickListener(R.id.tv_more, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(WiCardAdapter.this.context, CardUseActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("coupon_id", coupon.getOrderId());
                    intent.putExtra(WiCardAdapter.this.context.getPackageName(), bundle);
                    WiCardAdapter.this.context.startActivity(intent);
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
                new CustomDialog.Builder(WiCardAdapter.this.context);
        normalDialog.setIcon(R.mipmap.face_sad);
        //normalDialog.setTitle("Title");
        normalDialog.setMessage("Do you wish to discard this card?");
        normalDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        List<Long> idList=new ArrayList<Long>();
                        final Coupon delCoupon=WiCardAdapter.this.getItem(position);
                        idList.add(delCoupon.getOrderId());
                        String idsStr=TextUtils.join(",",idList.toArray());
                        CouponModel.getInstance().delCouponsFromServer(idsStr, new OperationListener<Coupon>() {
                            @Override
                            public void done(Coupon obj, AppException exception) {
                                if(exception==null) {
                                    ProductModel.getInstance().deleteProductById(delCoupon.getProductId());
                                    WiCardAdapter.this.remove(position);
                                    dialog.cancel();
                                    toast("This card has been discarded successfully.");
                                }
                                else{
                                    toast("Failed to discard the card, Error Message: "+ ResourcesUtils.getString(WiCardAdapter.this.context,exception.getErrorCode()));
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

    /**
     * 翻转动画
     * @param oldView
     * @param newView
     * @param time
     */
    private void flipAnimatorXViewShow(final View oldView, final View newView, final long time) {

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(oldView, "rotationY", 0, 90);
        final ObjectAnimator animator2 = ObjectAnimator.ofFloat(newView, "rotationY", -90, 0);

        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setCameraDistance(oldView);
                setCameraDistance(newView);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                oldView.setVisibility(View.GONE);
                animator2.setDuration(time).start();
                newView.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        animator1.setDuration(time).start();
    }

    /**
     * 改变视角距离, 要不然翻转时视图会拉伸很长
     */
    private void setCameraDistance(View view) {
        int distance = 16000;
        float scale = this.context.getResources().getDisplayMetrics().density * distance;
        view.setCameraDistance(scale);
    }

}