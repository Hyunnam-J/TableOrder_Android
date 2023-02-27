package com.example.tableorder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tableorder.databinding.ActivityMainBinding
import com.example.tableorder.main.MainFragment
import com.example.tableorder.setting.SettingFragment

class MainActivity() : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    var map : HashMap<String, Any> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //화면을 누르면 메뉴를 선택하는 메인 프래그먼트로 이동.
        binding.enter.setOnClickListener{

            //먼저 세팅 프래그먼트를 연결해서(실행시켜서) 설정 값을 세팅한다
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, SettingFragment()).commit()

            //그리고 세팅된 설정 값으로 뷰를 불러온다
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, MainFragment(map)).commit()

        }   //binding.enter.setOnClickListener

        binding.setting.setOnClickListener{
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, SettingFragment()).commit()
        }
    }   //onCreate
}   //class


