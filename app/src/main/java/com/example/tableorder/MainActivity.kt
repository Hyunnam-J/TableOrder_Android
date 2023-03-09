package com.example.tableorder

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    lateinit var binding : ActivityMainBinding
    var map : HashMap<String, Any> = HashMap()
    lateinit var checkPasswordDialog : Dialog
    lateinit var job: Job
    private val coroutineScopeIO = CoroutineScope(Dispatchers.IO)
    private val apiInterface : SettingApiInterface = ApiClient.getApiClient().create(
        SettingApiInterface::class.java)

    companion object{
        var pref : SharedPreferences? = null
        var editor : SharedPreferences.Editor? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = this?.getSharedPreferences("pref", 0)!!
        editor = pref?.edit()!!

        val animMain = AnimationUtils.loadAnimation(this, R.anim.anim_main)
        binding.enter.startAnimation(animMain)

        checkPasswordDialog = this?.let { Dialog(it) }!!

        binding.enter.setOnClickListener{

            val myProgressDialog = MyProgressDialog(this)
            myProgressDialog.show()

            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, MainFragment(map)).commit()

            myProgressDialog.dismiss()

        }   //binding.enter.setOnClickListener

        binding.setting.setOnClickListener{
            enterSetting()
        }
    }   //onCreate

    fun enterSetting(){

        checkPasswordDialog!!.setContentView(R.layout.activity_check_password_dialog)

        SizingDialog().sizingDialog(checkPasswordDialog, this, 0.7, 0.15)

        checkPasswordDialog.show()

        checkPasswordDialog.findViewById<Button>(R.id.checkPasswordCancel).setOnClickListener{
            checkPasswordDialog.dismiss()
        }

        checkPasswordDialog.findViewById<Button>(R.id.checkPasswordConfirm).setOnClickListener{

            job = coroutineScopeIO.launch {

                //설정된 회사 아이디 값과 포스 값 별로 비밀번호를 조회한다
                val call : Call<String> = apiInterface.checkPassword(
                    pref?.getString("Com ID", "").toString(),
                    pref?.getString("Pos", "").toString())
                call.enqueue(object : Callback<String>{
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if(response.isSuccessful){

                            //디비에 저장된 가져온 비밀번호와 입력한 비밀번호가 같은지
                            if(response.body()==checkPasswordDialog.findViewById<EditText>(R.id.checkPassword).text.toString()){

                                checkPasswordDialog.dismiss()

                                supportFragmentManager.beginTransaction()
                                    .replace(binding.container.id, SettingFragment()).commit()

                            }else{
                                checkPasswordDialog.dismiss()
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

            checkPasswordDialog.dismiss()
        }
    }   //enterSetting()
}   //class


