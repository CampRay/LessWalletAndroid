package com.campray.lesswalletandroid.ui;


import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.adapter.WiCouponAdapter;
import com.campray.lesswalletandroid.adapter.WiTicketAdapter;
import com.campray.lesswalletandroid.adapter.base.BaseRecyclerAdapter;
import com.campray.lesswalletandroid.adapter.base.IMutlipleItem;
import com.campray.lesswalletandroid.adapter.listener.OnRecyclerViewListener;
import com.campray.lesswalletandroid.common.SmartCouponType;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.db.entity.Product;
import com.campray.lesswalletandroid.model.CouponModel;
import com.campray.lesswalletandroid.model.ProductModel;
import com.campray.lesswalletandroid.ui.base.MenuActivity;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 电子卡卷的页面Activity处理类
 * Created by Phills on 11/2/2017.
 */

public class CouponActivity extends MenuActivity {
    @BindView(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    @BindView(R.id.rc_coupon_list)
    RecyclerView rc_coupon_list;
    //Coupon列表的适配器
    private BaseRecyclerAdapter adapter;
    private int typeId=1;
    private LinearLayoutManager linearLayoutManager;
    //当前列表显示的最后一个对象的位置索引
    private int lastPosition=0;
    //当前列表加载的数据页数
    private int lastPage=1;
    private int pageSize=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        //设置刷新进度条的颜色
        sw_refresh.setColorSchemeResources(android.R.color.holo_orange_light);
        sw_refresh.setProgressViewOffset(true, 0, 50);//设置加载圈是否有缩放效果，后两个参数是展示的位置y轴坐标
        typeId=this.getBundle().getInt("type_id");
        showCouponList(typeId);
        setListener();
        //设置SwipeRefreshLayout进行刷新，以便加载数据
    }
    @Override
    public void onResume() {
        super.onResume();
        //sw_refresh.setRefreshing(true);
        queryData(1);
    }

    /**
     * 根据电子卷的类型适配显示不同的电子卷列表
     */
    private void showCouponList(int typeId){
        if(typeId==1){//电子卷类型
            //布局对象
            IMutlipleItem<Product> mutlipleItem = new IMutlipleItem<Product>() {
                //根据对象所在位置参数和对象数据，设置对象的不同布局类型
                @Override
                public int getItemViewType(int postion, Product product) {
                    if(postion>0&&product==null){
                        return 2;
                    }
                    else if(product.getCoupons().size()>1){
                        return 1;
                    }
                    return 0;
                }
                // 根据指定的View类型参数，返回对应的布局layout资源文件ID
                @Override
                public int getItemLayoutId(int viewtype) {
                    switch(viewtype){
                        case 0:
                            return R.layout.item_coupon;
                        case 1:
                            return R.layout.item_coupon_muti;
                        case 2:
                            return R.layout.item_progressbar;
                        default:
                            return R.layout.item_coupon;

                    }
                }
                //返回布局对象数量
                @Override
                public int getItemCount(List<Product> list) {
                    return list.size();
                }
            };
            adapter=new WiCouponAdapter(this,mutlipleItem,null);
        }
        else if(typeId==2){//电子票类型
            IMutlipleItem<Coupon> mutlipleItem = new IMutlipleItem<Coupon>() {
                //根据对象所在位置参数和对象数据，设置对象的不同布局类型
                @Override
                public int getItemViewType(int postion, Coupon coupon) {
                    if(postion>0&&coupon==null){
                        return 2;
                    }
                    return 0;
                }
                // 根据指定的View类型参数，返回对应的布局layout资源文件ID
                @Override
                public int getItemLayoutId(int viewtype) {
                    switch(viewtype){
                        case 0:
                            return R.layout.item_ticket;
                        case 2:
                            return R.layout.item_progressbar;
                        default:
                            return R.layout.item_ticket;

                    }
                }
                //返回布局对象数量
                @Override
                public int getItemCount(List<Coupon> list) {
                    return list.size();
                }
            };
            adapter=new WiTicketAdapter(this,mutlipleItem,null);
        }
        else if(typeId==3){//电子会员卡类型

        }
        rc_coupon_list.setAdapter(adapter);
        linearLayoutManager=new LinearLayoutManager(this);
        rc_coupon_list.setLayoutManager(linearLayoutManager);
    }

    /**
     * 设置相关事件
     */
    private void setListener(){
        //SwipeRefreshLayout下拉刷新时加载新数据
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryData(1);
            }
        });
        //设置RecyclerView滚动事件监听器
        rc_coupon_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    int count=adapter.getItemCount();
                    if(lastPosition+1==count){
                        int showPage=(int)Math.ceil((lastPosition+1)/pageSize)+1;
                        if(lastPage<showPage) {
                            queryData(showPage);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastPosition=linearLayoutManager.findLastVisibleItemPosition();
            }
        });
        //设置列表适配器中自定义的事件监听器
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
            }

            @Override
            public boolean onItemLongClick(int position) {
                return true;
            }
        });

    }

    /**
     * 获取新数据,并刷新列表
     * @param page
     * @return
     */
    private void queryData(final int page){
        if(page>1) {
            adapter.addFooter();
            rc_coupon_list.scrollToPosition(lastPosition+1);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                //new Handler().post(new Runnable() {
                //同上面的new Handler().post()一样，都是在主线程运行，可以修改UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List couponList=null;
                        if(typeId==1){
                            couponList=ProductModel.getInstance().getProductPageByType(typeId,page,pageSize);
                        }
                        else{
                            couponList= CouponModel.getInstance().getCouponsPageByType(typeId,page,pageSize);
                        }
                        //List<Coupon> couponList=CouponModel.getInstance().getCouponsPageByType(typeId,page,pageSize);
                        //List<Product> productList= ProductModel.getInstance().getProductPageByType(typeId,page,pageSize);
                        if(couponList!=null) {
                            if(page==1){
                                adapter.bindDatas(couponList);
                            }
                            else{
                                adapter.removeFooter();
                                adapter.appendDatas(couponList);
                            }
                            lastPage=page;
                            adapter.notifyDataSetChanged();
                        }
                        sw_refresh.setRefreshing(false);
                    }
                });
            }
        }).start();


    }

}
