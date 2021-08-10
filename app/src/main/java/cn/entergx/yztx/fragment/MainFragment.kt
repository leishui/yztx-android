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
import cn.entergx.yztx.activity.NormalVideoActivity
import cn.entergx.yztx.adapter.MainAdapter
import cn.entergx.yztx.bean.bean.Lesson
import cn.entergx.yztx.msg.LessonMsg
import cn.entergx.yztx.network.Repo
import cn.entergx.yztx.utils.Utils
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Repo.getLesson(object : Callback<LessonMsg> {
            override fun onFailure(call: Call<LessonMsg>, t: Throwable) {

            }

            override fun onResponse(call: Call<LessonMsg>, response: Response<LessonMsg>) {
                Log.e("TAG", "onResponse: " + response.body()?.content)
                val lessonPage = response.body()?.content
                if (lessonPage != null) {
                    initRV(lessonPage.content)
                }
            }
        })

    }

    private fun initRV(lesson: ArrayList<Lesson>) {
        val rvFragmentMain = rv_fragment_main
        val adapter = MainAdapter(requireContext()).also {
            it.setBannerList(
                arrayListOf(
                    R.drawable.ic_bv_1,
                    R.drawable.ic_bv_2,
                    R.drawable.ic_bv_3,
                    R.drawable.ic_bv_4
                )
            )
            it.setVideoList(lesson)
        }
        adapter.setOnItemClickListener(object : MainAdapter.OnBannerItemClickListener {
            override fun onClick(pos: Int) {
                Utils.toast(requireContext(), "点击了第" + pos + "项")
            }
        })
        adapter.setOnItemClickListener(object : MainAdapter.OnRecyclerItemClickListener {
            override fun onClick(
                lesson: Lesson
            ) {
                //Utils.toast(requireContext(), url)
                startActivity(Intent(requireContext(),NormalVideoActivity::class.java).also {
                    it.putExtra("lesson",lesson)
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