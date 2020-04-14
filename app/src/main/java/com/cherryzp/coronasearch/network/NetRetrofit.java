package com.cherryzp.coronasearch.network;

import com.cherryzp.coronasearch.service.CoronaService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetRetrofit {

    private Retrofit retrofit;
    private CoronaService coronaService;
    public static NetRetrofit netRetrofit = new NetRetrofit();

    public NetRetrofit() {
        retrofit = new Retrofit.Builder().baseUrl("https://openapi.naver.com/v1/search/").addConverterFactory(GsonConverterFactory.create()).build();

        coronaService = retrofit.create(CoronaService.class);
    }

    public static NetRetrofit getInstance() {
        return netRetrofit;
    }

    public CoronaService getCoronaService() {
        return coronaService;
    }


}
