package com.campray.lesswalletandroid.adapter.base;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.campray.lesswalletandroid.adapter.listener.OnRecyclerViewListener;
import com.campray.lesswalletandroid.db.entity.Coupon;
import com.campray.lesswalletandroid.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


/**
 * 列表视图控件的数据集合的显示处理器
 * 支持添加自定义头部布局；
 * 支持扩展多种item布局；
 * 支持设置recycleview点击/长按事件
 * @param <T>
 * @author Phills
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerHolder> {
    //viewType:列表第一个元素,并且不为Null值
    public static final int TYPE_FIRST = 0;
    //viewType:列表第一个元素,并且为Null值
    public static final int TYPE_FIRST_NULL = 1;
    //viewType:列表有多个元素，并且不是第一个或最后一个元素,并且不为Null值
    public static final int TYPE_ITEM = 2;
    //viewType:列表有多个元素，并且不是第一个或最后一个元素,并且为Null值
    public static final int TYPE_ITEM_NULL = 3;
    //viewType:列表有多个元素，并且最后一个元素为null值
    public static final int TYPE_LAST_NULL = 4;
    //viewType:列表有多个元素,并且最后一个元素不为Null值
    public static final int TYPE_LAST = 5;
    //viewType:列表没有任何元素
    public static final int TYPE_EMPTY = 6;

    protected final Context context;
    //列表对象对应要显示的数据列表
    protected List<T> lists;
    //当前列表对象布局资源ID
    protected int layoutId;
    protected OnRecyclerViewListener listener;
    protected BaseRecyclerHolder viewHolder;//视图对象处理器

    /**
     * 支持一种或多种Item布局
     *
     * @param context
     * @param layoutResourceId 默认的列表对象布局资源ID
     * @param datas 列表中对应要显示的数据对象集合
     */
    public BaseRecyclerAdapter(Context context, int layoutResourceId, Collection<T> datas) {
        this.context = context;
        this.layoutId = layoutResourceId;
        this.lists = datas == null ? new ArrayList<T>() : new ArrayList<T>(datas);
    }

    /**
     * 绑定数据到列表适配器(全部数据)
     * @param datas
     * @return
     */
    public BaseRecyclerAdapter<T> bindDatas(Collection<T> datas) {
        this.lists = datas == null ? new ArrayList<T>() : new ArrayList<T>(datas);
        notifyDataSetChanged();
        return this;
    }
    /**
     * 绑定数据列表适配器(追加新数据)
     * @param datas
     * @return
     */
    public BaseRecyclerAdapter<T> appendDatas(Collection<T> datas) {
        if(datas == null){
            this.lists.add(null);
        }
        else{
            this.lists.addAll(new ArrayList<T>(datas));
        }
        notifyDataSetChanged();
        return this;
    }

    /**
     * 显示底部进度条
     * @return
     */
    public void addFooter() {
        this.lists.add(null);
        notifyItemInserted(getCount()-1);
    }
    /**
     * 移除底部进度条
     * @return
     */
    public void removeFooter() {
        this.remove(getCount()-1);
        notifyItemChanged(getCount()-1);
    }

    /**
     * 绑定列表View对象，需实现此方法
     * @param holder
     * @param item
     */
    public abstract void bindView(BaseRecyclerHolder holder, T item, int position);

    /**
     * 获取当前位置的列表对象的自定义的视图类型(最先被调用)
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if(getCount()==0){
            return TYPE_EMPTY;
        }
        else {
            if (position == 0) {
                if (getItem(position) == null) {
                    return TYPE_FIRST_NULL;
                } else {
                    return TYPE_FIRST;
                }
            }else if((getCount()-1)==position) {
                if (getItem(position) == null) {
                    return TYPE_LAST_NULL;
                } else {
                    return TYPE_LAST;
                }
            }else {
                if (getItem(position) == null) {
                    return TYPE_ITEM_NULL;
                } else {
                    return TYPE_ITEM;
                }
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    /**
     * 初始化创建View处理器(重载RecyclerView.Adapter方法)
     * @param parent
     * @param viewType 自定义的视图类型(可根据此值加载不同的布局资源)
     * @return
     */
    @Override
    public BaseRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(layoutId, parent, false);
        viewHolder=new BaseRecyclerHolder(layoutId, root);
        return viewHolder;
    }

    /**
     * 根据不同的对象布局类型，绑定不同的单一对象布局处理器(重载RecyclerView.Adapter方法)
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(BaseRecyclerHolder holder, int position) {
        bindView(holder, getItem(position), position);
        //为当前要绑定的对象设置监听器
        holder.itemView.setOnClickListener(getOnClickListener(position));
        holder.itemView.setOnLongClickListener(getOnLongClickListener(position));
    }

    /**
     * 获取列表中显示对象的数量(重载RecyclerView.Adapter方法)
      * @return
     */
    @Override
    public int getItemCount() {
        return lists.size();
    }

    /**
     * 获取视图对象处理器
     * @return
     */
    public BaseRecyclerHolder getViewHolder() {
        return viewHolder;
    }

    /**
     * 删除数据
     * @param position
     */
    public void remove(int position) {
        if(lists!=null) {
            try {
                lists.remove(position);
                notifyDataSetChanged();
            }catch (Exception e){}
        }
    }

    /**
     * 获取指定position的Item
     * @param position
     * @return
     */
    public T getItem(int position) {
        if(lists.size()>0) {
            return lists.get(position);
        }
        return null;
    }

    /**
     * 获取总数
     * @return
     */
    public int getCount() {
        return this.lists == null?0:this.lists.size();
    }


    /**
     * 设置点击/长按等事件监听器
     * @param onRecyclerViewListener
     */
    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.listener = onRecyclerViewListener;
    }

    /**
     *
     * @param position
     * @return
     */
    public View.OnClickListener getOnClickListener(final int position) {
        //实例化一个View点击事件监听器
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null && v != null) {
                    //View被点击时,触发自定义的OnRecyclerViewListener的点击处理方法
                    listener.onItemClick(position);
                }
            }
        };
    }

    public View.OnLongClickListener getOnLongClickListener(final int position) {
        //实例化一个View长按事件监听器
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null && v != null) {
                    //View被长按时,触发自定义的OnRecyclerViewListener的长按处理方法
                    listener.onItemLongClick(position);
                }
                return true;
            }
        };
    }


    private Toast toast;
    public void toast(final Object obj) {
        try {
            ((BaseActivity)context).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (toast == null)
                        toast = Toast.makeText(context,"", Toast.LENGTH_SHORT);
                    toast.setText(obj.toString());
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}