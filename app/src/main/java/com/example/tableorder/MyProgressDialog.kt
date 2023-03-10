package com.example.tableorder

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager

class MyProgressDialog(context: Context) : Dialog(context) {
    init {
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.myprogressdialog)
        setCancelable(false)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND) // dialog의 dim 처리 배경 제거
    }
}