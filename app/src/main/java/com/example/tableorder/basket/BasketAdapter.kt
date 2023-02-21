package com.example.tableorder.basket

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.tableorder.R
import com.example.tableorder.databinding.ItemRecvBasketBinding
import com.example.tableorder.vo.basket.BasketVO
import java.text.NumberFormat
import java.util.*
import kotlin.collections.HashMap

class BasketAdapter(
    requireContext: Context,
    map: HashMap<String, Any>,
    basketList: List<BasketVO>,
    basketFragment: BasketFragment
) : Adapter<BasketAdapter.BasketViewHolder>(){

    private val context : Context
    val map : HashMap<String, Any>
    val basketList : List<BasketVO>
    val basketFragment : BasketFragment

    init {
        this.context = requireContext
        this.map = map
        this.basketList = basketList
        this.basketFragment = basketFragment
    }

    class BasketViewHolder(binding: ItemRecvBasketBinding) : ViewHolder(binding.root) {

        var itemQuantity : TextView
        var itemName : TextView
        var itemTotalPrice : TextView

        var addQuantity : Button
        var removeQuantity : Button

        init {
            itemQuantity = binding.itemQuantity
            itemName = binding.itemName
            itemTotalPrice = binding.itemTotalPrice
            addQuantity = binding.addQuantity
            removeQuantity = binding.removeQuantity
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        val binding = ItemRecvBasketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BasketViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(h: BasketViewHolder, i: Int) {
        h.itemQuantity.text = basketList[i].quantity.toString()+"개"
        h.itemName.text = basketList[i].itemName2
        h.itemTotalPrice.text =
            NumberFormat.getCurrencyInstance(Locale.KOREA).format(Integer.parseInt(basketList[i].getuPrice()) * basketList[i].quantity)
        h.addQuantity.setOnClickListener{
            Log.d("로그", "onBindViewHolder: "+h.itemQuantity.text)
            if(h.itemQuantity.text == "1개") h.removeQuantity.setBackgroundResource(R.drawable.ic_baseline_remove_circle_24)

            basketList[i].quantity = basketList[i].quantity+1

            h.itemQuantity.text = basketList[i].quantity.toString()+"개"
            h.itemTotalPrice.text =
                NumberFormat.getCurrencyInstance(Locale.KOREA).format(Integer.parseInt(basketList[i].getuPrice()) * basketList[i].quantity)

            map.replace(
                basketList[i].itemCode,
                BasketVO(
                    basketList[i].comId,
                    basketList[i].getpName(),
                    basketList[i].itemName2,
                    basketList[i].pos,
                    basketList[i].getpCode(),
                    basketList[i].tabNo,
                    basketList[i].itemCode,
                    basketList[i].getuPrice(),
                    basketList[i].getbColor(),
                    basketList[i].quantity
                )   //BasketVO
            )   //map.replace
            basketFragment.calcTotalPrice()
        }   //h.addQuantity.setOnClickListener

        h.removeQuantity.setOnClickListener{

            if(basketList[i].quantity==1)return@setOnClickListener

            basketList[i].quantity = basketList[i].quantity-1

            h.itemQuantity.text = basketList[i].quantity.toString()+"개"
            h.itemTotalPrice.text = ( Integer.parseInt(basketList[i].getuPrice()) * basketList[i].quantity ).toString()

            map.replace(
                basketList[i].itemCode,
                BasketVO(
                    basketList[i].comId,
                    basketList[i].getpName(),
                    basketList[i].itemName2,
                    basketList[i].pos,
                    basketList[i].getpCode(),
                    basketList[i].tabNo,
                    basketList[i].itemCode,
                    basketList[i].getuPrice(),
                    basketList[i].getbColor(),
                    basketList[i].quantity
                )   //BasketVO
            )   //map.replace

            if(h.itemQuantity.text == "1개") h.removeQuantity.setBackgroundResource(R.drawable.ic_baseline_remove_circle_24_unactivate)

            basketFragment.calcTotalPrice()
        }   //h.removeQuantity.setOnClickListener



    }   //onBindViewHolder

    override fun getItemCount(): Int {
        return basketList.size
    }

}   //class