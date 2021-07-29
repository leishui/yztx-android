package cn.entergx.yztx.network

import cn.entergx.yztx.msg.SimpleMsg
import cn.entergx.yztx.utils.MyCallback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Repo {
    private val ipService: IpService = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://yztx.entergx.cn/").build().create(IpService::class.java)

    fun getCode(phone: Long,callback: MyCallback<SimpleMsg>) {
        ipService.getCode(phone).enqueue(callback)
    }
    fun loginPhone(phone: Long,password:String,callback: MyCallback<SimpleMsg>){
        ipService.logInByPhone(phone,password).enqueue(callback)
    }
    fun signIn(phone: Long,password: String,code:Int,callback: MyCallback<SimpleMsg>){
        ipService.signIn(phone, password, code).enqueue(callback)
    }
}