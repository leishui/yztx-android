package cn.entergx.yztx.network

import cn.entergx.yztx.bean.bean.Comment
import cn.entergx.yztx.bean.bean.LessonSet
import cn.entergx.yztx.bean.bean.User
import cn.entergx.yztx.bean.page.SpringPage
import cn.entergx.yztx.msg.LessonMsg
import cn.entergx.yztx.msg.Msg
import cn.entergx.yztx.msg.PageMsg
import cn.entergx.yztx.msg.SimpleMsg
import cn.entergx.yztx.utils.MyCallback
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query

object Repo {
    private val ipService: IpService = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://yztx.entergx.cn/").build().create(IpService::class.java)

    fun getUserInfo(id:Long,callback: MyCallback<Msg<User>>){
        ipService.getUserInfo(id).enqueue(callback)
    }
    fun getCode(phone: Long, callback: MyCallback<SimpleMsg>) {
        ipService.getCode(phone).enqueue(callback)
    }

    fun loginPhone(phone: Long, password: String, callback: MyCallback<Msg<User>>) {
        ipService.logInByPhone(phone, password).enqueue(callback)
    }

    fun signIn(phone: Long, password: String, code: Int,identity:Int,date:Long, callback: MyCallback<Msg<User>>) {
        ipService.signIn(phone, password, code,identity, date).enqueue(callback)
    }

    fun getLesson(callback: Callback<LessonMsg>) {
        ipService.getLesson(0, 14).enqueue(callback)
    }
    fun getLessonSet(callback: Callback<PageMsg<LessonSet>>) {
        ipService.getLessonSet(0, 14).enqueue(callback)
    }

    fun getCommentsAndReplies(
        commentId: Long,
        type: Int,
        page: Int,
        size: Int,
        callback: Callback<PageMsg<Comment>>
    ) {
        ipService.getCommentsAndReplies(commentId, type, page, size).enqueue(callback)
    }

    fun saveComment(comment_id: Long, content: String, user_id: Long, type: Int,callback: Callback<SimpleMsg>) {
        ipService.saveComment(comment_id,content,user_id, type).enqueue(callback)
    }
}