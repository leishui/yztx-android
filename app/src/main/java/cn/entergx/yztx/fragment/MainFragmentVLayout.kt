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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import cn.entergx.yztx.R
import cn.entergx.yztx.activity.DisplayActivity
import cn.entergx.yztx.activity.NormalVideoActivity
import cn.entergx.yztx.adapter.delegate.SimpleDelegateAdapter
import cn.entergx.yztx.adapter.delegate.SingleDelegateAdapter
import cn.entergx.yztx.bean.bean.Lesson
import cn.entergx.yztx.constant.StatusType
import cn.entergx.yztx.msg.LessonMsg
import cn.entergx.yztx.network.Repo
import cn.entergx.yztx.utils.Utils
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.alibaba.android.vlayout.layout.StickyLayoutHelper
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
open class MainFragmentVLayout : Fragment() {
    var page = 0
    lateinit var recyclerView: RecyclerView
    lateinit var refreshLayout: SmartRefreshLayout
    private lateinit var mNewsAdapter: SimpleDelegateAdapter<Lesson>
    var mDelegateAdapter: DelegateAdapter? = null
    val mAdapters: MutableList<DelegateAdapter.Adapter<*>> =
        ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_v_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
    }

    open fun initViews() {
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
                    banner.setSource(Utils.getBannerListMain())
                        .setOnItemClickListener { _: View?, _: BannerItem?, position1: Int ->
                            Utils.toast(
                                "headBanner position--->$position1"
                            )
                        }.startScroll()
                }
            }

        //???????????????, ???????????????
        val gridLayoutHelper = GridLayoutHelper(4)
        gridLayoutHelper.setPadding(0, 16, 0, 0)
        gridLayoutHelper.vGap = 10
        gridLayoutHelper.hGap = 0
        val server = "http://entergx.cn/"
        val urls = arrayOf(
            server + "phb.html",
            server + "baomariji.html",
            server + "fayulichengbei.html",
            server + "yunyuzhoukan.html",
            server + "nbnc.html",
            server + "nbnz.html",
            server + "bbpy.html",
            server + "qtgj.html"
        )
        val commonAdapter: SimpleDelegateAdapter<AdapterItem> = object :
            SimpleDelegateAdapter<AdapterItem>(
                R.layout.adapter_common_grid_item,
                gridLayoutHelper,
                Utils.getGridItems(requireContext())
            ) {
            override fun bindData(
                holder: RecyclerViewHolder,
                position: Int,
                item: AdapterItem?
            ) {
                if (item != null) {
                    val imageView =
                        holder.findViewById<RadiusImageView>(R.id.riv_item)
                    imageView.isCircle = true
                    ImageLoader.get()
                        .loadImage(imageView, item.icon)
                    holder.text(R.id.tv_title, item.title.toString().substring(0, 1))
                    holder.text(R.id.tv_sub_title, item.title)
                    holder.click(
                        R.id.ll_container
                    ) { v: View? ->
                        startActivity(Intent(requireContext(), DisplayActivity::class.java).also {
                            it.putExtra("title", item.title)
                            it.putExtra("url", urls[position])
                        })
                    }
                }
            }
        }

        //???????????????
        val stickyLayoutHelper = StickyLayoutHelper()
        stickyLayoutHelper.setStickyStart(true)
        val titleAdapter: SingleDelegateAdapter = object :
            SingleDelegateAdapter(R.layout.adapter_vlayout_title_item, stickyLayoutHelper) {
            override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
                holder.text(R.id.tv_title, "??????")
                holder.text(R.id.tv_action, "??????")
                holder.click(
                    R.id.tv_action
                ) { v: View? -> Utils.toast("??????") }
            }
        }

        //?????????????????????
        mNewsAdapter = object : SimpleDelegateAdapter<Lesson>(
            R.layout.item_main,
            LinearLayoutHelper()
        ) {
            override fun bindData(
                holder: RecyclerViewHolder,
                position: Int,
                model: Lesson?
            ) {
                if (model != null) {
                    val i = position * 2
                    val ii = i + 1
                    holder.click(R.id.cv_video_main_1) {
                        startActivity(
                            Intent(
                                requireContext(),
                                NormalVideoActivity::class.java
                            ).also {
                                it.putExtra("lesson", mData[i])
                            })
                    }
                    holder.click(R.id.cv_video_main_2) {
                        startActivity(
                            Intent(
                                requireContext(),
                                NormalVideoActivity::class.java
                            ).also {
                                it.putExtra("lesson", mData[ii])
                            })
                    }
                    holder.text(R.id.tv_title_main_1, mData[i].name)
                    holder.text(R.id.tv_title_main_2, mData[ii].name)
                    holder.image(R.id.iv_cover_main_1, mData[i].cover_url)
                    holder.image(R.id.iv_cover_main_2, mData[ii].cover_url)
                }
            }


            override fun getItemCount(): Int {
                return mData.size / 2
            }
        }
        mDelegateAdapter = DelegateAdapter(virtualLayoutManager)
        //mAdapters.add(floatAdapter)
        //mAdapters.add(scrollFixAdapter)
        mAdapters.add(bannerAdapter)
        mAdapters.add(commonAdapter)
        mAdapters.add(titleAdapter)
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

    open fun initData(page: Int) {
        if (page == 0) this.page = 0
        this.page += 1
        Repo.getLessonByPage(page, 20, object : Callback<LessonMsg> {
            override fun onFailure(call: Call<LessonMsg>, t: Throwable) {
                Utils.toast("??????????????????:????????????")
                if (page == 0) {
                    mDelegateAdapter!!.setAdapters(mAdapters)
                    refreshLayout.finishRefresh()
                } else {
                    refreshLayout.finishLoadMore()
                }
            }

            override fun onResponse(
                call: Call<LessonMsg>,
                response: Response<LessonMsg>
            ) {
                if (response.body()!!.status == StatusType.SUCCESSFUL) {
                    if (page == 0) {
                        mNewsAdapter.refresh(response.body()!!.content.content)
                        mDelegateAdapter!!.setAdapters(mAdapters)
                    } else {
                        if (response.body()!!.content.empty) {
                            this@MainFragmentVLayout.page -= 1
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