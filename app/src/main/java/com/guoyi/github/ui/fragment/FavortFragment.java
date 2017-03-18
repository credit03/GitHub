package com.guoyi.github.ui.fragment;

import com.guoyi.github.Constants;
import com.guoyi.github.R;
import com.guoyi.github.adapter.GithubAdapter;
import com.guoyi.github.bean.GithubResponse;
import com.guoyi.github.bean.Language;
import com.guoyi.github.utils.FavoReposHelper;
import com.guoyi.github.utils.rxbus2.RxBusBuilder;
import com.guoyi.github.utils.rxbus2.rx.RxBusMode;

import io.reactivex.functions.Consumer;

/**
 * Created by Credit on 2017/3/16.
 */

public class FavortFragment extends BaseSwipeFragement {

    public static FavortFragment newInstance() {
        FavortFragment fragment = new FavortFragment();
        return fragment;
    }

    @Override
    public void replaceTheme() {
        super.replaceTheme();
        parentActivity.setToolBar(toolbar, getString(R.string.favorites));
        toolbar.setNavigationOnClickListener(n -> {
            parentActivity.onBackPressed();

        });
    }

    @Override
    protected void initEvent() {

        RxBusBuilder.create(GithubResponse.class)
                // this enables the binding to the key
                .withKey(Constants.FAVORT_DELETE) // you may add multiple keys as well!
                .withMode(RxBusMode.Main)
                .subscribe(new Consumer<GithubResponse>() {
                    @Override
                    public void accept(GithubResponse event) {
                        log("收到RX删除事件 " + event.toString());
                        if (adapter != null) {
                            adapter.changeData(FavoReposHelper.getInstance().getFavos());
                        }
                    }
                });


    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.changeData(FavoReposHelper.getInstance().getFavos());
    }

    @Override
    public void initAdapter() {
        adapter = new GithubAdapter(mContext, new Language("favos", "favos_path"));
    }
}
