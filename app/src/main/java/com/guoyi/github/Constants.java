package com.guoyi.github;

import com.guoyi.github.bean.GithubResponse;
import com.guoyi.github.bean.User;

import java.util.ArrayList;

/**
 * Created by Credit on 2017/3/17.
 */

public final class Constants {

    public static final int FAVORT_ADD = 3; //添加收藏
    public static final int FAVORT_DELETE = 4; //取消收藏

    public static final int LANGUAGE_CUSTOM_MOVE = 5; //语言定制
    public static final int LANGUAGE_CUSTOM_DELETE = 6; //删除

    public static final int LANGUAGE_DATA_CHANGE = 7; //语言数据改变
    public static final int LANGUAGE_DATA_ADD = 8; //语言添加

    public static GithubResponse response;
    public static GithubResponse my;

    static {
        response = new GithubResponse();
        response.language = "java";
        response.name = "GithubTrends";
        response.description = "It's a GitHub Trending repositories Viewer with Material Design.";
        response.owner = "laowch";
        response.url = "https://github.com/laowch/GithubTrends";
        response.contributors = new ArrayList<>();
        User user = new User();
        user.avatar = "https://avatars0.githubusercontent.com/u/5444668";
        response.contributors.add(user);


        my = new GithubResponse();
        my.language = "java";
        my.name = "Github100";
        my.description = "当前项目是开源的，参考GithubTrends开发\\n 使用RxJava2+Retrofit2+Rxbus2等热门技术整合开发\\n\n" +
                "        bug反馈:creidt_yi@163.com \\n\n" +
                "        QQ：874951370\\n\n" +
                "        Github项目链接";
        my.owner = "creidt03";
        my.url = "https://github.com/credit03";
        my.contributors = new ArrayList<>();
        User user1 = new User();
        user1.avatar = "https://avatars1.githubusercontent.com/u/19606272";
        my.contributors.add(user1);
    }


}
