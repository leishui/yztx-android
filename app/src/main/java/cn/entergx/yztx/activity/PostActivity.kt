package cn.entergx.yztx.activity

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import cn.entergx.yztx.R
import cn.entergx.yztx.adapter.delegate.SimpleDelegateAdapter
import cn.entergx.yztx.adapter.delegate.SingleDelegateAdapter
import cn.entergx.yztx.bean.bean.Comment
import cn.entergx.yztx.bean.bean.Post
import cn.entergx.yztx.constant.ResourceType
import cn.entergx.yztx.constant.StatusType
import cn.entergx.yztx.msg.PageMsg
import cn.entergx.yztx.msg.SimpleMsg
import cn.entergx.yztx.network.Repo
import cn.entergx.yztx.utils.SPUtils
import cn.entergx.yztx.utils.Utils
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.luck.picture.lib.tools.ScreenUtils
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder
import kotlinx.android.synthetic.main.fragment_v_community.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostActivity : AppCompatActivity() {

    private var page = 0
    lateinit var post: Post
    lateinit var recyclerView: RecyclerView
    lateinit var refreshLayout: SmartRefreshLayout
    private lateinit var mNewsAdapter: SimpleDelegateAdapter<Comment>
    var mDelegateAdapter: DelegateAdapter? = null
    val mAdapters: MutableList<DelegateAdapter.Adapter<*>> =
        ArrayList()
    private var popupWindow: PopupWindow? = null
    private var popComment: PopupWindow? = null
    private lateinit var review: View
    private lateinit var bottomAdapter: SingleDelegateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_v_post)
        review = LayoutInflater.from(this@PostActivity).inflate(
            R.layout.fragment_v_post, null
        )
        initPost()
        initViews()
        initListeners()
        initPopComment()
        initPopOpen()
    }

    private fun initPost() {
        post = intent.getSerializableExtra("post") as Post
    }

    fun initViews() {
        recyclerView = rv_v
        refreshLayout = rf_v
        // 1.??????VirtualLayoutManager
        val virtualLayoutManager = VirtualLayoutManager(this)
        recyclerView.layoutManager = virtualLayoutManager

        // 2.??????RecycledViews???????????????????????????
        val viewPool = RecyclerView.RecycledViewPool()
        viewPool.setMaxRecycledViews(0, 20)
        recyclerView.setRecycledViewPool(viewPool)



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
                    holder.text(R.id.tv_comment_content, model.comment_content)
                    holder.text(R.id.tv_up_time_comment, Utils.transToString(model.comment_time))
                    holder.image(R.id.ri_avatar_comment, model.commentator_url)
                }
            }
        }
        bottomAdapter = object : SingleDelegateAdapter(R.layout.adapter_vlayout_bottom_item) {
            override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

            }

        }
        val topAdapter = object : SingleDelegateAdapter(R.layout.activity_post) {
            override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
                holder.text(R.id.tv_post_title, post.post_name)
                holder.text(R.id.tv_post_content, post.post_content)
                val linearLayout = holder.findViewById<LinearLayout>(R.id.ll_post_photo)
                linearLayout.removeAllViews()
                if (post.postResourcesList != null)
                    for (s in post.postResourcesList) {
                        val iv = ImageView(this@PostActivity)
                        val layoutParams =
                            LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                        iv.layoutParams = layoutParams
                        iv.setPadding(5, 5, 5, 5)
                        iv.scaleType = ImageView.ScaleType.CENTER_CROP
                        Glide.with(this@PostActivity).asBitmap().load(s)
                            .placeholder(R.drawable.ic_gank)
                            .error(R.drawable.ic_error).into(object :
                                SimpleTarget<Bitmap>() {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap>?
                                ) {
                                    val width: Int = resource.width
                                    val height: Int = resource.height
                                    layoutParams.height =
                                        Utils.getScreenWidth(this@PostActivity) * height / width
                                    iv.layoutParams = layoutParams
                                    iv.setImageBitmap(resource)
                                }
                            })
                        //holder.image(i,s)
                        linearLayout.addView(iv)
                    }
            }

        }
        mDelegateAdapter = DelegateAdapter(virtualLayoutManager)
        mAdapters.add(topAdapter)
        //mAdapters.add(ottomAdapter)
        mAdapters.add(mNewsAdapter)
        mAdapters.add(bottomAdapter)

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
        Repo.getCommentsAndReplies(post.postId,
            ResourceType.POST, page, 20, object : Callback<PageMsg<Comment>> {
                override fun onFailure(call: Call<PageMsg<Comment>>, t: Throwable) {
                    Utils.toast("??????????????????:????????????")
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
                                this@PostActivity.page -= 1
                                refreshLayout.finishLoadMoreWithNoMoreData()
                                Utils.toast("???????????????")
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

    private fun initPopComment() {
        popComment = PopupWindow(this)
        popComment?.apply {
            contentView =
                LayoutInflater.from(this@PostActivity).inflate(R.layout.popup_comment, null)
            width = ViewGroup.LayoutParams.MATCH_PARENT
            setBackgroundDrawable(getDrawable(R.color.white))
            //animationStyle = R.style.PopupSchool
            isFocusable = true
            val et = contentView.findViewById<EditText>(R.id.et_comment_content)
            contentView.findViewById<Button>(R.id.bt_comment_send)
                .setOnClickListener {
                    popComment?.dismiss()
                    sendComment(
                        post.postId,
                        et.text.toString(),
                        SPUtils.getUser().userId
                    )
                    et.text.clear()
                }
        }
    }

    private fun sendComment(comment_id: Long, content: String, user_id: Long) {
        Repo.saveComment(
            comment_id,
            content,
            user_id,
            ResourceType.POST,
            object : Callback<SimpleMsg> {
                override fun onFailure(call: Call<SimpleMsg>, t: Throwable) {
                    Utils.toast(this@PostActivity, "????????????")
                }

                override fun onResponse(call: Call<SimpleMsg>, response: Response<SimpleMsg>) {
                    if (response.body()?.status == StatusType.SUCCESSFUL) {
                        Utils.toast(this@PostActivity, "????????????")
                        initData(0)
                        return
                    }
                    Utils.toast(this@PostActivity, "???????????????" + response.body()?.msg)
                }
            })
    }

    private fun initPopOpen() {
        popupWindow = PopupWindow(this)
        popupWindow?.apply {
            contentView =
                LayoutInflater.from(this@PostActivity)
                    .inflate(R.layout.popup_open_comment, null)
            width = ViewGroup.LayoutParams.MATCH_PARENT
            setBackgroundDrawable(getDrawable(R.color.white))
            animationStyle = R.style.PopupSchool
            contentView.findViewById<TextView>(R.id.tv_open_comment)
                .setOnClickListener {
                    //popupWindow?.dismiss()
                    popComment?.showAtLocation(
                        review,
                        Gravity.BOTTOM,
                        0,
                        0
                    )
                    val edit =
                        popComment?.contentView?.findViewById<EditText>(R.id.et_comment_content)
                    edit?.requestFocus()
                    val imm =
                        edit?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)
                }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_UP) {
            popupWindow?.showAtLocation(
                review,
                Gravity.BOTTOM,
                0,
                0
            )
        } else if (ev?.action == MotionEvent.ACTION_DOWN) popupWindow?.dismiss()
        return super.dispatchTouchEvent(ev)
    }
}