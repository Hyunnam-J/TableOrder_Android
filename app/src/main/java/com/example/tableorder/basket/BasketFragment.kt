package com.example.tableorder.basket

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.tableorder.R
import com.example.tableorder.databinding.FragmentBasketBinding
import com.example.tableorder.main.MainFragment
import com.example.tableorder.retrofit.ApiClient
import com.example.tableorder.retrofit.BasketApiInterface
import com.example.tableorder.vo.basket.BasketVO
import com.example.tableorder.vo.basket.SendOrderVO
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*
import kotlin.collections.HashMap

class BasketFragment(map: HashMap<String, Any>) : Fragment() {

    val TAG = "로그"

    private var _binding : FragmentBasketBinding? = null
    private val binding get() = _binding!!

    var map : HashMap<String, Any> = HashMap()
    var basketList : List<BasketVO>? = null

    var coroutineScopeIO = CoroutineScope(Dispatchers.IO)
    lateinit var job: Job
    val apiInterface : BasketApiInterface = ApiClient.getApiClient().create(
        BasketApiInterface::class.java)

    init {
        this.map = map
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBasketBinding.inflate(inflater, container, false)

        val context = requireContext()

        binding.back.setOnClickListener{
            parentFragmentManager.beginTransaction().replace(R.id.container, MainFragment(map)).commit()
        }

        basketList = map.values.toList() as List<BasketVO>

        if(basketList!!.isEmpty()){
            displayImgv()
        }else{
            refreshBasketList()
        }

        binding.order.setOnClickListener{

            if(basketList!!.isEmpty()){
                Toast.makeText(context, "장바구니에 물건이 없습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            job = coroutineScopeIO.launch {

                val call : Call<String> = apiInterface.order(SendOrderVO(basketList!!, 1, 0))
                call.enqueue(object : Callback<String>{
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if(response.isSuccessful){
                            if(response.body()=="success"){
                                Toast.makeText(context, "주문이 완료되었습니다", Toast.LENGTH_LONG).show()

                                //프린트

                                //장바구니 비우고
                                map.clear()

                                //프래그먼트 지운다
                                val manager = activity?.supportFragmentManager
                                manager?.beginTransaction()?.remove(this@BasketFragment)?.commit()
                                manager?.popBackStack()

                            }else{
                                Toast.makeText(context, "통신 장애", Toast.LENGTH_SHORT).show()
                                Toast.makeText(context, "다시 주문해 주십시오", Toast.LENGTH_SHORT).show()
                                parentFragmentManager.beginTransaction().replace(R.id.container, MainFragment(map)).commit()
                            }
                        }else{
                            Toast.makeText(context, "통신 장애", Toast.LENGTH_LONG)
                            parentFragmentManager.beginTransaction().replace(R.id.container, MainFragment(map)).commit()
                        }
                        job.cancel()
                    }   //onResponse

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(context, "통신 장애", Toast.LENGTH_LONG)
                        job.cancel()
                    }
                })  //call.enqueue
            }   //job = coroutineScopeIO.launch
        }   //binding.order.setOnClickListener
        return binding.root
    }   //onCreateView

    public fun calcTotalPrice() {
        var tempSum : Int = 0

        for(i in 0 until map.size){
            tempSum += basketList!![i].quantity * basketList!![i].getuPrice()
        }

        binding.totalPrice.text = NumberFormat.getCurrencyInstance(Locale.KOREA).format(Integer.parseInt(tempSum.toString()))
    }

    fun createImgv() : View{
        val imgv = ImageView(requireContext())
        imgv.setBackgroundResource(R.drawable.teong)

        val lp = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
        }

        imgv.layoutParams = lp

        //imgv.id = ViewCompat.generateViewId()
        return imgv
    }

    public fun displayImgv(){
        binding.basketFrameLayout.addView(createImgv())
    }

    fun refreshBasketList(){
        basketList = map.values.toList() as List<BasketVO>

        val adapter = BasketAdapter(requireContext(), map, basketList!!, this)
        val manager = LinearLayoutManager(requireContext(), VERTICAL, false)

        binding.basketRecv.adapter = adapter
        binding.basketRecv.layoutManager = manager

        calcTotalPrice()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}   //BasketFragment