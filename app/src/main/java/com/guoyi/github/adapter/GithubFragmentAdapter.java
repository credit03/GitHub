package com.guoyi.github.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.guoyi.github.R;
import com.guoyi.github.bean.Language;
import com.guoyi.github.ui.fragment.BaseFragment;
import com.guoyi.github.ui.fragment.GithubFargment;
import com.guoyi.github.utils.LanguageHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Credit on 2017/1/15.
 */

public class GithubFragmentAdapter<T extends BaseFragment> extends FragmentPagerAdapter {
    private Context context;
    private String SpanTime;
    private FragmentManager fragmentManager;
    private List<Language> languages = new ArrayList();

    public GithubFragmentAdapter(FragmentManager fm, Context context, String spanTime) {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
        SpanTime = spanTime;
        languages = LanguageHelper.getInstance().getSelectedLanguages();
    }


    public synchronized void changeLanguage() {
        languages = LanguageHelper.getInstance().getSelectedLanguages();
        if (languages != null) {
            this.notifyDataSetChanged();
        }
    }

    public void updateSpanOrder(String SpanTime) {
        this.SpanTime = SpanTime;
        for (int i = 0; i < getCount(); i++) {
            Fragment fragment = fragmentManager.findFragmentByTag(getFragmentTag(R.id.view_pager, i));
            if (fragment instanceof GithubFargment) {
                if (fragment.isAdded()) {
                    ((GithubFargment) fragment).updateTimeSpan(SpanTime);
                }
            }
        }
    }

    public void destoryData() {
        for (int i = 0; i < getCount(); i++) {
            Fragment fragment = fragmentManager.findFragmentByTag(getFragmentTag(R.id.view_pager, i));
            if (fragment instanceof GithubFargment) {
                if (fragment.isAdded()) {
                    ((GithubFargment) fragment).destroyData();
                }
            }
        }
    }

    @Override
    public int getCount() {
        return this.languages.size();
    }

    @Override
    public BaseFragment getItem(int paramInt) {
        return GithubFargment.newInstance(languages.get(paramInt), SpanTime);
    }

    @Override
    public CharSequence getPageTitle(int paramInt) {
        return this.languages.get(paramInt).name;
    }


    public String getFragmentTag(int viewPagerId, int fragmentPosition) {
        return "android:switcher:" + viewPagerId + ":" + fragmentPosition;
    }


    /**
     * 在一个 Android 应用中，我使用 FragmentPagerAdapter 来处理多 Fragment 页面的横向滑动。
     * 不过我碰到了一个问题，即当 Fragment 对应的数据集发生改变时，我希望能够通过调用 mAdapter.notifyDataSetChanged()
     * 来触发 Fragment 页面使用新的数据调整或重新生成其内容，可是当我调用 notifyDataSetChanged() 后，发现什么都没发生。
     * <p>
     * <p>
     * 解决notifyDataSetChanged 之后数据不改变
     *
     * @param container
     * @param position
     * @return
     */

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        GithubFargment f = (GithubFargment) super.instantiateItem(container, position);
        if (f != null) {
            Language language = languages.get(position);
            f.updataLanguage(language);
        }
        return f;
    }

    // Called when the host view is attempting to determine if an item's position has changed.
    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;


    }

    @Override
    public void finishUpdate(ViewGroup container) {
        try {
            super.finishUpdate(container);
        } catch (NullPointerException nullPointerException) {
            System.out.println("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
        }
    }
}
