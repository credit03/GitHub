package com.guoyi.github.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.guoyi.github.MyApplication;
import com.guoyi.github.ui.MainActivity;
import com.orhanobut.logger.Logger;

/**
 * Created by Credit on 2016/12/12.
 */
public abstract class BaseFragment extends Fragment implements OnClickListener {
    protected Context mContext;
    protected View rootView;
    public int pageIndex = 0;
    public int pageSize = 16;


    protected MainActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /**
         * activity+fragment多次切换出现页面空白问题,
         * 问题表现在，第一个fragment加载数据缓慢
         * 在未加载完全的时候切换其他页面的时候出现空白，
         * 在网上找了各种方法 刚开始以为缓存方面没做好(确实还没来得及做缓存那块的优化)，
         * 后来看各种帖子，设置在清单文件设至signTask,  ...终于意识到fragment状态没保存。。。
         */
        if (rootView == null) {
            rootView = setContentView(inflater, container);
            mContext = getActivity();
            parentActivity = (MainActivity) getActivity();
            initView(rootView);
            initValue(savedInstanceState);
            initEvent();
        } else {
            log("初始化.... this.rootView != null" + this.getClass().getSimpleName());
            ViewGroup localViewGroup = (ViewGroup) this.rootView.getParent();
            if (localViewGroup != null) {
                localViewGroup.removeView(this.rootView);
            }
        }
        replaceTheme();
        return rootView;
    }


    public void replaceTheme() {

    }

    public String fomatRstring(int r, String s) {
        return String.format(mContext.getString(r), s);
    }

    /**
     * 获取根view
     *
     * @return
     */
    public View getRootView() {
        return rootView;
    }


    public long lastime = 0;

    /**
     * 当前时间与上一次加载时间比较，是否大于3分钟
     *
     * @return
     */
    public boolean comLastLoadTime() {
        return comLastLoadTime(3);

    }

    /**
     * 当前时间与上一次加载时间比较，是否大于min分钟
     *
     * @return
     */
    public boolean comLastLoadTime(int min) {
        if (System.currentTimeMillis() - lastime > 1000 * 60 * min) {
            lastime = System.currentTimeMillis();
            return true;
        }
        return false;

    }

    /**
     * 设置布局文件
     *
     * @param inflater
     * @param container
     * @return
     */
    protected abstract View setContentView(LayoutInflater inflater, ViewGroup container);

    /**
     * 初始化View
     *
     * @param rootView
     */
    protected abstract void initView(View rootView);

    /**
     * 初始化事件
     */
    protected abstract void initEvent();

    /**
     * 初始化值
     *
     * @param savedInstanceState
     */
    protected abstract void initValue(Bundle savedInstanceState);

    /**
     * 加载数据
     *
     * @param msg
     */
    protected abstract void loadData(String msg);

    /**
     * 回收资源，在Activity--OnDestroy调用
     */
    public abstract void destroyData();

    /***
     * 测试LOG
     */
    public void log(String msg) {
        if (MyApplication.DEBUG) {
            Logger.d(msg);
        }
    }

    //分享文字
    public void shareText(String text) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }


}
