package cn.entergx.yztx.network

import cn.entergx.yztx.bean.bean.Comment
import cn.entergx.yztx.bean.bean.LessonSet
import cn.entergx.yztx.bean.bean.Post
import cn.entergx.yztx.bean.bean.User
import cn.entergx.yztx.bean.page.SpringPage
import cn.entergx.yztx.msg.LessonMsg
import cn.entergx.yztx.msg.Msg
import cn.entergx.yztx.msg.PageMsg
import cn.entergx.yztx.msg.SimpleMsg
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
        @Query("code") code: Int,
        @Query("identity") identity: Int,
        @Query("date") date: Long
    ): Call<Msg<User>>

    //账号登录
    @POST("user/login_by_account")
    fun logInByAccount(
        @Query("account") account: String,
        @Query("password") password: String
    ): Call<Msg<User>>

    //手机号登录
    @POST("user/login_by_phone")
    fun logInByPhone(
        @Query("phone") phone: Long,
        @Query("password") password: String
    ): Call<Msg<User>>

    //通过用户id获取用户信息
    @GET("user/get_info")
    fun getUserInfo(@Query("id") id: Long): Call<Msg<User>>

    //
    @GET("lesson/get")
    fun getLesson(@Query("page") page: Int, @Query("size") size: Int): Call<LessonMsg>

    @GET("lesson/get_set")
    fun getLessonSet(@Query("page") page: Int, @Query("size") size: Int): Call<PageMsg<LessonSet>>

    @POST("comment/get_comments_and_replies")
    fun getCommentsAndReplies(
        @Query("comment_id") commentId: Long,
        @Query("type") type: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<PageMsg<Comment>>

    @POST("comment/save_comment")
    fun saveComment(
        @Query("comment_id") comment_id: Long,
        @Query("content") content: String,
        @Query("user_id") user_id: Long,
        @Query("type") type: Int
    ): Call<SimpleMsg>

    @Multipart
    @POST("resource/upload_files")
    fun uploadFiles(
        @Part files: List<MultipartBody.Part>,
        @Part("up_id") upId: Long
    ): Call<Msg<ArrayList<String>>>

    @POST("post/upload")
    fun uploadPost(
        @Query("name") name: String,
        @Query("content") content: String,
        @Query("resources") resources: String?,
        @Query("up_id") upId: Long,
        @Query("source_type") sourceType: Boolean,
        @Query("post_type") type: Long
    ): Call<SimpleMsg>
    @GET("post/get")
    fun getPostsByPage(@Query("page")page: Int,@Query("size")size: Int):Call<Msg<SpringPage<Post>>>
}