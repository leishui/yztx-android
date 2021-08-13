/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package cn.entergx.yztx.fragment.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import cn.entergx.yztx.R
import cn.entergx.yztx.adapter.delegate.SimpleDelegateAdapter
import cn.entergx.yztx.adapter.delegate.SingleDelegateAdapter
import cn.entergx.yztx.bean.bean.Comment
import cn.entergx.yztx.constant.ResourceType
import cn.entergx.yztx.constant.StatusType
import cn.entergx.yztx.msg.PageMsg
import cn.entergx.yztx.network.Repo
import cn.entergx.yztx.utils.Utils
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.alibaba.android.vlayout.layout.StickyLayoutHelper
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder
import kotlinx.android.synthetic.main.fragment_v_community.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * VLayout使用步骤
 * 1.设置VirtualLayoutManager
 * 2.设置RecycledViews复用池大小（可选）
 * 3.设置DelegateAdapter
 *
 * @author xuexiang
 * @since 2020/3/19 11:26 PM
 */
class CommentFragmentVLayout(private val lessonId: Long) : Fragment() {
    private var page = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: SmartRefreshLayout
    private lateinit var mNewsAdapter: SimpleDelegateAdapter<Comment>
    private var mDelegateAdapter: DelegateAdapter? = null
    private val mAdapters: MutableList<DelegateAdapter.Adapter<*>> =
        ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_v_comment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
    }

    private fun initViews() {
        recyclerView = rv_v
        refreshLayout = rf_v
        // 1.设置VirtualLayoutManager
        val virtualLayoutManager = VirtualLayoutManager(requireContext())
        recyclerView.layoutManager = virtualLayoutManager

        // 2.设置RecycledViews复用池大小（可选）
        val viewPool = RecycledViewPool()
        viewPool.setMaxRecycledViews(0, 20)
        recyclerView.setRecycledViewPool(viewPool)

        //资讯的标题
        val stickyLayoutHelper = StickyLayoutHelper()
        stickyLayoutHelper.setStickyStart(true)
        val titleAdapter: SingleDelegateAdapter = object :
            SingleDelegateAdapter(R.layout.adapter_vlayout_title_item, stickyLayoutHelper) {
            override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
                holder.text(R.id.tv_title, "热门评论")
                holder.text(R.id.tv_action, "按热度")
                holder.click(
                    R.id.tv_action
                ) { v: View? -> Utils.toast("按热度") }
            }
        }
        val bottomAdapter = object :SingleDelegateAdapter(R.layout.adapter_vlayout_bottom_item){
            override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

            }

        }

        //资讯，线性布局
        mNewsAdapter = object : SimpleDelegateAdapter<Comment>(
            R.layout.item_comment,
            LinearLayoutHelper()
        ) {
            override fun bindData(
                holder: RecyclerViewHolder,
                position: Int,
                model: Comment?
            ) {
                if (model != null) {
                    holder.text(R.id.tv_up_name_comment, model.commentator_name)
                    holder.text(R.id.tv_comment_content,model.comment_content)
                    holder.text(R.id.tv_up_time_comment, Utils.transToString(model.comment_time))
                    holder.image(R.id.ri_avatar_comment,model.commentator_url)
                }
            }
        }
        mDelegateAdapter = DelegateAdapter(virtualLayoutManager)
        //mAdapters.add(floatAdapter)
        //mAdapters.add(scrollFixAdapter)
        //mAdapters.add(bannerAdapter)
        //mAdapters.add(commonAdapter)
        //mAdapters.add(titleAdapter)
        mAdapters.add(mNewsAdapter)
        mAdapters.add(bottomAdapter)

        // 3.设置DelegateAdapter
        recyclerView.adapter = mDelegateAdapter
    }

    private fun initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener {
            initData(0)
        }
        //上拉加载
        refreshLayout.setOnLoadMoreListener {
            initData(page)
        }
        refreshLayout.autoRefresh() //第一次进入触发自动刷新，演示效果
        //initData(0)
    }

    fun initData(page: Int) {
        if (page == 0) this.page = 0
        this.page += 1
        Repo.getCommentsAndReplies(lessonId,
            ResourceType.LESSON,page, 10, object : Callback<PageMsg<Comment>> {
            override fun onFailure(call: Call<PageMsg<Comment>>, t: Throwable) {
                Utils.toast("获取评论失败:请求异常")
                if (page == 0) {
                    mDelegateAdapter!!.setAdapters(mAdapters)
                    refreshLayout.finishRefresh()
                } else {
                    refreshLayout.finishLoadMore()
                }
            }

            override fun onResponse(
                call: Call<PageMsg<Comment>>,
                response: Response<PageMsg<Comment>>
            ) {
                if (response.body()!!.status == StatusType.SUCCESSFUL) {
                    if (page == 0) {
                        mNewsAdapter.refresh(response.body()!!.content.content)
                        mDelegateAdapter!!.setAdapters(mAdapters)
                    } else {
                        if (response.body()!!.content.empty) {
                            this@CommentFragmentVLayout.page -= 1
                            Utils.toast("已全部加载")
                            refreshLayout.finishLoadMoreWithNoMoreData()
                        } else mNewsAdapter.loadMore(response.body()!!.content.content)
                    }
                } else {
                    Utils.toast("获取评论失败")
                }
                if (page == 0)
                    refreshLayout.finishRefresh()
                else
                    refreshLayout.finishLoadMore()
            }
        })
    }

}