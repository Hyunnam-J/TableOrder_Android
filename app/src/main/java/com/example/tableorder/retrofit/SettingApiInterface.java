package com.example.tableorder.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SettingApiInterface {

//    @GET("posts/{post}")
//    Call<String> getTeset(@Path("post") String get);
//    val call : Call<String> = apiInterface.getTeset("1")

    @GET("api/setting/checkPassword")
    Call<String> checkPassword(
            @Query("comId") String comId,
            @Query("pos") String pos
    );

}   //interface ApiInterface
