package com.example.tableorder.retrofit;

import com.example.tableorder.vo.basket.SendOrderVO;
import com.example.tableorder.vo.basket.SendPayVO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BasketApiInterface {

    @POST("api/basket/order")
    Call<String> order(
            @Body SendOrderVO sendOrderVO
    );

    @POST("api/basket/pay")
    Call<String> pay(
            @Body SendPayVO sendPayVO
    );

}   //interface ApiInterface
