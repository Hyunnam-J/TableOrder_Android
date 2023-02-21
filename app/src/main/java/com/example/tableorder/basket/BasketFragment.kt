package com.example.tableorder.basket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.tableorder.R
import com.example.tableorder.databinding.FragmentBasketBinding
import com.example.tableorder.main.MainFragment
import com.example.tableorder.vo.basket.BasketVO
import com.google.gson.Gson
import java.text.NumberFormat
import java.util.*
import kotlin.collections.HashMap

class BasketFragment(map: HashMap<String, Any>) : Fragment() {

    val TAG = "로그"

    private var _binding : FragmentBasketBinding? = null
    private val binding get() = _binding!!

    var map : HashMap<String, Any> = HashMap()
    var basketList : List<BasketVO>? = null

    init {
        this.map = map
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBasketBinding.inflate(inflater, container, false)

        binding.back.setOnClickListener{
            parentFragmentManager.beginTransaction().replace(R.id.container, MainFragment(map)).commit()
        }

        basketList = map.values.toList() as List<BasketVO>

        if(basketList!!.size == 0){
            binding.basketLinearLayout.removeView(binding.basketRecv)
            binding.basketFrameLayout.setBackgroundResource(R.drawable.ic_launcher_foreground)
        }else{
            val adapter = BasketAdapter(requireContext(), map, basketList!!, this)
            val manager = LinearLayoutManager(requireContext(), VERTICAL, false)

            binding.basketRecv.adapter = adapter
            binding.basketRecv.layoutManager = manager

            calcTotalPrice()
        }

        binding.order.setOnClickListener{
            //주문 로직
            val s = Gson().toJson(basketList)
            val a = Gson().toJson(map)
        }

        return binding.root
    }   //onCreateView

    public fun calcTotalPrice() {
        var tempSum : Int = 0

        for(i in 0 until map.size){
            tempSum += basketList!![i].quantity * Integer.parseInt(basketList!![i].getuPrice())
        }

        binding.totalPrice.text = NumberFormat.getCurrencyInstance(Locale.KOREA).format(Integer.parseInt(tempSum.toString()))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}   //BasketFragment