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
package cn.entergx.yztx.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import cn.entergx.yztx.R
import cn.entergx.yztx.activity.PostActivity
import cn.entergx.yztx.adapter.delegate.SimpleDelegateAdapter
import cn.entergx.yztx.adapter.delegate.SingleDelegateAdapter
import cn.entergx.yztx.bean.bean.Post
import cn.entergx.yztx.bean.page.SpringPage
import cn.entergx.yztx.constant.StatusType
import cn.entergx.yztx.msg.Msg
import cn.entergx.yztx.network.Repo
import cn.entergx.yztx.utils.Utils
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.alibaba.android.vlayout.layout.StickyLayoutHelper
import com.bumptech.glide.Glide
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder
import com.xuexiang.xui.adapter.simple.AdapterItem
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner
import com.xuexiang.xui.widget.imageview.ImageLoader
import com.xuexiang.xui.widget.imageview.RadiusImageView
import kotlinx.android.synthetic.main.fragment_v_community.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * VLayout????????????
 * 1.??????VirtualLayoutManager
 * 2.??????RecycledViews???????????????????????????
 * 3.??????DelegateAdapter
 *
 * @author xuexiang
 * @since 2020/3/19 11:26 PM
 */
class CommunityFragmentVLayout : Fragment() {
    private var page = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: SmartRefreshLayout
    private lateinit var mNewsAdapter: SimpleDelegateAdapter<Post>
    private var mDelegateAdapter: DelegateAdapter? = null
    private val mAdapters: MutableList<DelegateAdapter.Adapter<*>> =
        ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_v_community, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
    }

    private fun initViews() {
        recyclerView = rv_v
        refreshLayout = rf_v
        // 1.??????VirtualLayoutManager
        val virtualLayoutManager = VirtualLayoutManager(requireContext())
        recyclerView.layoutManager = virtualLayoutManager

        // 2.??????RecycledViews???????????????????????????
        val viewPool = RecycledViewPool()
        viewPool.setMaxRecycledViews(0, 20)
        recyclerView.setRecycledViewPool(viewPool)

        //????????????????????????
        val bannerAdapter: SingleDelegateAdapter =
            object : SingleDelegateAdapter(R.layout.include_head_view_banner) {
                override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
                    val banner =
                        holder.findViewById<SimpleImageBanner>(R.id.sib_simple_usage)
                    banner.setSource(Utils.getBannerListPost())
                        .setOnItemClickListener { _: View?, _: BannerItem?, position1: Int ->
                            Utils.toast(
                                "headBanner position--->$position1"
                            )
                        }.startScroll()
                }
            }

        //?????????????????????
        mNewsAdapter = object : SimpleDelegateAdapter<Post>(
            R.layout.adapter_news_xuilayout_list_item,
            LinearLayoutHelper()
        ) {
            override fun bindData(
                holder: RecyclerViewHolder,
                position: Int,
                model: Post?
            ) {

                val linearLayout = holder.itemView.findViewById<LinearLayout>(R.id.l_post_item)
                val postResourcesList = model?.postResourcesList
                linearLayout.removeAllViews()
                if (postResourcesList != null) {
                    var i = 0
                    for (s in postResourcesList) {
                        if (i == 3)
                            break
                        val iv = ImageView(requireContext())
                        val layoutParams =
                            LinearLayout.LayoutParams(0, Utils.dip2px(requireContext(), 150f))
                        layoutParams.weight = 1f
                        iv.layoutParams = layoutParams
                        iv.setPadding(5, 5, 5, 5)
                        iv.scaleType = ImageView.ScaleType.CENTER_CROP
                        Glide.with(requireContext()).load(s).placeholder(R.drawable.ic_gank)
                            .error(R.drawable.ic_error).into(iv)
                        //holder.image(i,s)
                        linearLayout.addView(iv)
                        i++
                    }
                }
                holder.image(R.id.ri_avatar_community,model?.user?.avatar_url)
                holder.text(R.id.tv_community_up_name, model?.user?.user_name)
                holder.text(R.id.tv_community_post_title, model?.post_name)
                holder.text(R.id.tv_community_post_des, model?.post_content)
                holder.click(
                    R.id.card_view
                ) { v: View? ->
                    startActivity(Intent(requireContext(),PostActivity::class.java).also { it.putExtra("post",model) })
                }
            }

        }
        mDelegateAdapter = DelegateAdapter(virtualLayoutManager)
        //mAdapters.add(floatAdapter)
        //mAdapters.add(scrollFixAdapter)
        mAdapters.add(bannerAdapter)
        //mAdapters.add(commonAdapter)
        //mAdapters.add(titleAdapter)
        mAdapters.add(mNewsAdapter)

        // 3.??????DelegateAdapter
        recyclerView.adapter = mDelegateAdapter
    }

    private fun initListeners() {
        //????????????
        refreshLayout.setOnRefreshListener {
            initData(0)
        }
        //????????????
        refreshLayout.setOnLoadMoreListener {
            initData(page)
        }
        refreshLayout.autoRefresh() //????????????????????????????????????????????????
        //initData(0)
    }

    private fun initData(page: Int) {
        if (page == 0) this.page = 0
        this.page += 1
        Repo.getPostsByPage(page, 10, object : Callback<Msg<SpringPage<Post>>> {
            override fun onFailure(call: Call<Msg<SpringPage<Post>>>, t: Throwable) {
                Utils.toast("??????????????????:????????????")
                if (page == 0) {
                    mDelegateAdapter!!.setAdapters(mAdapters)
                    refreshLayout.finishRefresh()
                } else {
                    refreshLayout.finishLoadMore()
                }
            }

            override fun onResponse(
                call: Call<Msg<SpringPage<Post>>>,
                response: Response<Msg<SpringPage<Post>>>
            ) {
                if (response.body()!!.status == StatusType.SUCCESSFUL) {
                    if (page == 0) {
                        mNewsAdapter.refresh(response.body()!!.content.content)
                        mDelegateAdapter!!.setAdapters(mAdapters)
                    } else {
                        if (response.body()!!.content.empty) {
                            this@CommunityFragmentVLayout.page -= 1
                            Utils.toast("???????????????")
                            refreshLayout.finishLoadMoreWithNoMoreData()
                        } else mNewsAdapter.loadMore(response.body()!!.content.content)
                    }
                } else {
                    Utils.toast("??????????????????")
                }
                if (page == 0)
                    refreshLayout.finishRefresh()
                else
                    refreshLayout.finishLoadMore()
            }
        })
    }

}