package com.example.tableorder.vo.basket

data class SendOrderVO (

    var basketList: List<BasketVO>,
    var tNum : Int,
    var subTotAmt : Int

)