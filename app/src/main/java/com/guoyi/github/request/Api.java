package com.guoyi.github.request;

import com.guoyi.github.bean.GithubResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Credit on 2017/3/16.
 */

public interface Api {

    @GET("{type}_{span}")
    Observable<List<GithubResponse>> getData(@Path("type") String type, @Path("span") String span);
}
