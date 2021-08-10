package cn.entergx.yztx.widget

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import cn.entergx.yztx.R

class MyPopup(private val context: Context):PopupWindow(context) {
    override fun dismiss() {

    }

    fun myDismiss(){
        super.dismiss()
    }

}