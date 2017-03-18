package com.guoyi.github.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.guoyi.github.R;
import com.guoyi.github.adapter.viewholder.BaseViewHolder;
import com.guoyi.github.adapter.viewholder.GithubViewHolder;
import com.guoyi.github.bean.GithubResponse;
import com.guoyi.github.bean.Language;

/**
 * Created by Credit on 2017/3/16.
 */

public class GithubAdapter extends BaseRecyclerAdapter<GithubResponse> {

    private Language language;

    public GithubAdapter(Context mContext, Language language) {
        super(mContext);
        this.language = language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GithubViewHolder(mContext, parent, R.layout.item_github_view, listener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (holder instanceof GithubViewHolder) {
            ((GithubViewHolder) holder).bindData(mData.get(position), language, position);
        }
    }


}
