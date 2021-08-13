package cn.entergx.yztx.fragment

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cn.entergx.yztx.R
import cn.entergx.yztx.activity.LessonVideoActivity
import cn.entergx.yztx.adapter.delegate.SimpleDelegateAdapter
import cn.entergx.yztx.adapter.delegate.SingleDelegateAdapter
import cn.entergx.yztx.bean.bean.LessonSet
import cn.entergx.yztx.constant.StatusType
import cn.entergx.yztx.msg.PageMsg
import cn.entergx.yztx.network.Repo
import cn.entergx.yztx.utils.Utils
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner
import kotlinx.android.synthetic.main.fragment_v_community.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LessonFragmentVLayout:MainFragmentVLayout() {
    private lateinit var mNewsAdapter: SimpleDelegateAdapter<LessonSet>
    override fun initData(page: Int) {
        if (page == 0) this.page = 0
        this.page += 1
        Repo.getLessonSetByPage(page, 10, object : Callback<PageMsg<LessonSet>> {
            override fun onFailure(call: Call<PageMsg<LessonSet>>, t: Throwable) {
                Utils.toast("获取课程失败:请求异常")
                if (page == 0) {
                    mDelegateAdapter!!.setAdapters(mAdapters)
                    refreshLayout.finishRefresh()
                } else {
                    refreshLayout.finishLoadMore()
                }
            }

            override fun onResponse(
                call: Call<PageMsg<LessonSet>>,
                response: Response<PageMsg<LessonSet>>
            ) {
                if (response.body()!!.status == StatusType.SUCCESSFUL) {
                    if (page == 0) {
                        mNewsAdapter.refresh(response.body()!!.content.content)
                        mDelegateAdapter!!.setAdapters(mAdapters)
                    } else {
                        if (response.body()!!.content.empty) {
                            this@LessonFragmentVLayout.page -= 1
                            Utils.toast("已全部加载")
                        } else mNewsAdapter.loadMore(response.body()!!.content.content)
                    }
                } else {
                    Utils.toast("获取课程失败")
                }
                if (page == 0)
                    refreshLayout.finishRefresh()
                else
                    refreshLayout.finishLoadMore()
            }
        })
    }

    override fun initViews() {
        recyclerView = rv_v
        refreshLayout = rf_v
        // 1.设置VirtualLayoutManager
        val virtualLayoutManager = VirtualLayoutManager(requireContext())
        recyclerView.layoutManager = virtualLayoutManager

        // 2.设置RecycledViews复用池大小（可选）
        val viewPool = RecyclerView.RecycledViewPool()
        viewPool.setMaxRecycledViews(0, 20)
        recyclerView.setRecycledViewPool(viewPool)

        //轮播条，单独布局
        val bannerAdapter: SingleDelegateAdapter =
            object : SingleDelegateAdapter(R.layout.include_head_view_banner) {
                override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
                    val banner =
                        holder.findViewById<SimpleImageBanner>(R.id.sib_simple_usage)
                    banner.setSource(Utils.getBannerListLesson())
                        .setOnItemClickListener { _: View?, _: BannerItem?, position1: Int ->
                            Utils.toast(
                                "headBanner position--->$position1"
                            )
                        }.startScroll()
                }
            }
        //资讯，线性布局
        mNewsAdapter = object : SimpleDelegateAdapter<LessonSet>(
            R.layout.item_main,
            LinearLayoutHelper()
        ) {
            override fun bindData(
                holder: RecyclerViewHolder,
                position: Int,
                model: LessonSet?
            ) {
                if (model != null) {
                    val i = position * 2
                    val ii = i + 1
                    holder.click(R.id.cv_video_main_1) {
                        startActivity(Intent(requireContext(), LessonVideoActivity::class.java).also {
                            it.putExtra("lessonSet",mData[i])
                        })
                    }
                    holder.click(R.id.cv_video_main_2) {
                        startActivity(Intent(requireContext(), LessonVideoActivity::class.java).also {
                            it.putExtra("lessonSet",mData[ii])
                        })
                    }
                    holder.text(R.id.tv_title_main_1, mData[i].title)
                    holder.text(R.id.tv_title_main_2, mData[ii].title)
                    holder.image(R.id.iv_cover_main_1, mData[i].cover_url)
                    holder.image(R.id.iv_cover_main_2, mData[ii].cover_url)
                }
            }


            override fun getItemCount(): Int {
                return mData.size / 2
            }
        }
        mDelegateAdapter = DelegateAdapter(virtualLayoutManager)
        mAdapters.add(bannerAdapter)
        mAdapters.add(mNewsAdapter)

        // 3.设置DelegateAdapter
        recyclerView.adapter = mDelegateAdapter
    }
}