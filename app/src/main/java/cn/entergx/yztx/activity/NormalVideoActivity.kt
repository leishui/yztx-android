package cn.entergx.yztx.activity

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager.widget.ViewPager
import cn.entergx.yztx.R
import cn.entergx.yztx.adapter.VideoPageAdapter
import cn.entergx.yztx.bean.bean.Lesson
import cn.entergx.yztx.constant.ResourceType
import cn.entergx.yztx.constant.StatusType
import cn.entergx.yztx.fragment.video.IntroFragment
import cn.entergx.yztx.fragment.video.CommentFragmentVLayout
import cn.entergx.yztx.msg.SimpleMsg
import cn.entergx.yztx.network.Repo
import cn.entergx.yztx.utils.SPUtils
import cn.entergx.yztx.utils.Utils
import cn.entergx.yztx.utils.Utils.getScreenWidth
import com.bumptech.glide.Glide
import com.example.gsyvideoplayer.listener.AppBarStateChangeListener
import com.example.gsyvideoplayer.video.LandLayoutVideo
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.CommonUtil
import com.shuyu.gsyvideoplayer.utils.Debuger
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import kotlinx.android.synthetic.main.content_scrolling.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NormalVideoActivity : AppCompatActivity() {
    private var review: View? = null
    private lateinit var adapter: VideoPageAdapter
    private var isPlay = false
    private var isPause = false
    private var isSmall = false
    private var orientationUtils: OrientationUtils? = null
    private var detailPlayer: LandLayoutVideo? = null
    private var appBar: AppBarLayout? = null
    private var fab: FloatingActionButton? = null
    private var root: CoordinatorLayout? = null
    private var tb: TabLayout? = null
    private var vp: ViewPager? = null
    private var toolBarLayout: CollapsingToolbarLayout? = null
    private var curState: AppBarStateChangeListener.State? = null
    private lateinit var lesson: Lesson
    private var isComment = false
    private var popupWindow: PopupWindow? = null
    private var popComment: PopupWindow? = null
    private lateinit var commentFragmentVLayout:CommentFragmentVLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        review = LayoutInflater.from(this).inflate(
            R.layout.activity_normal_video, null
        )
        setContentView(R.layout.activity_normal_video)
        lesson = intent.getSerializableExtra("lesson") as Lesson
        initView()
        //增加封面
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(this).load(lesson.cover_url)
            .placeholder(R.drawable.ic_gank)
            .error(R.drawable.ic_error).into(imageView)
        imageView.setImageResource(R.mipmap.xxx1)
        resolveNormalVideoUI()

        //外部辅助的旋转，帮助全屏
        orientationUtils = OrientationUtils(this, detailPlayer)
        //初始化不打开外部的旋转
        orientationUtils!!.isEnable = false
        val gsyVideoOption = GSYVideoOptionBuilder()
        gsyVideoOption.setThumbImageView(imageView)
            .setIsTouchWiget(true)
            .setRotateViewAuto(false)
            .setLockLand(false)
            .setShowFullAnimation(false)
            .setNeedLockFull(true)
            .setSeekRatio(1f)
            .setUrl(lesson.resource_url)
            .setCacheWithPlay(false)
            .setVideoTitle(lesson.name)
            .setVideoAllCallBack(object : GSYSampleCallBack() {
                override fun onStartPrepared(
                    url: String,
                    vararg objects: Any
                ) {
                    super.onStartPrepared(url, *objects)
                    Log.d(TAG, "onStartPrepared: ")
                    setScroll(false)
                }

                override fun onClickStop(
                    url: String,
                    vararg objects: Any
                ) {
                    super.onClickStop(url, *objects)
                    Log.d(TAG, "onClickStop: ")
                    setScroll(true)
                }

                override fun onClickResume(
                    url: String,
                    vararg objects: Any
                ) {
                    super.onClickResume(url, *objects)
                    Log.d(TAG, "onClickResume: ")
                    setScroll(false)
                }

                override fun onClickResumeFullscreen(
                    url: String,
                    vararg objects: Any
                ) {
                    super.onClickResumeFullscreen(url, *objects)
                    Log.d(TAG, "onClickResumeFullscreen: ")
                    setScroll(false)
                }

                override fun onClickStopFullscreen(
                    url: String,
                    vararg objects: Any
                ) {
                    super.onClickStopFullscreen(url, *objects)
                    Log.d(TAG, "onClickStopFullscreen: ")
                    setScroll(true)
                }

                override fun onPrepared(
                    url: String,
                    vararg objects: Any
                ) {
                    Debuger.printfError("***** onPrepared **** " + objects[0])
                    Debuger.printfError("***** onPrepared **** " + objects[1])
                    super.onPrepared(url, *objects)
                    //开始播放了才能旋转和全屏
                    orientationUtils!!.isEnable = detailPlayer!!.isRotateWithSystem
                    isPlay = true
                    root!!.removeView(fab)
                }

                override fun onEnterFullscreen(
                    url: String,
                    vararg objects: Any
                ) {
                    super.onEnterFullscreen(url, *objects)
                    Debuger.printfError("***** onEnterFullscreen **** " + objects[0]) //title
                    Debuger.printfError("***** onEnterFullscreen **** " + objects[1]) //当前全屏player
                }

                override fun onQuitFullscreen(
                    url: String,
                    vararg objects: Any
                ) {
                    super.onQuitFullscreen(url, *objects)
                    Debuger.printfError("***** onQuitFullscreen **** " + objects[0]) //title
                    Debuger.printfError("***** onQuitFullscreen **** " + objects[1]) //当前非全屏player
                    if (orientationUtils != null) {
                        orientationUtils!!.backToProtVideo()
                    }
                }
            })
            .setLockClickListener { _, lock ->
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils!!.isEnable = !lock
                }
            }
            .setGSYVideoProgressListener { progress, secProgress, currentPosition, duration ->
                Debuger.printfLog(
                    " progress $progress secProgress $secProgress currentPosition $currentPosition duration $duration"
                )
            }
            .build(detailPlayer)
        detailPlayer!!.fullscreenButton
            .setOnClickListener { //直接横屏
                orientationUtils!!.resolveByClick()

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                detailPlayer!!.startWindowFullscreen(this@NormalVideoActivity, true, true)
            }
        detailPlayer!!.setLinkScroll(true)
    }

    override fun onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils!!.backToProtVideo()
        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        curPlay!!.onVideoPause()
        super.onPause()
        isPause = true
    }

    override fun onResume() {
        curPlay!!.onVideoResume()
        //setScroll(false)
        appBar!!.addOnOffsetChangedListener(appBarStateChangeListener)
        super.onResume()
        isPause = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isPlay) {
            curPlay!!.release()
        }
        if (orientationUtils != null) orientationUtils!!.releaseListener()
    }

    /**
     * orientationUtils 和  detailPlayer.onConfigurationChanged 方法是用于触发屏幕旋转的
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            detailPlayer!!.onConfigurationChanged(this, newConfig, orientationUtils, true, true)
        }
    }

    private fun initView() {
        val toolbar =
            findViewById<Toolbar>(R.id.toolbar)
        detailPlayer = findViewById(R.id.detail_player)
        tb = findViewById(R.id.tl_video_scroll)
        vp = findViewById(R.id.vp_video_scroll)
        initContent()
        root = findViewById(R.id.root_layout)
        setSupportActionBar(toolbar)
        toolBarLayout = findViewById(R.id.toolbar_layout)
        toolBarLayout!!.setCollapsedTitleTextColor(Color.WHITE)
        toolBarLayout!!.setExpandedTitleColor(Color.TRANSPARENT)
        toolBarLayout!!.title = lesson.name
        fab = findViewById(R.id.fab)
        fab!!.setOnClickListener(View.OnClickListener {
            detailPlayer!!.startPlayLogic()
            root!!.removeView(fab)
        })
        appBar = findViewById(R.id.app_bar)
        val bvParams = detailPlayer!!.layoutParams
        val abParams = appBar!!.layoutParams
        val height = getScreenWidth(this) * 9 / 16
        bvParams.height = height
        abParams.height = height
        appBar!!.layoutParams = abParams
        detailPlayer!!.layoutParams = bvParams
        appBar!!.addOnOffsetChangedListener(appBarStateChangeListener)
    }

    private fun initContent() {
        commentFragmentVLayout = CommentFragmentVLayout(
            lesson.lessonId
        )
        adapter = VideoPageAdapter(
            this, supportFragmentManager, IntroFragment(lesson), commentFragmentVLayout
        )
        initPopComment()
        initPopOpen()
        initVpAndTb()
    }

    private fun initPopComment() {
        popComment = PopupWindow(this)
        popComment?.apply {
            contentView =
                LayoutInflater.from(this@NormalVideoActivity).inflate(R.layout.popup_comment, null)
            width = ViewGroup.LayoutParams.MATCH_PARENT
            setBackgroundDrawable(getDrawable(R.color.white))
            //animationStyle = R.style.PopupSchool
            isFocusable = true
            val et = contentView.findViewById<EditText>(R.id.et_comment_content)
            contentView.findViewById<Button>(R.id.bt_comment_send)
                .setOnClickListener {
                    popComment?.dismiss()
                    sendComment(
                        lesson.lessonId,
                        et.text.toString(),
                        SPUtils.getUser().userId
                    )
                    et.text.clear()
                }
        }
    }

    private fun sendComment(comment_id: Long, content: String, user_id: Long) {
        Repo.saveComment(comment_id, content, user_id, ResourceType.LESSON, object : Callback<SimpleMsg> {
            override fun onFailure(call: Call<SimpleMsg>, t: Throwable) {
                Utils.toast(this@NormalVideoActivity, "发送失败")
            }

            override fun onResponse(call: Call<SimpleMsg>, response: Response<SimpleMsg>) {
                if (response.body()?.status == StatusType.SUCCESSFUL) {
                    Utils.toast(this@NormalVideoActivity, "发送成功")
                    commentFragmentVLayout.initData(0)
                    return
                }
                Utils.toast(this@NormalVideoActivity, "发送失败：" + response.body()?.msg)
            }
        })
    }

    private fun initVpAndTb() {
        vp!!.adapter = adapter
        tb!!.setupWithViewPager(vp)
        tb!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 1) {
                    isComment = true
                    popupWindow?.showAtLocation(
                        review,
                        Gravity.BOTTOM,
                        0,
                        0
                    )
                } else if (tab?.position == 0) {
                    isComment = false
                    popupWindow?.dismiss()
                }
            }
        })
    }

    private fun initPopOpen() {
        popupWindow = PopupWindow(this)
        popupWindow?.apply {
            contentView =
                LayoutInflater.from(this@NormalVideoActivity)
                    .inflate(R.layout.popup_open_comment, null)
            width = ViewGroup.LayoutParams.MATCH_PARENT
            setBackgroundDrawable(getDrawable(R.color.white))
            animationStyle = R.style.PopupSchool
            //isFocusable = true
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

    private fun resolveNormalVideoUI() {
        //增加title
        detailPlayer!!.titleTextView.visibility = View.GONE
        detailPlayer!!.backButton.visibility = View.GONE
    }

    private val curPlay: GSYVideoPlayer?
        get() = if (detailPlayer!!.fullWindowPlayer != null) {
            detailPlayer!!.fullWindowPlayer
        } else detailPlayer

    private var appBarStateChangeListener: AppBarStateChangeListener =
        object : AppBarStateChangeListener() {
            override fun onStateChanged(
                appBarLayout: AppBarLayout,
                state: State
            ) {
                println("--------------$state")
                when (state) {
                    State.EXPANDED -> {
                        //展开状态
                        curState = state
                        //toolBarLayout.setTitle("");
                    }
                    State.COLLAPSED -> {
                        //折叠状态
                        //如果是小窗口就不需要处理
                        //toolBarLayout.setTitle("Title");
                        if (!isSmall && isPlay) {
                            isSmall = true
                            val size = CommonUtil.dip2px(
                                this@NormalVideoActivity,
                                150f
                            )
                            //detailPlayer.showSmallVideo(new Point(size, size), true, true);
                            orientationUtils!!.isEnable = false
                        }
                        curState = state
                    }
                    else -> {
                        if (curState == State.COLLAPSED) {
                            //由折叠变为中间状态
                            //toolBarLayout.setTitle("");
                            if (isSmall) {
                                isSmall = false
                                orientationUtils!!.isEnable = true
                                //必须
                                //                        detailPlayer.postDelayed(new Runnable() {
                                //                            @Override
                                //                            public void run() {
                                //                                detailPlayer.hideSmallVideo();
                                //                            }
                                //                        }, 50);
                            }
                        }
                        curState = state
                        //中间状态
                    }
                }
            }
        }

    private fun setScroll(bool: Boolean) {

        val layoutParams =
            toolBarLayout!!.layoutParams as AppBarLayout.LayoutParams
        if (bool) layoutParams.scrollFlags =
            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED else layoutParams.scrollFlags =
            AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
        toolBarLayout!!.layoutParams = layoutParams
        vp_video_scroll.isClickable = false
//        val l = ll_fuck.layoutParams as CoordinatorLayout.LayoutParams
//        l.behavior = CoordinatorLayout
    }

    companion object {
        private const val TAG = "*****"
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_UP && isComment) {
            popupWindow?.showAtLocation(
                review,
                Gravity.BOTTOM,
                0,
                0
            )
        } else if (ev?.action == MotionEvent.ACTION_DOWN && isComment) popupWindow?.dismiss()
        Log.d(TAG, "dispatchTouchEvent: " + ev.toString())
        return super.dispatchTouchEvent(ev)
    }
}