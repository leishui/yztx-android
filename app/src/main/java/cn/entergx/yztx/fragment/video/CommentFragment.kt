package cn.entergx.yztx.fragment.video

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.entergx.yztx.R
import cn.entergx.yztx.adapter.CommentAdapter
import cn.entergx.yztx.bean.bean.Comment
import cn.entergx.yztx.constant.ResourceType
import cn.entergx.yztx.msg.PageMsg
import cn.entergx.yztx.network.Repo
import cn.entergx.yztx.utils.Utils
import kotlinx.android.synthetic.main.fragment_comment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentFragment(private val mContext: Context,private val lessonId: Long) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPopup()
        initData()
    }
    companion object{
        fun initData(lessonId: Long,mContext: Context,rv_comment:RecyclerView) {
            Repo.getCommentsAndReplies(
                lessonId,
                ResourceType.LESSON,
                0,
                14,
                object : Callback<PageMsg<Comment>> {
                    override fun onFailure(call: Call<PageMsg<Comment>>, t: Throwable) {
                        Utils.toast(mContext, "拉取评论失败" + t.message)
                        Log.d("10086", "onFailure: ", t)
                    }

                    override fun onResponse(
                        call: Call<PageMsg<Comment>>,
                        response: Response<PageMsg<Comment>>
                    ) {
                        Utils.toast(mContext, "拉取评论成功")
                        rv_comment.adapter =
                            response.body()?.content?.content?.let { CommentAdapter(mContext, it) }
                        rv_comment.layoutManager = LinearLayoutManager(mContext)
                    }
                })
        }
    }

    private fun initData() {
        Repo.getCommentsAndReplies(
            lessonId,
            ResourceType.LESSON,
            0,
            14,
            object : Callback<PageMsg<Comment>> {
                override fun onFailure(call: Call<PageMsg<Comment>>, t: Throwable) {
                    Utils.toast(mContext, "拉取评论失败" + t.message)
                    Log.d("10086", "onFailure: ", t)
                }

                override fun onResponse(
                    call: Call<PageMsg<Comment>>,
                    response: Response<PageMsg<Comment>>
                ) {
                    Utils.toast(mContext, "拉取评论成功")
                    rv_comment.adapter =
                        response.body()?.content?.content?.let { CommentAdapter(mContext, it) }
                    rv_comment.layoutManager = LinearLayoutManager(mContext)
                }
            })
    }

    private fun initPopup() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false)
    }
}