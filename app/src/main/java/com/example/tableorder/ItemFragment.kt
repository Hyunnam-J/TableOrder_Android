package com.example.tableorder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tableorder.databinding.FragmentItemBinding
import com.example.tableorder.retrofit.ApiClient
import com.example.tableorder.retrofit.ApiInterface
import com.example.tableorder.vo.ItemVO
import com.example.tableorder.vo.TabCodeVO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class ItemFragment(tabCodeVO: TabCodeVO) : Fragment() {

    private var _binding : FragmentItemBinding? = null
    private val binding get() = _binding!!

    private val apiInterface : ApiInterface = ApiClient.getApiClient().create(ApiInterface::class.java)
    private val coroutineScopeIO = CoroutineScope(Dispatchers.IO)
    lateinit var job: Job

    var tabCodeVO : TabCodeVO
    lateinit var itemList : List<ItemVO>

    init {
        this.tabCodeVO = tabCodeVO
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemBinding.inflate(inflater, container, false)

        //소메뉴를 불러오고,
        job = coroutineScopeIO.launch {
            val call : Call<String> = apiInterface.itemMenu("003", tabCodeVO.getpCode(), "101")
            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(response.isSuccessful){

                        itemList = Gson().fromJson(response.body(), object : TypeToken<ArrayList<ItemVO?>?>(){}.type)

                        //불러온 소메뉴를 어댑터로 그려준다.
                        //그리는 과정은 innerAdapter에서.
                        val innerAdapter = InnerAdapter(requireContext(), itemList)
                        val manager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

                        binding.innerRecv.adapter = innerAdapter
                        binding.innerRecv.layoutManager = manager

                    }else{
                        Toast.makeText(requireContext(), "통신 에러", Toast.LENGTH_LONG)
                    }
                    job.cancel()
                }   //override fun onResponse(call: Call<String>, response: Response<String>)

                override fun onFailure(call: Call<String>, t: Throwable) {
                    job.cancel()
                }
            })  //call.enqueue(object : Callback<String>
        }   //var job = coroutineScopeIO.launch
        return binding.root
    }   //override fun onCreateView

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}   //class ItemFragment