package com.guoyi.github.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.guoyi.github.MyApplication;
import com.guoyi.github.R;
import com.guoyi.github.utils.OfflineACache;
import com.guoyi.github.utils.Theme;
import com.orhanobut.logger.Logger;

/**
 * Created by Credit on 2017/3/16.
 */

public class BaseAcitvity extends AppCompatActivity {


    protected OfflineACache aCache;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        aCache = OfflineACache.get(this);
        PreTheme();
        super.onCreate(savedInstanceState);

    }


    /**
     * 切换主题
     */
    public void PreTheme() {
        Theme theme = aCache.getCurrentTheme();
        switch (theme) {
            case Blue:
                this.setTheme(R.style.BlueTheme);
                break;
            case Green:
                this.setTheme(R.style.GreenTheme);
                break;
            case Red:
                this.setTheme(R.style.RedTheme);
                break;
            case Indigo:
                this.setTheme(R.style.IndigoTheme);
                break;
            case BlueGrey:
                this.setTheme(R.style.BlueGreyTheme);
                break;
            case Black:
                this.setTheme(R.style.BlackTheme);
                break;
            case Orange:
                this.setTheme(R.style.OrangeTheme);
                break;
            case Purple:
                this.setTheme(R.style.PurpleTheme);
                break;
            case Pink:
                this.setTheme(R.style.PinkTheme);
                break;
            default:
                this.setTheme(R.style.BlueTheme);
                break;
        }
    }

    public void log(String msg) {
        if (MyApplication.DEBUG) {
            Logger.d(msg);
        }
    }
}
