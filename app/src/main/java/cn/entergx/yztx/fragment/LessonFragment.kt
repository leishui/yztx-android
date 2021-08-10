package cn.entergx.yztx.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cn.entergx.yztx.R
import cn.entergx.yztx.activity.LessonVideoActivity
import cn.entergx.yztx.activity.NormalVideoActivity
import cn.entergx.yztx.adapter.LessonAdapter
import cn.entergx.yztx.adapter.MainAdapter
import cn.entergx.yztx.bean.bean.Lesson
import cn.entergx.yztx.bean.bean.LessonSet
import cn.entergx.yztx.bean.page.SpringPage
import cn.entergx.yztx.msg.LessonMsg
import cn.entergx.yztx.msg.PageMsg
import cn.entergx.yztx.network.Repo
import cn.entergx.yztx.utils.Utils
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LessonFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Repo.getLessonSet(object : Callback<PageMsg<LessonSet>> {
            override fun onFailure(call: Call<PageMsg<LessonSet>>, t: Throwable) {
                Log.e("TAG++", "onResponse: " + t)
            }

            override fun onResponse(call: Call<PageMsg<LessonSet>>, response: Response<PageMsg<LessonSet>>) {
                Log.e("TAG++", "onResponse: " + response.body()?.content)
                val lessonPage = response.body()?.content?.content
                if (lessonPage != null) {
                    initRV(lessonPage)
                }
            }
        })

    }

    private fun initRV(lessonSet: ArrayList<LessonSet>) {
        val rvFragmentMain = rv_fragment_main
        val adapter = LessonAdapter(requireContext()).also {
            it.setBannerList(
                arrayListOf(
                    R.drawable.ic_bv_1,
                    R.drawable.ic_bv_2,
                    R.drawable.ic_bv_3,
                    R.drawable.ic_bv_4
                )
            )
            it.setVideoList(lessonSet)
        }
        adapter.setOnItemClickListener(object : LessonAdapter.OnBannerItemClickListener {
            override fun onClick(pos: Int) {
                Utils.toast(requireContext(), "点击了第" + pos + "项")
            }
        })
        adapter.setOnItemClickListener(object : LessonAdapter.OnRecyclerItemClickListener {
            override fun onClick(
                lessonSet: LessonSet
            ) {
                //Utils.toast(requireContext(), url)
                startActivity(Intent(requireContext(),LessonVideoActivity::class.java).also {
                    it.putExtra("lessonSet",lessonSet)
                })
            }
        })
        rvFragmentMain.adapter = adapter
        rvFragmentMain.layoutManager = LinearLayoutManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

}