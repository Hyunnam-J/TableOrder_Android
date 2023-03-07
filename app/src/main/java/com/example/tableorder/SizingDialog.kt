package com.example.tableorder

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.*
import androidx.window.layout.WindowMetrics
import androidx.window.layout.WindowMetricsCalculator

class SizingDialog {

    fun sizingDialog(dialog : Dialog, context: Context, width : Double, height : Double) {

        val windowMetrics : WindowMetrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(context as Activity)

        val x = windowMetrics.bounds.width()
        val y = windowMetrics.bounds.height()

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes

        params?.width = (x * width).toInt()
        params?.height = (y * height).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams

    }
}