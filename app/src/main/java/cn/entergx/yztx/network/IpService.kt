package cn.entergx.yztx.network

import cn.entergx.yztx.msg.SimpleMsg
import retrofit2.Call
import retrofit2.http.*

interface IpService {


    //获取验证码
    @GET("user/get_code")
    fun getCode(@Query("phone") phone: Long): Call<SimpleMsg>

    //用户注册
    @POST("user/sign_in")
    fun signIn(
        @Query("phone") phone: Long,
        @Query("password") password: String,
        @Query("code") code: Int
    ): Call<SimpleMsg>

    //账号登录
    @POST("user/login_by_account")
    fun logInByAccount(
        @Query("account") account: String,
        @Query("password") password: String
    ): Call<SimpleMsg>

    //手机号登录
    @POST("user/login_by_phone")
    fun logInByPhone(
        @Query("phone") phone: Long,
        @Query("password") password: String
    ): Call<SimpleMsg>

    //通过用户id获取用户信息
    @GET("user/get_info")
    fun getUserInfo(@Query("id") id: Long): Call<SimpleMsg>
}