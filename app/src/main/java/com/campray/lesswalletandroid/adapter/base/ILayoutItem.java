package com.campray.lesswalletandroid.adapter.base;


/**
 * 列表控件的自定义的布局对象接口
 *
 */
public abstract class ILayoutItem<T> {

    /**
     * 获取当前要显示的位置对象的布局类型
     * @param viewType 当前位置对象,由BaseRecyclerAdpter类默认判断的布局类型
     * @param postion
     * @param t
     * @return
     */
    public int getItemViewType(int viewType,int postion, T t){
        return viewType;
    }

    /**
     * 根据布局类型，设置对应的布局XML文件
     * @param viewtype
     * @return 布局xml资源ID
     */
    public abstract int getItemLayoutId(int viewtype);
}
