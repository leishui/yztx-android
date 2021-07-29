package cn.entergx.yztx.network

import cn.entergx.yztx.msg.SimpleMsg
import retrofit2.Call
import retrofit2.http.*

interface IpService {


    //更改密码时获取验证码
    @GET("user/get_code")
    fun getCode(@Query("phone") phone: Long): Call<SimpleMsg>

    @POST("user/sign_in")
    fun signIn(
        @Query("account") account: String,
        @Query("password") password: String,
        @Query("phone") phone:Long,
        @Query("code") code:Int
    ):Call<SimpleMsg>
}