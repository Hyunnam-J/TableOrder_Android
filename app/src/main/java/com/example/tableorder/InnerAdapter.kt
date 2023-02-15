package com.example.tableorder

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.tableorder.databinding.ItemRecvInnerBinding
import com.example.tableorder.vo.ItemVO

class InnerAdapter(
    requireContext: Context,
    itemList: List<ItemVO>
) : Adapter<InnerAdapter.InnerViewHolder>(){

    var itemList: List<ItemVO>

    init {
        this.itemList = itemList
    }

    class InnerViewHolder(binding : ItemRecvInnerBinding) : ViewHolder(binding.root){

        var menu: TextView

        init {
            menu = binding.menu
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val binding = ItemRecvInnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerViewHolder(binding)
    }

    override fun onBindViewHolder(h: InnerViewHolder, i: Int) {
        h.menu.text = itemList[i].itemName2
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    //리사이클러 뷰 아이템 재활용으로 인한 데이터 꼬임 현상 방지
    override fun getItemViewType(position: Int): Int {
        return position
    }
}   //OuterAdapter-class