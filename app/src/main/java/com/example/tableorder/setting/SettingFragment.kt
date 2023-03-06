package com.example.tableorder.setting

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.*
import android.widget.RadioGroup.OnCheckedChangeListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.tableorder.R
import com.example.tableorder.SizingDialog
import com.example.tableorder.databinding.FragmentSettingBinding

class SettingFragment() : Fragment(), View.OnClickListener, OnCheckedChangeListener {

    private val TAG = "로그"

    private var _binding : FragmentSettingBinding? = null
    private val binding get() = _binding!!

    lateinit var settingDialog : Dialog

    companion object{
        var settingPref : SharedPreferences? = null
        var settingEditor : SharedPreferences.Editor? = null

        var comId : String = ""
        var pos : String = ""
        var selectMode : String = ""
        var tNum : String = ""

        var test : String = ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        setting()
        putMap()

        settingDialog = context?.let { Dialog(it) }!!

        binding.back.setOnClickListener{

            val manager = activity?.supportFragmentManager
            manager?.beginTransaction()?.remove(this)?.commit()
            manager?.popBackStack()
        }

        binding.buttonComId.setOnClickListener(this)
        binding.buttonPos.setOnClickListener(this)
        binding.buttonTest.setOnClickListener(this)
        binding.buttonTnum.setOnClickListener(this)

        //고정, 이동 선택 시 값 저장 처리
        binding.selectMode.setOnCheckedChangeListener(this)

        return binding.root
    }

    fun putMap(){
        binding.textComId.text = comId
        binding.textPos.text = pos

        if(selectMode==binding.holdRadioButton.text.toString()){
            binding.selectMode.check(binding.holdRadioButton.id)
        }else if(selectMode==binding.moveRadioButton.text.toString()){
            binding.selectMode.check(binding.moveRadioButton.id)
        }

        binding.textTnum.text = tNum

        binding.textTest.text = test
    }

    fun setting(){
        //변수 값 저장을 위한 세팅
        settingPref = activity?.getSharedPreferences("pref", 0)!!
        settingEditor = settingPref?.edit()!!

        //시작 후 저장된 값을 불러온다
        comId = settingPref?.getString("Com ID", "").toString()
        pos = settingPref?.getString("Pos", "").toString()
        selectMode = settingPref?.getString("selectMode", "").toString()
        tNum = settingPref?.getString("Table No", "").toString()

        test = settingPref?.getString("test", "").toString()
    }
    override fun onClick(v: View?) {
        when(v?.id){

            R.id.buttonComId -> showDialog("Com ID", comId)
            R.id.buttonPos -> showDialog("Pos", pos)
            R.id.buttonTnum -> showDialog("Table No", tNum)

            R.id.buttonTest -> showDialog("test", test)

            R.id.settingCancel -> settingDialog!!.dismiss()

        }   //when
    }   //override fun onClick(v: View?)

    fun showDialog(key : String, value : String){

        settingDialog!!.setContentView(R.layout.activity_setting_dialog)
        SizingDialog().sizingDialog(settingDialog, requireContext(), 0.7, 0.2)
        settingDialog.show()

        settingDialog.findViewById<TextView>(R.id.settingName).text = key + " :"

        settingDialog.findViewById<EditText>(R.id.setValues).text =
            Editable.Factory.getInstance().newEditable(value)

        settingDialog.findViewById<Button>(R.id.settingCancel).setOnClickListener(this)
        settingDialog.findViewById<Button>(R.id.settingConfirm).setOnClickListener{

            settingEditor?.putString(key, settingDialog.findViewById<EditText>(R.id.setValues).text.toString())
            settingEditor?.commit()

            refreshFragment(requireFragmentManager())
            settingDialog.dismiss()

        }
    }   //showDialog()

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

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(checkedId){
            R.id.holdRadioButton ->{

                settingEditor?.putString("selectMode", binding.holdRadioButton.text.toString())
                settingEditor?.commit()
                Toast.makeText(context, "고정식 모드가 선택되었습니다", Toast.LENGTH_SHORT).show()
            }
            R.id.moveRadioButton ->{

                settingEditor?.putString("selectMode", binding.moveRadioButton.text.toString())
                settingEditor?.commit()
                Toast.makeText(context, "이동식 모드가 선택되었습니다", Toast.LENGTH_SHORT).show()
            }
        }   //when
    }   //override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {

}   //class