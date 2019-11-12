package com.campray.lesswalletandroid.ui;


import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.adapter.WiCouponAdapter;
import com.campray.lesswalletandroid.adapter.WiTicketAdapter;
import com.campray.lesswalletandroid.adapter.base.BaseRecyclerAdapter;
import com.campray.lesswalletandroid.adapter.listener.OnRecyclerViewListener;
import com.campray.lesswalletandroid.db.entity.Product;
import com.campray.lesswalletandroid.event.RefreshEvent;
import com.campray.lesswalletandroid.model.ProductModel;
import com.campray.lesswalletandroid.ui.base.MenuActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * 电子卡卷的页面Activity处理类
 * Created by Phills on 11/2/2017.
 */

public class CouponActivity extends MenuActivity {
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
        typeId=this.getBundle().getInt("type_id");
        showCouponList(typeId);
        setListener();
    }
    @Override
    public void onResume() {
        super.onResume();
        queryData(1);
    }

    /**
     * 根据电子卷的类型适配显示不同的电子卷列表
     */
    private void showCouponList(final int typeId){
        if(typeId==2) {
            adapter = new WiTicketAdapter(this, R.layout.item_ticket, null);
        }
        else{
            adapter = new WiCouponAdapter(this, R.layout.item_coupon, null);
        }

        rc_coupon_list.setAdapter(adapter);
        linearLayoutManager=new LinearLayoutManager(this);
        rc_coupon_list.setLayoutManager(linearLayoutManager);
    }

    /**
     * 设置相关事件
     */
    private void setListener(){
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
            //adapter.addFooter();
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
                        //List<Coupon> couponList= CouponModel.getInstance().getCouponsPageByType(typeId,page,pageSize);
                        List<Product> productList= ProductModel.getInstance().getProductPageByType(typeId,page,pageSize);
                        if(productList!=null) {
                            if(page==1){
                                adapter.bindDatas(productList);
                            }
                            else{
                                //adapter.removeFooter();
                                adapter.appendDatas(productList);
                            }
                            lastPage=page;
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }).start();


    }

    /**
     * 注册消息接收事件，接收EventBus通過EventBus.getDefault().post(T)方法发出的事件通知，onMessageEvent(T)方法就会自动接收并处理些事件通知
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshEvent event){
        queryData(lastPage);
    }

}
