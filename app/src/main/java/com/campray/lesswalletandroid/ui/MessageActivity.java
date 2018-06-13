package com.campray.lesswalletandroid.ui;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.adapter.HistoryAdapter;
import com.campray.lesswalletandroid.adapter.WiCouponAdapter;
import com.campray.lesswalletandroid.adapter.WiTicketAdapter;
import com.campray.lesswalletandroid.adapter.base.BaseRecyclerAdapter;
import com.campray.lesswalletandroid.adapter.base.IMutlipleItem;
import com.campray.lesswalletandroid.adapter.listener.OnRecyclerViewListener;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.db.entity.History;
import com.campray.lesswalletandroid.db.entity.Product;
import com.campray.lesswalletandroid.model.CouponModel;
import com.campray.lesswalletandroid.model.HistoryModel;
import com.campray.lesswalletandroid.model.ProductModel;
import com.campray.lesswalletandroid.ui.base.MenuActivity;

import java.util.List;

import butterknife.BindView;

/**
 * 消息列表页面Activity处理类
 * Created by Phills on 6/2/2018.
 */

public class MessageActivity extends MenuActivity {
    @BindView(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    @BindView(R.id.rc_message_list)
    RecyclerView rc_message_list;
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
        setContentView(R.layout.activity_message);
        //设置刷新进度条的颜色
        sw_refresh.setColorSchemeResources(android.R.color.holo_orange_light);
        sw_refresh.setProgressViewOffset(true, 0, 50);//设置加载圈是否有缩放效果，后两个参数是展示的位置y轴坐标
        showMessageList();
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
     * 显示消息列表
     */
    private void showMessageList(){
        //布局对象
        IMutlipleItem<History> mutlipleItem = new IMutlipleItem<History>() {
            //根据对象所在位置参数和对象数据，设置对象的不同布局类型
            @Override
            public int getItemViewType(int postion, History history) {
                return 0;
            }
            // 根据指定的View类型参数，返回对应的布局layout资源文件ID
            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.item_message;
            }
            //返回布局对象数量
            @Override
            public int getItemCount(List<History> list) {
                return list.size();
            }
        };
        adapter=new HistoryAdapter(this,mutlipleItem,null);

        rc_message_list.setAdapter(adapter);
        linearLayoutManager=new LinearLayoutManager(this);
        rc_message_list.setLayoutManager(linearLayoutManager);
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
        rc_message_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
            public boolean onItemLongClick(final int position) {
                AlertDialog dialog = new AlertDialog.Builder(MessageActivity.this)
                        .setTitle("删除消息")//设置对话框的标题
                        .setMessage("确定要删除此消息吗？")//设置对话框的内容
                        //设置对话框的按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                History delHistory=(History)adapter.getItem(position);
                                HistoryModel.getInstance().delHistory(delHistory);
                                adapter.remove(position);
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
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
            rc_message_list.scrollToPosition(lastPosition+1);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                //new Handler().post(new Runnable() {
                //同上面的new Handler().post()一样，都是在主线程运行，可以修改UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List messageList=HistoryModel.getInstance().getAllHistories(page,pageSize);
                        if(messageList!=null) {
                            if(page==1){
                                adapter.bindDatas(messageList);
                            }
                            else{
                                adapter.removeFooter();
                                adapter.appendDatas(messageList);
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
