package cn.entergx.yztx.model

import cn.entergx.yztx.msg.SimpleMsg
import cn.entergx.yztx.network.IpService
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserModel {
    private val ipService: IpService = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("http://118.178.178.211:8080/").build().create(IpService::class.java)

    fun getCode(phone: Long,callback: Callback<SimpleMsg>) {
        ipService.getCode(phone).enqueue(callback)
    }
}