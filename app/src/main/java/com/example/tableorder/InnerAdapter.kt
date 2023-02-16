package com.example.tableorder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.tableorder.databinding.ItemRecvInnerBinding
import com.example.tableorder.vo.ItemVO
import java.io.File

class InnerAdapter(
    requireContext: Context,
    itemList: List<ItemVO>
) : Adapter<InnerAdapter.InnerViewHolder>(){

    var itemList: List<ItemVO>

    init {
        this.itemList = itemList
    }

    class InnerViewHolder(binding : ItemRecvInnerBinding) : ViewHolder(binding.root){

        var itemNameEven: TextView
        var uPriceEven: TextView
        var itemImageEven: ImageView

        var itemNameOdd: TextView
        var uPriceOdd: TextView
        var itemImageOdd: ImageView

        init {
            itemNameEven = binding.itemNameEven
            uPriceEven = binding.uPriceEven
            itemImageEven = binding.itemImageEven

            itemNameOdd = binding.itemNameOdd
            uPriceOdd = binding.uPriceOdd
            itemImageOdd = binding.itemImageOdd
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val binding = ItemRecvInnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerViewHolder(binding)
    }

    override fun onBindViewHolder(h: InnerViewHolder, i: Int) {

        val path = "D:\\TableOrderItemImage\\"

        if(i % 2 == 0){
            h.itemNameEven.text = itemList[i].itemName2
            h.uPriceEven.text = itemList[i].getuPrice()

//            val file = File(path+itemList[i].getbColor()+".png")
//            val b = file.exists()
//            if(b){
//                val op : BitmapFactory.Options = BitmapFactory.Options()
//                val bm : Bitmap = BitmapFactory.decodeFile(file.path, op)
//                h.itemImageEven.setImageBitmap(bm)
//            }


        }else{
            h.itemNameOdd.text = itemList[i].itemName2
            h.uPriceOdd.text = itemList[i].getuPrice()

            val file = File(path+itemList[i].getbColor()+".png")
            val op : BitmapFactory.Options = BitmapFactory.Options()
            val bm : Bitmap = BitmapFactory.decodeFile(file.path, op)
            h.itemImageOdd.setImageBitmap(bm)

        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    //리사이클러 뷰 아이템 재활용으로 인한 데이터 꼬임 현상 방지
    override fun getItemViewType(position: Int): Int {
        return position
    }
}   //OuterAdapter-class