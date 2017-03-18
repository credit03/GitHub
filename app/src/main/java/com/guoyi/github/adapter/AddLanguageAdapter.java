package com.guoyi.github.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.guoyi.github.R;
import com.guoyi.github.adapter.viewholder.AddLanguageViewHolder;
import com.guoyi.github.adapter.viewholder.BaseViewHolder;
import com.guoyi.github.bean.Language;

/**
 * Created by Credit on 2017/3/17.
 */

public class AddLanguageAdapter extends BaseRecyclerAdapter<Language> {


    public AddLanguageAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddLanguageViewHolder(mContext, parent, R.layout.item_add_language_grid, listener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindData(mData.get(position), position);
    }
}
