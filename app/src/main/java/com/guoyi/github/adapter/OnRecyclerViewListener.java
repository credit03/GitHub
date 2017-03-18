package com.guoyi.github.adapter;

import android.view.View;

import com.guoyi.github.adapter.viewholder.BaseViewHolder;

/**
 * 为RecycleView添加点击事件
 */
public interface OnRecyclerViewListener {
    /**
     * @param view     事件触发view
     * @param position 位置
     * @param holder   ViewHolder
     */
    void onItemClick(View view, int position, BaseViewHolder holder);
}
