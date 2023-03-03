package com.example.tableorder

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.tableorder.databinding.ActivityMainBinding
import com.example.tableorder.main.MainFragment
import com.example.tableorder.retrofit.ApiClient
import com.example.tableorder.retrofit.SettingApiInterface
import com.example.tableorder.setting.SettingFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity() : AppCompatActivity() {

    val TAG = "로그"
    private lateinit var binding : ActivityMainBinding
    var map : HashMap<String, Any> = HashMap()
    lateinit var dialog : Dialog
    lateinit var job: Job
    private val coroutineScopeIO = CoroutineScope(Dispatchers.IO)
    private val apiInterface : SettingApiInterface = ApiClient.getApiClient().create(
        SettingApiInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startSettingFragment()

        val animMain = AnimationUtils.loadAnimation(this, R.anim.anim_main)
        binding.enter.startAnimation(animMain)

        dialog = this?.let { Dialog(it) }!!

        binding.enter.setOnClickListener{

            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, MainFragment(map)).commit()

        }   //binding.enter.setOnClickListener

        binding.setting.setOnClickListener{
            enterSetting()
        }
    }   //onCreate

    fun startSettingFragment(){
        val settingFragment = SettingFragment()

        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, settingFragment).commit()

        val manager = this.supportFragmentManager
        manager.beginTransaction().remove(settingFragment).commit()
        manager.popBackStack()
    }

    fun enterSetting(){

        dialog!!.setContentView(R.layout.activity_check_password_dialog)
        sizingDialog()
        dialog.show()

        dialog.findViewById<Button>(R.id.checkPasswordCancel).setOnClickListener{
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.checkPasswordConfirm).setOnClickListener{

            job = coroutineScopeIO.launch {

                //설정된 회사 아이디 값과 포스 값 별로 비밀번호를 조회한다
                val call : Call<String> = apiInterface.checkPassword(SettingFragment.comId, SettingFragment.pos)
                call.enqueue(object : Callback<String>{
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if(response.isSuccessful){

                            //디비에 저장된 가져온 비밀번호와 입력한 비밀번호가 같은지
                            if(response.body()==dialog.findViewById<EditText>(R.id.checkPassword).text.toString()){

                                dialog.dismiss()

                                supportFragmentManager.beginTransaction()
                                    .replace(binding.container.id, SettingFragment()).commit()

                            }else{
                                dialog.dismiss()
                                Toast.makeText(this@MainActivity, "비밀번호가 다릅니다", Toast.LENGTH_LONG).show()
                            }
                        }else{
                            Toast.makeText(this@MainActivity, "통신 장애", Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "통신 장애", Toast.LENGTH_LONG).show()
                    }
                })
                job.cancel()
            }   //job = coroutineScopeIO.launch

            dialog.dismiss()
        }
    }   //enterSetting()

    fun sizingDialog(){

        val windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        val deviceHeight = size.y

        params?.width = (deviceWidth * 0.7).toInt()
        params?.height = (deviceHeight * 0.15).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }
}   //class


