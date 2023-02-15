package com.example.tableorder.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

//    @GET("posts/{post}")
//    Call<String> getTeset(@Path("post") String get);
//    val call : Call<String> = apiInterface.getTeset("1")

    @GET("api/tabMenu")
    Call<String> tabMenu(
            @Query("comId") String comId,
            @Query("tabUse") String tabUse,
            @Query("pos") String pos
    );

    @GET("api/itemMenu")
    Call<String> itemMenu(
            @Query("comId") String comId,
            @Query("pCode") String pCode,
            @Query("pos") String pos
    );

}   //interface ApiInterface
