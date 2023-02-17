package com.example.tableorder.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.tableorder.databinding.ItemRecvInnerBinding
import com.example.tableorder.vo.basket.BasketItemVO
import com.example.tableorder.vo.main.MainItemVO
import java.net.URL

class InnerAdapter(
    context: Context,
    itemList: List<MainItemVO>
) : Adapter<InnerAdapter.InnerViewHolder>(){

    private var itemList: List<MainItemVO>
    var context: Context

    var toast: Toast? = null

    val baskeList = ArrayList<BasketItemVO>()

    init {
        this.itemList = itemList
        this.context = context
    }

    class InnerViewHolder(binding : ItemRecvInnerBinding) : ViewHolder(binding.root){

        var itemName: TextView
        var uPrice: TextView
        var itemImage: ImageView
        var menu: LinearLayout

        init {
            itemName = binding.itemName
            uPrice = binding.uPrice
            itemImage = binding.itemImage
            menu = binding.menu
        }
    }   //InnerViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val binding = ItemRecvInnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerViewHolder(binding)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(h: InnerViewHolder, i: Int) {

        h.itemName.text = itemList[i].itemName2
        h.uPrice.text = itemList[i].getuPrice()

        var url = URL("http://192.168.0.8/static/TableOrderItemImage/"+itemList[i].getbColor()+".png")
        Glide.with(context).load(url).into(h.itemImage)

        h.menu.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    h.menu.setBackgroundColor(Color.parseColor("#75ABB8"))
                }
                else -> {
                    h.menu.setBackgroundColor(Color.WHITE)
                    showToast(itemList[i].itemName2+"하나가 장바구니에 담겼습니다")

                    //장바구니 추가 로직




               }
            }   //when event?.action
            //리턴값이 false면 seekbar 동작 안됨
            true //or false
        }   //menu.setOnTouchListener


    }   //onBindViewHolder

    override fun getItemCount(): Int {
        return itemList.size
    }

    //리사이클러 뷰 아이템 재활용으로 인한 데이터 꼬임 현상 방지
    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun showToast(string: String){
        toast?.cancel()
        toast = Toast.makeText(context, string, Toast.LENGTH_SHORT)
        toast?.show()
    }
}   //OuterAdapter-class