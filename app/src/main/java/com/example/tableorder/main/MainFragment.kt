package com.example.tableorder.main

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.tableorder.*
import com.example.tableorder.basket.BasketFragment
import com.example.tableorder.databinding.FragmentMainBinding
import com.example.tableorder.retrofit.ApiClient
import com.example.tableorder.retrofit.MainApiInterface
import com.example.tableorder.setting.SettingFragment
import com.example.tableorder.vo.main.MainTabCodeVO
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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

    lateinit var tNumDialog : Dialog
    var isFirst : Boolean = true

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

        if(MainActivity.pref?.getString("selectOrderMode", "") == "이동식"){

            binding.settingTnum.isVisible = true

            binding.settingTnum.setOnClickListener{
//                tNumDialog = Dialog(context)
//                tNumDialog.setContentView(R.layout.activity_tnum_dialog)
//                SizingDialog().sizingDialog(tNumDialog, context, 0.4, 0.15)
//                tNumDialog.show()
//
//                tNumDialog.findViewById<EditText>(R.id.textTnum).setText(SettingFragment.settingPref?.getString("Table No", ""))
//                tNumDialog.findViewById<Button>(R.id.buttonTnum).setOnClickListener{
//                    SettingFragment.settingEditor?.putString("Table No", tNumDialog.findViewById<EditText>(R.id.textTnum).text.toString())
//                    SettingFragment.settingEditor?.commit()
//                    tNumDialog.dismiss()
//                }

                tNumDialog = Dialog(context)
                tNumDialog.setContentView(R.layout.activity_keyboard_dialog)
                SizingDialog().sizingDialog(tNumDialog, context, 0.4, 0.4)
                tNumDialog.show()

                isFirst = true

                tNumDialog.findViewById<TextView>(R.id.tNumView).text =
                    MainActivity.pref?.getString("Table No", "")

                tNumDialog.findViewById<Button>(R.id.one).setOnClickListener{ enterKey("1") }
                tNumDialog.findViewById<Button>(R.id.two).setOnClickListener{ enterKey("2") }
                tNumDialog.findViewById<Button>(R.id.three).setOnClickListener{ enterKey("3") }
                tNumDialog.findViewById<Button>(R.id.four).setOnClickListener{ enterKey("4") }
                tNumDialog.findViewById<Button>(R.id.five).setOnClickListener{ enterKey("5") }
                tNumDialog.findViewById<Button>(R.id.six).setOnClickListener{ enterKey("6") }
                tNumDialog.findViewById<Button>(R.id.seven).setOnClickListener{ enterKey("7") }
                tNumDialog.findViewById<Button>(R.id.eight).setOnClickListener{ enterKey("8") }
                tNumDialog.findViewById<Button>(R.id.nine).setOnClickListener{ enterKey("9") }
                tNumDialog.findViewById<Button>(R.id.zero).setOnClickListener{ enterKey("0") }

                tNumDialog.findViewById<Button>(R.id.tNumDelete).setOnClickListener{
                    tNumDialog.findViewById<TextView>(R.id.tNumView).text =
                        removeLastChar(tNumDialog.findViewById<TextView>(R.id.tNumView).text.toString())
                }

                tNumDialog.findViewById<Button>(R.id.tNumEnter).setOnClickListener{

                    if(tNumDialog.findViewById<TextView>(R.id.tNumView).text.toString()==""){
                        Toast.makeText(context, "값을 입력해 주세요", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }

                    if(tNumDialog.findViewById<TextView>(R.id.tNumView).text.toString().substring(0, 1)=="0"){
                        Toast.makeText(context, "잘못된 테이블 번호 형식입니다", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }

                    MainActivity.editor?.putString("Table No", tNumDialog.findViewById<TextView>(R.id.tNumView).text.toString())
                    MainActivity.editor?.commit()

                    tNumDialog.dismiss()
                }
            }   //binding.settingTnum.setOnClickListener
        }   //if(SettingFragment.settingPref?.getString("selectMode", "") == "이동식")

        //먼저 대메뉴를 불러오고,
        job = coroutineScopeIO.launch {
            val call : Call<String> = apiInterface.tabMenu(
                MainActivity.pref?.getString("Com ID", "").toString(),
                "1",
                MainActivity.pref?.getString("Pos", "").toString())
            call.enqueue(object : Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {

                    if(response.isSuccessful){

                        val respList : RespList<MainTabCodeVO> = Gson().fromJson(response.body(), object : TypeToken<RespList<MainTabCodeVO?>?>(){}.type)

                        if(respList.resultCode==1){
                            try {

                                tabList = respList.item as ArrayList<MainTabCodeVO>

                                viewPager.adapter = ViewPagerAdapter(parentFragmentManager, lifecycle, tabList, map)

                                //대메뉴인 tabLayout과 소메뉴 뷰 페이지인 viewPager를 합쳐준다.
                                //소메뉴 뷰에 관한 건 viewPager에서 설정.
                                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                                    tab.text = tabList[position].getpName()
                                }.attach()

                            }catch (e : java.lang.Exception){

                                binding.errorFrameLayout.removeView(binding.viewPager)
                                goOnErrorPage(respList.resultMsg)

                            }   //try-catch
                        }else{
                            Toast.makeText(context, "데이터베이스 처리 중 오류", Toast.LENGTH_LONG).show()
                        }   //if(respList.resultCode==1)

                    }else{
                        Toast.makeText(context, "통신 에러", Toast.LENGTH_LONG).show()
                    }   //if(response.isSuccessful)
                    job.cancel()
                }   //override fun onResponse(call: Call<String>, response: Response<String>)

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(context, "통신 에러", Toast.LENGTH_LONG).show()
                    job.cancel()
                }
            })  //call.enqueue(object : Callback<String>
        }   //var job = coroutineScopeIO.launch

        /*
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
        */

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

    fun goOnErrorPage(resultMsg : String){
        binding.errorFrameLayout.addView(createTextv(resultMsg))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun enterKey(num : String) {

        if(isFirst){
            tNumDialog.findViewById<TextView>(R.id.tNumView).text = num
            isFirst = false
        }else{
            tNumDialog.findViewById<TextView>(R.id.tNumView).text =
                tNumDialog.findViewById<TextView>(R.id.tNumView).text.toString() + num
        }
    }

    fun removeLastChar(str: String?): String? {
        return str?.replaceFirst(".$".toRegex(), "")
    }
}   //class MainFragment



