package com.example.tableorder.main

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.tableorder.MyProgressDialog
import com.example.tableorder.R
import com.example.tableorder.Resp
import com.example.tableorder.basket.BasketFragment
import com.example.tableorder.databinding.FragmentMainBinding
import com.example.tableorder.retrofit.ApiClient
import com.example.tableorder.retrofit.MainApiInterface
import com.example.tableorder.setting.SettingFragment
import com.example.tableorder.vo.main.MainTabCodeVO
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment(map: HashMap<String, Any>) : Fragment() {

    val TAG = "로그"

    private var _binding : FragmentMainBinding? = null
    val binding get() = _binding!!

    private val apiInterface : MainApiInterface = ApiClient.getApiClient().create(
        MainApiInterface::class.java)
    private val coroutineScopeIO = CoroutineScope(Dispatchers.IO)
    lateinit var job: Job

    lateinit var tabList : List<MainTabCodeVO>

    var map : HashMap<String, Any> = HashMap()

    init {
        this.map = map
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)

        val viewPager = binding.viewPager
//        viewPager.isUserInputEnabled = false    //뷰페이저 스크롤 차단
        val tabLayout = binding.tabLayout

        val context: Context = requireContext()

        //먼저 대메뉴를 불러오고,
        job = coroutineScopeIO.launch {
            val call : Call<String> = apiInterface.tabMenu(SettingFragment.comId, "1", SettingFragment.pos)
            call.enqueue(object : Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {

                    val progressDialog = MyProgressDialog(context)
                    progressDialog.show()






                    if(response.isSuccessful){



                        val resp : Resp<MainTabCodeVO> = Gson().fromJson(response.body(), object : TypeToken<Resp<MainTabCodeVO?>?>(){}.type)

                        try {

                            tabList = resp.item as ArrayList<MainTabCodeVO>

                            viewPager.adapter = ViewPagerAdapter(parentFragmentManager, lifecycle, tabList, map)

                            //대메뉴인 tabLayout과 소메뉴 뷰 페이지인 viewPager를 합쳐준다.
                            //소메뉴 뷰에 관한 건 viewPager에서 설정.
                            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                                tab.text = tabList[position].getpName()
                            }.attach()

                        }catch (e : java.lang.Exception){

                            binding.errorFrameLayout.removeView(binding.viewPager)
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

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabSelected: 셀렉")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabSelected: 셀렉 해제")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabSelected: 다시 셀렉")
            }
        })  //binding.tabLayout.addOnTabSelectedListener

        binding.back.setOnClickListener{
            map.clear()

            val manager = activity?.supportFragmentManager
            manager?.beginTransaction()?.remove(this)?.commit()
            manager?.popBackStack()
        }

        binding.basket.setOnClickListener{
            parentFragmentManager.beginTransaction().replace(R.id.container, BasketFragment(map)).commit()
        }

        return binding.root
    }   //onCreateView

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
        binding.errorFrameLayout.addView(createTextv(resultMsg))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}   //class MainFragment

