package com.cherryzp.coronasearch.service;

import com.cherryzp.coronasearch.pojo.CoronaNews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface CoronaService {

    @GET("news.json?query=%EC%BD%94%EB%A1%9C%EB%82%98&")
    Call<CoronaNews> doGetCoronaNews(@Query("display") int display, @Query("start") int start, @Query("sort") String sort
                                        , @Header("X-Naver-Client-Id") String id,@Header("X-Naver-Client-Secret") String secret);


}
