package com.guoyi.github.adapter.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.guoyi.github.Constants;
import com.guoyi.github.R;
import com.guoyi.github.adapter.OnRecyclerViewListener;
import com.guoyi.github.bean.Language;
import com.guoyi.github.helper.ItemTouchHelperViewHolder;
import com.guoyi.github.helper.OnStartDragListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Credit on 2017/3/17.
 */

public class CustomLanguageViewHolder extends BaseViewHolder<Language> implements ItemTouchHelperViewHolder {

    private OnStartDragListener mDragStartListener;

    public CustomLanguageViewHolder(Context context, ViewGroup root, int layoutRes, OnStartDragListener listener, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, layoutRes, onRecyclerViewListener);
        this.mDragStartListener = listener;
    }

    @InjectView(R.id.text)
    TextView text;
    @InjectView(R.id.handle)
    ImageView handle;
    @InjectView(R.id.remove)
    ImageView remove;
    @InjectView(R.id.item)
    FrameLayout item;

    @Override
    public void initView(View rootView) {
        ButterKnife.inject(this, rootView);
    }

    public void bindData(Language language, int mode, int postion) {
        super.bindData(language, postion);
        if (mdata != null) {
            text.setText(mdata.name);
            switch (mode) {
                case Constants.LANGUAGE_CUSTOM_MOVE:
                    handle.setVisibility(View.VISIBLE);
                    remove.setVisibility(View.GONE);
                    break;
                case Constants.LANGUAGE_CUSTOM_DELETE:
                    handle.setVisibility(View.GONE);
                    remove.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    public void initEvent() {
        // Start a drag whenever the handle view it touched
        handle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(CustomLanguageViewHolder.this);
                }
                return false;
            }
        });

        remove.setOnClickListener(this);
    }

    @Override
    public void onItemSelected() {
        itemView.setBackgroundColor(Color.LTGRAY);
    }

    @Override
    public void onItemClear() {
        itemView.setBackgroundColor(0);
    }
}
