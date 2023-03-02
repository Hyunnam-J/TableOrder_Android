package com.example.tableorder.main

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tableorder.Resp
import com.example.tableorder.databinding.FragmentItemBinding
import com.example.tableorder.retrofit.ApiClient
import com.example.tableorder.retrofit.MainApiInterface
import com.example.tableorder.setting.SettingFragment
import com.example.tableorder.vo.main.MainItemVO
import com.example.tableorder.vo.main.MainTabCodeVO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ItemFragment(tabCodeVO: MainTabCodeVO, map: HashMap<String, Any>) : Fragment() {

    private var _binding : FragmentItemBinding? = null
    private val binding get() = _binding!!

    private val apiInterface : MainApiInterface = ApiClient.getApiClient().create(
        MainApiInterface::class.java)
    private val coroutineScopeIO = CoroutineScope(Dispatchers.IO)
    lateinit var job: Job

    var tabCodeVO : MainTabCodeVO
    lateinit var itemList : ArrayList<MainItemVO>

    var map : HashMap<String, Any> = HashMap()

    init {
        this.tabCodeVO = tabCodeVO
        this.map = map
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemBinding.inflate(inflater, container, false)

        val context = requireContext()

        val settingFragment = SettingFragment

        //소메뉴를 불러오고,
        job = coroutineScopeIO.launch {
            val call : Call<String> = apiInterface.itemMenu(settingFragment.comId, tabCodeVO.getpCode(), settingFragment.pos)
            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(response.isSuccessful){

                        val resp : Resp<MainItemVO> = Gson().fromJson(response.body(), object : TypeToken<Resp<MainItemVO?>?>(){}.type)

                        try {

                            itemList = resp.item as ArrayList<MainItemVO>

                            //불러온 소메뉴를 어댑터로 그려준다.
                            //그리는 과정은 innerAdapter에서.
                            val innerAdapter = InnerAdapter(context, itemList, map)

                            if(itemList.size < 5){
                                val innerManager = GridLayoutManager(context, 1, RecyclerView.HORIZONTAL, false)

                                binding.innerRecv.adapter = innerAdapter
                                binding.innerRecv.layoutManager = innerManager
                            }else{
                                val innerManager = GridLayoutManager(context, 4, RecyclerView.VERTICAL, false)

                                binding.innerRecv.adapter = innerAdapter
                                binding.innerRecv.layoutManager = innerManager
                            }

                        }catch (e : java.lang.Exception){

                            binding.itemFrameLayout.removeView(binding.innerRecv)
                            binding.itemFrameLayout.setBackgroundColor(Color.WHITE)
                            displayTextv(resp.resultMsg)

                        }

                    }else{
                        Toast.makeText(context, "통신 에러", Toast.LENGTH_LONG)
                    }
                    job.cancel()
                }   //override fun onResponse(call: Call<String>, response: Response<String>)

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(context, "통신 에러", Toast.LENGTH_LONG)
                    job.cancel()
                }
            })  //call.enqueue(object : Callback<String>
        }   //var job = coroutineScopeIO.launch
        return binding.root
    }   //override fun onCreateView

    fun createTextv(resultMsg : String) : View{
        val textv = TextView(requireContext())
        textv.text = resultMsg

        val lp = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
        }

        textv.layoutParams = lp

        textv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 50F)

        //imgv.id = ViewCompat.generateViewId()
        return textv
    }

    public fun displayTextv(resultMsg : String){
        binding.itemFrameLayout.addView(createTextv(resultMsg))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}   //class ItemFragment