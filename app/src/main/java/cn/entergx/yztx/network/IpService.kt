package cn.entergx.yztx.network

import cn.entergx.yztx.msg.SimpleMsg
import retrofit2.Call
import retrofit2.http.*

interface IpService {


    //更改密码时获取验证码
    @GET("user/get_code")
    fun getCode(@Query("phone") phone:Long): Call<SimpleMsg>
//
//    //重置密码
//    @POST("user/change_password")
//    fun changePassword(@Query("password") password: String,@Query("checkNumber") checkNumber:String,@Query("phoneNumber") phoneNumber: String):Call<StatusBean>
//
//    //更新用户信息
//    @POST("user/update_data")
//    fun updateData(@Header("token") token:String,@Body body: RequestBody):Call<StatusBean>
//
//    //更新用户自我介绍
//    @POST("user/update_introduction")
//    fun updateIntroduction(@Header("token") token:String,@Query("intro") intro:String,@Query("skills") skills:String):Call<StatusBean>
//
//    //更新用户项目经验
//    @POST("user/update_experience")
//    fun updateExperience(@Header("token") token:String,@Query("projectName") projectName:String,@Query("projectInfo") projectInfo:String):Call<StatusBean>
//
//    //更新头像
//    @Multipart
//    @POST("user/update_profile_photo")
//    fun updateProfilePhoto(@Header("token") token:String,@Part body: MultipartBody.Part):Call<UpdateProfilePhotoBean>
//
//    //获取用户信息ByToken
//    @POST("user/get_user_by_token")
//    fun getUserByToken(@Header("token")  token:String):Call<UserInfoBean>
//
//    //获取全部学校
//    @POST("user/get_university")
//    fun getUniversity():Call<UniversityBean>
//
//    //获取某学校的所有学院
//    @POST("user/get_faculty")
//    fun getFacultyByUnid(@Query("unid") unid:String):Call<FacultyBean>
}