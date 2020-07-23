package com.aylwin.yo_se_eso.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HelperWs {

    public static Retrofit getConfiguration(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://aylwin100-001-site1.itempurl.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit;
    }
}
