package com.guoyi.github.bean;

import com.google.gson.annotations.SerializedName;
import com.guoyi.github.MyApplication;
import com.guoyi.github.utils.rxbus2.RxBus;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lao on 15/9/23.
 */
public class GithubResponse implements Serializable {

    public String name;

    public String owner;

    @SerializedName("link")
    public String url;

    @SerializedName("des")
    public String description;
    public String meta;

    public String language;

    public String path;

    public List<User> contributors;

    @Override
    public boolean equals(Object o) {
        if (o instanceof GithubResponse) {
            return name.equals(((GithubResponse) o).name) && owner.equals(((GithubResponse) o).owner);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + owner.hashCode();
    }


    public void sendRxBus(int type) {
        MyApplication.log("准备发送事件：" + type);
        RxBus.get()
                .withKey(type)
                .send(this);
    }

    @Override
    public String toString() {
        return "GithubResponse{" +
                "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", meta='" + meta + '\'' +
                ", language='" + language + '\'' +
                ", contributors=" + contributors +
                '}';
    }
}
