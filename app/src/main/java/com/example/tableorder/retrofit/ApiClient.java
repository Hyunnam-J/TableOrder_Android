package com.example.tableorder.retrofit;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {

    static String baseUrl = "http://192.168.0.8/";
    static Retrofit retrofit;

    public static Retrofit getApiClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
//                    .client(new OkHttpClient.Builder().readTimeout(10, TimeUnit.SECONDS).build())
                .build();
        return retrofit;
    }
}
