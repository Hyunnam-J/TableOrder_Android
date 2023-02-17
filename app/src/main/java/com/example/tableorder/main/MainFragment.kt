package com.example.tableorder.main

import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tableorder.R
import com.example.tableorder.basket.BasketFragment
import com.example.tableorder.databinding.FragmentMainBinding
import com.example.tableorder.retrofit.ApiClient
import com.example.tableorder.retrofit.MainApiInterface
import com.example.tableorder.vo.main.MainTabCodeVO
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class MainFragment : Fragment() {

    val TAG = "로그"

    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val apiInterface : MainApiInterface = ApiClient.getApiClient().create(
        MainApiInterface::class.java)
    private val coroutineScopeIO = CoroutineScope(Dispatchers.IO)
    lateinit var job: Job

    lateinit var tabList : List<MainTabCodeVO>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)

        //먼저 대메뉴를 불러오고,
        job = coroutineScopeIO.launch {
            val call : Call<String> = apiInterface.tabMenu("003", "1", "101")
            call.enqueue(object : Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(response.isSuccessful){
                        tabList = Gson().fromJson(response.body(), object : TypeToken<ArrayList<MainTabCodeVO?>?>(){}.type)

                        val viewPager = binding.viewPager
                        val tabLayout = binding.tabLayout

                        viewPager.adapter = ViewPagerAdapter(parentFragmentManager, lifecycle, tabList)

                        //대메뉴인 tabLayout과 소메뉴 뷰 페이지인 viewPager를 합쳐준다.
                        //소메뉴 뷰에 관한 건 viewPager에서 설정.
                        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                            tab.text = tabList[position].getpName()
                        }.attach()

                    }else{
                        Toast.makeText(requireContext(), "통신 에러", Toast.LENGTH_LONG)
                    }
                    job.cancel()
                }   //override fun onResponse(call: Call<String>, response: Response<String>)

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(requireContext(), "통신 에러", Toast.LENGTH_LONG)
                    job.cancel()
                }
            })  //call.enqueue(object : Callback<String>
        }   //var job = coroutineScopeIO.launch

        binding.back.setOnClickListener{
            val manager = activity?.supportFragmentManager
            manager?.beginTransaction()?.remove(this)?.commit()
            manager?.popBackStack()
        }

        binding.basket.setOnClickListener{
            parentFragmentManager.beginTransaction().replace(R.id.container, BasketFragment()).commit()
        }

        return binding.root
    }   //onCreateView

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}   //class MainFragment