package cn.entergx.yztx.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import cn.entergx.yztx.bean.bean.User
import cn.entergx.yztx.constant.Identity
import cn.entergx.yztx.constant.Sex
import com.google.gson.Gson

object SPUtils {
    private val context = ContextTool.getContext()
    private val userSp: SharedPreferences =
        context.getSharedPreferences("User", Context.MODE_PRIVATE)
    private val statusSp: SharedPreferences =
        context.getSharedPreferences("Status", Context.MODE_PRIVATE)
    private val userEditor = userSp.edit()
    private val statusEditor = statusSp.edit()

    @SuppressLint("CommitPrefEdits")
    fun saveIsLogin(isLogin: Boolean) {
        userEditor.putBoolean("isLogin", isLogin)
        userEditor.apply()
        saveIsPicker(true)
    }

    fun getIsLogin(): Boolean {
        return userSp.getBoolean("isLogin", false)
    }
    private fun saveIsPicker(isLogin: Boolean) {
        statusEditor.putBoolean("isPicker", isLogin)
        statusEditor.apply()
    }

    fun getIsPicker(): Boolean {
        return statusSp.getBoolean("isPicker", false)
    }

    fun saveUser(user: User) {
        userEditor.putString("user", Gson().toJson(user))
        userEditor.apply()
    }

    fun getUser(): User {
        return Gson().fromJson(userSp.getString("user", ""), User::class.java)
    }

    fun saveYz(sex: Int, date: Long) {
        saveIsPicker(true)
        statusEditor.putInt("type", Identity.YUN_ZHONG)
        statusEditor.putInt("sex", sex)
        statusEditor.putLong("date", date)
        statusEditor.apply()
    }

    fun saveBb(sex: Int, babySex: Int, date: Long) {
        saveIsPicker(true)
        statusEditor.putInt("type", Identity.HAVE_BABY)
        statusEditor.putInt("sex", sex)
        statusEditor.putInt("babySex", babySex)
        statusEditor.putLong("date", date)
        statusEditor.apply()
    }

    fun getInitType(): Int {
        return statusSp.getInt("type", Identity.YUN_ZHONG)
    }
    fun getSex(): Int {
        return statusSp.getInt("sex", Sex.FEMALE)
    }

    fun getBabySex(): Int {
        return statusSp.getInt("babySex", Sex.FEMALE)
    }

    fun getDate(): Long {
        return statusSp.getLong("date", 0)
    }
}