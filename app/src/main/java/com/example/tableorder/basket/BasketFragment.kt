package com.example.tableorder.basket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tableorder.R
import com.example.tableorder.databinding.FragmentBasketBinding
import com.example.tableorder.databinding.FragmentMainBinding

class BasketFragment : Fragment() {

    val TAG = "로그"

    private var _binding : FragmentBasketBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBasketBinding.inflate(inflater, container, false)







        return binding.root
    }   //onCreateView

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}   //BasketFragment