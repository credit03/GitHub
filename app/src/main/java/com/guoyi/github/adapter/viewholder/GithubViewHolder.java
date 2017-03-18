package com.guoyi.github.adapter.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guoyi.github.Constants;
import com.guoyi.github.R;
import com.guoyi.github.adapter.OnRecyclerViewListener;
import com.guoyi.github.bean.GithubResponse;
import com.guoyi.github.bean.Language;
import com.guoyi.github.ui.WebViewActivity;
import com.guoyi.github.ui.view.AsyncImageView;
import com.guoyi.github.utils.FavoReposHelper;
import com.guoyi.github.utils.LanguageHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Credit on 2017/3/16.
 */

public class GithubViewHolder extends BaseViewHolder<GithubResponse> {
    public GithubViewHolder(Context context, ViewGroup root, int layoutRes, OnRecyclerViewListener l) {
        super(context, root, layoutRes, l);
    }

    @InjectView(R.id.cardView)
    CardView cardView;

    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.description)
    TextView description;
    @InjectView(R.id.meta)
    TextView meta;
    @InjectView(R.id.built_by)
    TextView builtBy;
    @InjectView(R.id.avatar1)
    AsyncImageView avatar1;
    @InjectView(R.id.avatar2)
    AsyncImageView avatar2;
    @InjectView(R.id.avatar3)
    AsyncImageView avatar3;
    @InjectView(R.id.avatar4)
    AsyncImageView avatar4;
    @InjectView(R.id.avatar5)
    AsyncImageView avatar5;
    @InjectView(R.id.contributors_line)
    LinearLayout contributorsLine;
    @InjectView(R.id.star)
    ImageView star;
    @InjectView(R.id.label)
    TextView label;

    private List<AsyncImageView> avatars;

    @Override
    public void initView(View rootView) {
        ButterKnife.inject(this, rootView);
        avatars = new ArrayList<>();
        avatars.add(avatar1);
        avatars.add(avatar2);
        avatars.add(avatar3);
        avatars.add(avatar4);
        avatars.add(avatar5);
    }


    private Language language;

    public void bindData(@NonNull GithubResponse githubData, Language language, int postion) {
        super.bindData(githubData, postion);
        this.language = language;
        if (mdata != null) {
            title.setText(mdata.owner + "/" + mdata.name);

            if (TextUtils.isEmpty(mdata.description)) {
                description.setVisibility(View.GONE);
            } else {
                description.setVisibility(View.VISIBLE);
                description.setText(mdata.description);
            }


            if (TextUtils.isEmpty(mdata.meta)) {
                meta.setVisibility(View.GONE);
            } else {
                meta.setText(mdata.meta);
                meta.setVisibility(View.VISIBLE);
            }


            if (TextUtils.isEmpty(language.path)) {
                // only show label in Tab 『All Language』
                if (TextUtils.isEmpty(mdata.language)) {
                    label.setText("N/A");
                } else {
                    Language l = LanguageHelper.getInstance().getLanguageByName(mdata.language);
                    if (l == null) {
                        label.setText(mdata.language);
                    } else {
                        label.setText(l.getShortName());
                    }
                }
                label.setVisibility(View.VISIBLE);
            } else {

                label.setVisibility(View.GONE);
            }


            if (FavoReposHelper.getInstance().contains(mdata)) {
                star.setImageResource(R.mipmap.ic_star_checked);
            } else {
                star.setImageResource(R.mipmap.ic_star_unchecked);
            }


            for (int i = 0; i < avatars.size(); i++) {
                if (mdata.contributors != null && i < mdata.contributors.size()) {
                    avatars.get(i).loadImage(mdata.contributors.get(i).avatar);
                    avatars.get(i).setVisibility(View.VISIBLE);
                } else {
                    avatars.get(i).setVisibility(View.GONE);
                }
            }

        }
    }

    @Override
    public void initEvent() {
        cardView.setOnClickListener(this);
        star.setOnClickListener(this);
        contributorsLine.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardView:
                WebViewActivity.startCurActivity(mContext, mdata);
                break;
            case R.id.star:

                if (TextUtils.isEmpty(mdata.language)) {
                    mdata.language = this.language.name;
                }
                mdata.path = this.language.path;

                if (FavoReposHelper.getInstance().contains(mdata)) {

                    mdata.sendRxBus(Constants.FAVORT_DELETE);
                    FavoReposHelper.getInstance().removeFavo(mdata);
                    star.setImageResource(R.mipmap.ic_star_unchecked);
                } else {
                    mdata.sendRxBus(Constants.FAVORT_ADD);
                    FavoReposHelper.getInstance().addFavo(mdata);
                    star.setImageResource(R.mipmap.ic_star_checked);
                }
                break;
            case R.id.contributors_line:
                String contributorUrl;
                if (mdata.url.endsWith("/")) {
                    contributorUrl = mdata.url + "graphs/contributors";
                } else {
                    contributorUrl = mdata.url + "/graphs/contributors";
                }

                GithubResponse data = new GithubResponse();
                data.url = contributorUrl;
                data.name = "贡献者";
                WebViewActivity.startCurActivity(mContext, data);

                break;
        }
    }
}
