package com.example.tableorder

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.view.ViewGroup
import android.view.WindowManager

class SizingDialog {

    fun sizingDialog(dialog : Dialog, context: Context, width : Double, height : Double ){

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        val deviceHeight = size.y

        params?.width = (deviceWidth * width).toInt()
        params?.height = (deviceHeight * height).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }
}   //class