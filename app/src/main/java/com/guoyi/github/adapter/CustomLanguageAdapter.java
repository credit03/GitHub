package com.guoyi.github.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.guoyi.github.R;
import com.guoyi.github.adapter.viewholder.BaseViewHolder;
import com.guoyi.github.adapter.viewholder.CustomLanguageViewHolder;
import com.guoyi.github.bean.Language;
import com.guoyi.github.helper.ItemTouchHelperAdapter;
import com.guoyi.github.helper.OnStartDragListener;

/**
 * Created by Credit on 2017/3/17.
 */

public class CustomLanguageAdapter extends BaseRecyclerAdapter<Language> implements ItemTouchHelperAdapter {


    private int Mode = 0;
    private OnStartDragListener mDragStartListener;

    public CustomLanguageAdapter(Context mContext, int mode, OnStartDragListener mDragStartListener) {
        super(mContext);
        this.mDragStartListener = mDragStartListener;
        Mode = mode;

        setListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(View view, int position, BaseViewHolder holder) {
                mData.remove(position);
                notifyDataSetChanged();
            }
        });
    }


    public void setMode(int mode) {
        this.Mode = mode;
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomLanguageViewHolder(mContext, parent, R.layout.item_language_custom_grid, mDragStartListener, listener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        CustomLanguageViewHolder viewHolder = (CustomLanguageViewHolder) holder;
        viewHolder.bindData(mData.get(position), Mode, position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Language language = mData.remove(fromPosition);
        mData.add(toPosition, language);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }
}
