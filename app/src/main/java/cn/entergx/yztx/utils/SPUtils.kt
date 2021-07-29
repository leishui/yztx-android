package cn.entergx.yztx.utils

import android.annotation.SuppressLint
import android.content.Context

object SPUtils {
    @SuppressLint("CommitPrefEdits")
    fun saveIsLogin(context: Context, isLogin:Boolean){
        val sp = context.getSharedPreferences("User",Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putBoolean("isLogin",isLogin)
        editor.apply()
    }
    fun getIsLogin(context: Context):Boolean{
        val sp = context.getSharedPreferences("User",Context.MODE_PRIVATE)
        return sp.getBoolean("isLogin",false)
    }
}