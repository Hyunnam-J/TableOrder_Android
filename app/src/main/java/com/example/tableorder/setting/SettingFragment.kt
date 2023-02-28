package com.example.tableorder.setting

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.tableorder.R
import com.example.tableorder.databinding.FragmentSettingBinding

class SettingFragment() : Fragment(), View.OnClickListener {

    private val TAG = "로그"

    private var _binding : FragmentSettingBinding? = null
    private val binding get() = _binding!!

    lateinit var pref : SharedPreferences
    lateinit var editor : SharedPreferences.Editor

    lateinit var dialog : Dialog

    companion object{

        var comId : String = ""
        var pos : String = ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        setting()
        putMap()

        dialog = context?.let { Dialog(it) }!!

        binding.back.setOnClickListener{
            val manager = activity?.supportFragmentManager
            manager?.beginTransaction()?.remove(this)?.commit()
            manager?.popBackStack()
        }

        binding.buttonComId.setOnClickListener(this)
        binding.buttonPos.setOnClickListener(this)

        return binding.root
    }

    fun putMap(){
        binding.textComId.text = comId
        binding.textPos.text = pos
    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.buttonComId -> showDialog("Com ID", comId)
            R.id.buttonPos -> showDialog("Pos", pos)

            R.id.settingCancel -> dialog!!.dismiss()

        }   //when
    }   //override fun onClick(v: View?)

    fun setting(){
        //변수 값 저장을 위한 세팅
        pref = activity?.getSharedPreferences("pref", 0)!!
        editor = pref?.edit()!!

        //시작 후 저장된 값을 불러온다
        comId = pref.getString("Com ID", "").toString()
        pos = pref.getString("Pos", "").toString()
    }

    fun showDialog(key : String, value : String){

        dialog!!.setContentView(R.layout.activity_setting_dialog)
        sizingSettingDialog()
        dialog.show()

        dialog.findViewById<TextView>(R.id.settingName).text = key + " :"

        dialog.findViewById<EditText>(R.id.setValues).text =
            Editable.Factory.getInstance().newEditable(value)

        dialog.findViewById<Button>(R.id.settingCancel).setOnClickListener(this)
        dialog.findViewById<Button>(R.id.settingConfirm).setOnClickListener{

            editor?.putString(key, dialog.findViewById<EditText>(R.id.setValues).text.toString())
            editor?.commit()

            refreshFragment(requireFragmentManager())
            dialog.dismiss()

        }

    }   //showDialog()

    fun sizingSettingDialog(){

        val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        val deviceHeight = size.y

        params?.width = (deviceWidth * 0.7).toInt()
        params?.height = (deviceHeight * 0.2).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    fun refreshFragment(fragmentManager: FragmentManager) {
        val detachFragment: FragmentTransaction = fragmentManager.beginTransaction()
        detachFragment.detach(this).commit()

        val attachFragment: FragmentTransaction = fragmentManager.beginTransaction()
        attachFragment.attach(this).commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}   //class