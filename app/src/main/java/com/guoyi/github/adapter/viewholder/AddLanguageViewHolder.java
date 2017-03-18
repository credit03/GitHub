package com.guoyi.github.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.guoyi.github.R;
import com.guoyi.github.adapter.OnRecyclerViewListener;
import com.guoyi.github.bean.Language;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Credit on 2017/3/17.
 */

public class AddLanguageViewHolder extends BaseViewHolder<Language> {

    @InjectView(R.id.text)
    TextView text;
    @InjectView(R.id.checkbox)
    CheckBox checkbox;
    @InjectView(R.id.item)
    FrameLayout item;

    public AddLanguageViewHolder(Context context, ViewGroup root, int layoutRes, OnRecyclerViewListener l) {
        super(context, root, layoutRes, l);
    }

    @Override
    public void initView(View rootView) {
        ButterKnife.inject(this, rootView);
    }

    @Override
    public void bindData(Language language, int postion) {
        super.bindData(language, postion);
        if (mdata != null) {
            checkbox.setChecked(mdata.check);
            text.setText(mdata.name);
        }
    }

    @Override
    public void initEvent() {
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mdata.check = b;
            }
        });
        checkbox.setOnClickListener(this);
    }
}
