package com.example.tableorder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tableorder.databinding.ActivityMainBinding

class MainActivity() : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //화면을 누르면 메뉴를 선택하는 메인 프래그먼트로 이동.
        binding.enter.setOnClickListener{
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, MainFragment()).commit()
        }
    }   //onCreate
}   //class


