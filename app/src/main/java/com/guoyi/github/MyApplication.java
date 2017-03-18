package com.guoyi.github;

import android.app.Application;
import android.content.Context;

import com.guoyi.github.utils.FavoReposHelper;
import com.guoyi.github.utils.LanguageHelper;
import com.orhanobut.logger.Logger;

/**
 * Created by Credit on 2017/3/16.
 */

public class MyApplication extends Application {
    private Context context;

    public static boolean DEBUG = true;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("github");
        LanguageHelper.init(this);
        FavoReposHelper.init(this);
    }

    public static void log(String msg) {
        if (DEBUG) {
            Logger.d(msg);
        }
    }

    public Context getContext() {
        return context;
    }
}
