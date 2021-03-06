package com.guoyi.github.ui.view;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.guoyi.github.R;

/**
 * Created by lao on 3/4/14.
 */
public class AsyncImageView extends AppCompatImageView {

    String url;

    public AsyncImageView(final Context context) {
        super(context);
        init();
    }

    public AsyncImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AsyncImageView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init() {

    }

    public void loadImage(final String imageUrl) {
        if (imageUrl != null && url != null) {
            if (Uri.parse(imageUrl).getPath().equals(Uri.parse(url).getPath())) {
                return;
            }
        }
        url = imageUrl;
        executeLoadImage();
    }


    protected void executeLoadImage() {

        if (TextUtils.isEmpty(url)) {
            setImageResource(R.drawable.image_loading_resource);
        } else {
            Glide.with(getContext()).load(url).error(R.drawable.image_loading_resource).into(this);

        }
    }
}
