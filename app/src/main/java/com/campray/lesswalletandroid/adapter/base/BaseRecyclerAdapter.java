package com.campray.lesswalletandroid.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.campray.lesswalletandroid.adapter.listener.OnRecyclerViewListener;
import com.campray.lesswalletandroid.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 * 支持添加自定义头部布局；
 * 支持扩展多种item布局；
 * 支持设置recycleview点击/长按事件
 * @param <T>
 * @author Phills
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerHolder> {
    /**
     * 默认布局
     */
    private final int TYPE_DEFAULT = 0;
    /**
     * 当list没有值得时候显示的布局
     */
    private final int TYPE_HEADER = 1;
    /**
     * 多重布局
     */
    private final int TYPE_MUTIPLE = 2;
    /**
     * 带header的多重布局(头部和列表中其它对象不一样)
     */
    private final int TYPE_MUTIPLE_HEADER = 3;

    private final int TYPE_FOOTER = 4;

    protected final Context context;
    //列表对象对应要显示的数据列表
    protected List<T> lists;
    //列表中有多种不同的显示布局时，需实例化此类方法以设置布局资源
    protected IMutlipleItem<T> items;
    protected OnRecyclerViewListener listener;
    protected BaseRecyclerHolder viewHolder;//视图对象处理器

    /**
     * 支持一种或多种Item布局
     *
     * @param context
     * @param items 列表中有多种不同的显示布局时，需在外部实例化此类方法并传入此Aapter中,否则传null
     * @param datas
     */
    public BaseRecyclerAdapter(Context context, IMutlipleItem<T> items, Collection<T> datas) {
        this.context = context;
        this.items = items;
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
        int type = getViewTypeByPosition(position);
        if(type==TYPE_HEADER){
            return items.getItemViewType(position, null);
        }else if(type==TYPE_MUTIPLE){
            return items.getItemViewType(position, lists.get(position));
        }else if(type==TYPE_MUTIPLE_HEADER){
            int headerCount = getItemCount() - lists.size();
            return items.getItemViewType(position, lists.get(position - headerCount));
        }else{
            return 0;
        }
    }

    /**
     * 初始化创建View处理器(重载RecyclerView.Adapter方法)
     * @param parent
     * @param viewType 自定义的视图类型(可根据此值加载不同的布局资源)
     * @return
     */
    @Override
    public BaseRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据不同的View类型打开不同的View布局
        int layoutId = items.getItemLayoutId(viewType);
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(layoutId, parent, false);
        viewHolder=new BaseRecyclerHolder(layoutId, root);
        return viewHolder;
    }

    /**
     * 绑定iew处理器(重载RecyclerView.Adapter方法)
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(BaseRecyclerHolder holder, int position) {
        int type = getViewTypeByPosition(position);
        if(type==TYPE_HEADER){
            bindView(holder, null, position);
        }else if(type==TYPE_MUTIPLE){
            bindView(holder, lists.get(position), position);
        }else if(type==TYPE_MUTIPLE_HEADER){
            int headerCount = getItemCount() - lists.size();
            bindView(holder, lists.get(position - headerCount), position);
        }else{
            bindView(holder, null, position);
        }
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
        if (items != null) {//当有多重布局的时候，则采用多重布局
            return items.getItemCount(lists);
        }
        return lists.size();
    }


    /**获取指定position的布局类型
     * @param position
     */
    private int getViewTypeByPosition(int position) {
        if (items == null) {//默认布局
            return TYPE_DEFAULT;
        } else {//多布局
            if (lists != null && lists.size() > 0) {//list有值的时候
                if (getItemCount() > lists.size()) {//是否有自定义的Header
                    int headerCount = getItemCount() - lists.size();
                    if (position >= headerCount) {//当前位置大于header个数
                        return TYPE_MUTIPLE_HEADER;
                    } else {//当前点击的是header
                        return TYPE_HEADER;
                    }
                } else {
                    return TYPE_MUTIPLE;
                }
            } else {//list还没有值的时候,显示的布局
                return TYPE_HEADER;
            }
        }
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
        int more = getItemCount() - lists.size();
        lists.remove(position - more);
        notifyDataSetChanged();
    }

    /**
     * 获取指定position的Item
     * @param position
     * @return
     */
    public T getItem(int position) {
        int more = getItemCount() - lists.size();
        return lists.get(position - more);
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