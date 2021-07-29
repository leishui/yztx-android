package cn.entergx.yztx.utils

import android.util.Log
import retrofit2.Call
import retrofit2.Callback

interface MyCallback<T>:Callback<T> {
    override fun onFailure(call: Call<T>, t: Throwable) {
        Log.e(call.request().url.toString(), "onFailure: ", t)
    }
}