package com.campray.lesswalletandroid.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.db.entity.Slider;
import com.campray.lesswalletandroid.model.SliderModel;
import com.campray.lesswalletandroid.ui.base.MenuActivity;
import com.campray.lesswalletandroid.util.SliderImageLoader;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Phills on 11/2/2017.
 */

public class MainActivity extends MenuActivity {
    @BindView(R.id.iv_coupon)
    ImageView iv_coupon;
    @BindView(R.id.iv_card)
    ImageView iv_card;
    @BindView(R.id.iv_ticket)
    ImageView iv_ticket;

    @BindView(R.id.iv_website)
    ImageView iv_website;
    @BindView(R.id.banner)
    Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initPermissions();

        //设置图片加载器
        banner.setImageLoader(new SliderImageLoader());
        List<Slider> list=SliderModel.getInstance().getAllSliders();
        List<String> images=new ArrayList<String>();
        List<String> titles=new ArrayList<String>();
        for (Slider slider:list) {
            if(!TextUtils.isEmpty(slider.getPicUrl())) {
                images.add(slider.getPicUrl());
                titles.add(slider.getText());
            }
        }
        banner.setImages(images);
        //banner.setBannerTitles(titles);
        //设置轮播时间
        banner.setDelayTime(4000);
        //banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        banner.start();
    }

    /**
     * 点击wicoupon按钮的事件方法
     * @param view
     */
    @OnClick(R.id.iv_coupon)
    public void onWicouponClick(View view){
        Bundle bundle=new Bundle();
        bundle.putInt("type_id", 1);
        startActivity(CouponActivity.class,bundle,true);
    }
    /**
     * 点击wicard按钮的事件方法
     * @param view
     */
    @OnClick(R.id.iv_card)
    public void onWicardClick(View view){
        Bundle bundle=new Bundle();
        bundle.putInt("type_id", 3);
        startActivity(CardActivity.class,bundle,true);
    }

    /**
     * 点击Web按钮的事件方法
     * @param view
     */
    @OnClick(R.id.iv_website)
    public void onWebClick(View view){
        startActivity(WebActivity.class,null,true);
    }


}
