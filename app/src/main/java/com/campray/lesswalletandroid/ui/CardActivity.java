package com.campray.lesswalletandroid.ui;


import android.content.DialogInterface;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.campray.lesswalletandroid.LessWalletApplication;
import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.adapter.WiCardAdapter;
import com.campray.lesswalletandroid.adapter.listener.OnRecyclerViewListener;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.db.entity.Merchant;
import com.campray.lesswalletandroid.db.entity.Product;
import com.campray.lesswalletandroid.db.entity.User;
import com.campray.lesswalletandroid.listener.OperationListener;
import com.campray.lesswalletandroid.model.CouponModel;
import com.campray.lesswalletandroid.model.UserModel;
import com.campray.lesswalletandroid.ui.base.MenuActivity;
import com.campray.lesswalletandroid.util.AppException;
import com.campray.lesswalletandroid.util.ResourcesUtils;
import com.campray.lesswalletandroid.view.CustomDialog;
import com.campray.lesswalletandroid.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 电子卡的页面Activity处理类
 * Created by Phills on 11/2/2017.
 */

public class CardActivity extends MenuActivity {
    @BindView(R.id.rc_card_list)
    RecyclerView rc_card_list;
    @BindView(R.id.ll_cardinfo)
    LinearLayout ll_cardinfo;


    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_merchant)
    TextView tv_merchant;
    @BindView(R.id.tv_points)
    TextView tv_points;
    @BindView(R.id.tv_cash)
    TextView tv_cash;
    @BindView(R.id.tv_expired)
    TextView tv_expired;
    @BindView(R.id.tv_shortdesc)
    TextView tv_shortdesc;

    private int typeId=3;

    private WiCardAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private LinearSnapHelper linearSnapHelper=new LinearSnapHelper();
    private User user=LessWalletApplication.INSTANCE().getAccount();
    //当前列表显示的最后一个对象的位置索引
    private int lastPosition=0;
    //当前列表加载的数据页数
    private int lastPage=1;
    private int pageSize=10;
    private long couponId=0;
    //当前列表居中显示的一个对象的位置索引
    private int position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        typeId=this.getBundle().getInt("type_id");
        showCardList();
        setListener();
        //如果有网络，则查询最新的用户信息
        UserModel.getInstance().updateUserFormServer(user.getId(), new OperationListener<User>() {
            @Override
            public void done(User obj, AppException exception) {user=obj;}
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        queryData(1);
    }

    /**
     * 根据会员卡的类型适配显示不同的会员卡列表
     */
    private void showCardList(){
        adapter=new WiCardAdapter(this,R.layout.item_card,null);
        rc_card_list.setAdapter(adapter);
        linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rc_card_list.setLayoutManager(linearLayoutManager);
        rc_card_list.addItemDecoration( new SpaceItemDecoration(15));
        //使用让列表当前对象居中的布局辅助器
        linearSnapHelper.attachToRecyclerView(rc_card_list);
    }

    /**
     * 设置相关事件
     */
    private void setListener(){
        //设置RecyclerView滚动事件监听器
        rc_card_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    //获取当前居中显示的列表对象
                    RelativeLayout layout_card=(RelativeLayout)linearSnapHelper.findSnapView(linearLayoutManager);
                    if(layout_card.getTag()!=null){
                        //获取当前居中显示的Coupon的ID
                        long oid=(long)layout_card.getTag();
                        position=adapter.findPosition(oid);
                        Coupon coupon=adapter.getItem(position);
                        showCardInfo(coupon);
                    }

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
     * 显示会员卡信息区域
     */
    private void showCardInfo(Coupon coupon){
        if(coupon!=null) {
            couponId=coupon.getOrderId();
            Product product=coupon.getProduct();
            tv_title.setText(product.getTitle());
            tv_shortdesc.setText(product.getShortDesc());
            String expired = (TextUtils.isEmpty(coupon.getStartTimeLocal()) ? "" : coupon.getStartTimeLocal()) + (TextUtils.isEmpty(coupon.getEndTimeLocal()) ? " -" : " - " + coupon.getEndTimeLocal());
            tv_expired.setText(expired);
            Merchant merchant=product.getMerchant();
            if(merchant!=null) {
                tv_merchant.setText(merchant.getName());
                Integer storeId = merchant.getStoreId();
                tv_points.setText(user.getStorePoints().get(storeId) + "");
                tv_cash.setText(user.getCashStr(storeId) + "");
            }
            else{
                tv_merchant.setText("");
                tv_points.setText("0");
                tv_cash.setText("0");
            }
        }
    }

    /**
     * 点击使用按钮
     */
    @OnClick(R.id.btn_use)
    public void onClickUse(){
        Bundle bundle=new Bundle();
        bundle.putLong("coupon_id",couponId);
        startActivity(CardUseActivity.class,bundle,false);
    }

    /**
     * 点击删除按钮
     */
    @OnClick(R.id.btn_discard)
    public void onClickDel(){
        //显示移除对话框
        showDialog();
    }


    /**
     * 获取新数据,并刷新列表
     * @param page
     * @return
     */
    private void queryData(final int page){
        if(page>1) {
            //adapter.addFooter();
            rc_card_list.scrollToPosition(lastPosition+1);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                //new Handler().post(new Runnable() {
                //同上面的new Handler().post()一样，都是在主线程运行，可以修改UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<Coupon> couponList= CouponModel.getInstance().getCouponsPageByType(typeId,page,pageSize);
                        if(couponList!=null) {
                            if(page==1){
                                adapter.bindDatas(couponList);
                                Coupon coupon=adapter.getItem(0);
                                showCardInfo(coupon);
                            }
                            else{
                                //adapter.removeFooter();
                                adapter.appendDatas(couponList);
                            }
                            lastPage=page;
                            adapter.notifyDataSetChanged();
                        }
                        if(adapter.getItemCount()==0){
                            ll_cardinfo.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 显示移除对话框
     */
    private void showDialog(){
        final CustomDialog.Builder normalDialog =new CustomDialog.Builder(this);
        normalDialog.setIcon(R.mipmap.face_sad);
        //normalDialog.setTitle("Title");
        normalDialog.setMessage("Do you wish to discard this card?");
        normalDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        List<Long> idList=new ArrayList<Long>();
                        idList.add(couponId);
                        //final int couponSize=idList.size();
                        String idsStr=TextUtils.join(",",idList.toArray());
                        CouponModel.getInstance().delCouponsFromServer(idsStr, new OperationListener<Coupon>() {
                            @Override
                            public void done(Coupon obj, AppException exception) {
                                if(exception==null) {
                                    adapter.remove(position);
                                    rc_card_list.scrollToPosition(0);
                                    dialog.cancel();
                                    toast("This card has been discarded successfully.");
                                }
                                else{
                                    toast("Failed to discard the card, Error Message: "+ ResourcesUtils.getString(CardActivity.this,exception.getErrorCode()));
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
