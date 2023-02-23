package com.example.tableorder.retrofit;

import com.example.tableorder.vo.basket.SendOrderVO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BasketApiInterface {

    @POST("api/basket/order")
    Call<String> order(
            @Body SendOrderVO sendOrderVO
    );

}   //interface ApiInterface
