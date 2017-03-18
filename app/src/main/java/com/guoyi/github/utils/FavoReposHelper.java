package com.guoyi.github.utils;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.guoyi.github.bean.GithubResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lao on 15/9/25.
 */
public class FavoReposHelper {


    static FavoReposHelper instance;
    static OfflineACache aCache;

    public static synchronized FavoReposHelper getInstance() {
        return instance;
    }


    public static void init(Application application) {
        instance = new FavoReposHelper(application);
    }

    List<GithubResponse> reposSet = new ArrayList<>();

    Context context;

    private FavoReposHelper(Context context) {
        this.context = context;
        aCache = OfflineACache.get(context);
        String favoReposJson = aCache.getAsString("favoJSON");

        if (!TextUtils.isEmpty(favoReposJson)) {
            reposSet = GsonTools.changeGsonToSafeList(favoReposJson, GithubResponse.class);
        }
        if (reposSet == null) {
            reposSet = new ArrayList<>();
        }
    }

    public boolean contains(GithubResponse repo) {
        return reposSet.contains(repo);
    }

    public List<GithubResponse> getFavos() {
        return reposSet;
    }

    public void addFavo(GithubResponse repo) {

        reposSet.add(repo);
        saveToPref();
    }


    public void removeFavo(GithubResponse repo) {
        reposSet.remove(repo);
        saveToPref();
    }

    private void saveToPref() {
        String json = GsonTools.createGsonString(reposSet);
        aCache.put("favoJSON", json);
    }
}
