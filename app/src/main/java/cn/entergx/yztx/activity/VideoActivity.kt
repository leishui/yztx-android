package cn.entergx.yztx.activity

import android.annotation.SuppressLint
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.entergx.yztx.R
import cn.entergx.yztx.media.MediaController
import cn.entergx.yztx.media.VideoPlayerView
import cn.entergx.yztx.media.VideoPlayerView.OnControllerEventsListener
import cn.entergx.yztx.media.callback.DanmukuSwitchListener
import cn.entergx.yztx.media.callback.VideoBackListener
import cn.entergx.yztx.utils.Utils
import kotlinx.android.synthetic.main.activity_video_player.*
import master.flame.danmaku.controller.IDanmakuView
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.IDisplayer
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import tv.danmaku.ijk.media.player.IMediaPlayer
import java.util.*

class VideoActivity : AppCompatActivity(), DanmukuSwitchListener, VideoBackListener {
    private lateinit var videoView: VideoPlayerView

    var mDanmakuView: IDanmakuView? = null
    private var mPlayerView: VideoPlayerView? = null
    private var mBufferingIndicator: View? = null
    private var mVideoPrepareLayout: RelativeLayout? = null
    private var mAnimImageView: ImageView? = null
    private var mPrepareText: TextView? = null

    private var cid = 0
    private var title: String? = null
    private var LastPosition = 0
    private var startText = "初始化播放器..."
    private var mLoadingAnim: AnimationDrawable? = null
    private var danmakuContext: DanmakuContext? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        mDanmakuView = sv_danmaku
        mPlayerView = playerView
        mBufferingIndicator = buffering_indicator
        mVideoPrepareLayout = video_start
        mAnimImageView = bili_anim
        mPrepareText = video_start_info
        val bvHeight = Utils.getScreenWidth(this) * 9 / 16
        val name = intent.getStringExtra("name")
        val url = intent.getStringExtra("url")
        val des = intent.getStringExtra("des")
        var id = intent.getLongExtra("id", 0)
        //tv_video.text = des
//        videoView = bd_video
//        val bvParams: ViewGroup.LayoutParams = videoView.layoutParams
//        bvParams.height = bvHeight
//        videoView.layoutParams = bvParams
//        videoView.setMediaController(MediaController(this))
//        videoView.setVideoURI(Uri.parse(url))
//        videoView.start()
        initViews()
    }

//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        }
//    }

    private fun initViews() {
//        val intent = intent
//        if (intent != null) {
//            cid = intent.getIntExtra(ConstantUtil.EXTRA_CID, 0)
//            title = intent.getStringExtra(ConstantUtil.EXTRA_TITLE)
//        }
        initAnimation()
        initMediaPlayer()
        initToolBar()
    }

    @SuppressLint("UseSparseArrays")
    private fun initMediaPlayer() {
        //配置播放器
        val mMediaController =
            MediaController(this)
        mMediaController.setTitle(title)
        mPlayerView?.setMediaController(mMediaController)
        mPlayerView?.setMediaBufferingIndicator(mBufferingIndicator)
        mPlayerView?.requestFocus()
        mPlayerView?.setOnInfoListener(onInfoListener)
        mPlayerView?.setOnSeekCompleteListener(onSeekCompleteListener)
        mPlayerView?.setOnCompletionListener(onCompletionListener)
        mPlayerView?.setOnControllerEventsListener(onControllerEventsListener)
        //设置弹幕开关监听
        mMediaController.setDanmakuSwitchListener(this)
        //设置返回键监听
        mMediaController.setVideoBackEvent(this)
        //配置弹幕库
        mDanmakuView?.enableDanmakuDrawingCache(true)
        //设置最大显示行数
        val maxLinesPair = HashMap<Int, Int>()
        //滚动弹幕最大显示5行
        maxLinesPair[BaseDanmaku.TYPE_SCROLL_RL] = 5
        //设置是否禁止重叠
        val overlappingEnablePair =
            HashMap<Int, Boolean>()
        overlappingEnablePair[BaseDanmaku.TYPE_SCROLL_RL] = true
        overlappingEnablePair[BaseDanmaku.TYPE_FIX_TOP] = true
        //设置弹幕样式
        danmakuContext = DanmakuContext.create()
        danmakuContext?.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3f)
            ?.setDuplicateMergingEnabled(false)?.setScrollSpeedFactor(1.2f)?.setScaleTextSize(0.8f)
            ?.setMaximumLines(maxLinesPair)?.preventOverlapping(overlappingEnablePair)
        loadData()
    }

    private fun loadData() {
        mPlayerView!!.setVideoURI(Uri.parse("https://vd4.bdstatic.com/mda-idnmtuazxbdrpgcb/1080p/mda-idnmtuazxbdrpgcb.mp4"))

        //mPlayerView!!.setVideoURI(Uri.parse("https://vd4.bdstatic.com/mda-idnmtuazxbdrpgcb/1080p/mda-idnmtuazxbdrpgcb.mp4"))
        mPlayerView!!.setOnPreparedListener {
            mLoadingAnim!!.stop()
            startText = "$startText【完成】\n视频缓冲中..."
            mPrepareText!!.text = startText
            mVideoPrepareLayout!!.visibility = View.GONE
            mPlayerView!!.start()
        }
    }


    /**
     * 初始化加载动画
     */
    private fun initAnimation() {
        mVideoPrepareLayout?.visibility = View.VISIBLE
        startText = "$startText【完成】\n解析视频地址...【完成】\n全舰弹幕填装..."
        mPrepareText?.text = startText
        mLoadingAnim = mAnimImageView?.background as AnimationDrawable
        mLoadingAnim!!.start()
    }


    private fun initToolBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.setBackgroundDrawable(null)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    /**
     * 视频缓冲事件回调
     */
    private val onInfoListener =
        IMediaPlayer.OnInfoListener { mp, what, extra ->
            if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
                if (mDanmakuView != null && mDanmakuView!!.isPrepared) {
                    mDanmakuView!!.pause()
                    mBufferingIndicator?.visibility = View.VISIBLE
                }
            } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
                if (mDanmakuView != null && mDanmakuView!!.isPaused) {
                    mDanmakuView!!.resume()
                }
                mBufferingIndicator?.visibility = View.GONE
            }
            true
        }

    /**
     * 视频跳转事件回调
     */
    private val onSeekCompleteListener =
        IMediaPlayer.OnSeekCompleteListener { mp ->
            if (mDanmakuView != null && mDanmakuView!!.isPrepared) {
                mDanmakuView!!.seekTo(mp.currentPosition)
            }
        }

    /**
     * 视频播放完成事件回调
     */
    private val onCompletionListener =
        IMediaPlayer.OnCompletionListener {
            if (mDanmakuView != null && mDanmakuView!!.isPrepared) {
                mDanmakuView!!.seekTo(0.toLong())
                mDanmakuView!!.pause()
            }
            mPlayerView?.pause()
        }

    /**
     * 控制条控制状态事件回调
     */
    private val onControllerEventsListener: OnControllerEventsListener =
        object : OnControllerEventsListener {
            override fun onVideoPause() {
                if (mDanmakuView != null && mDanmakuView!!.isPrepared) {
                    mDanmakuView!!.pause()
                }
            }

            override fun OnVideoResume() {
                if (mDanmakuView != null && mDanmakuView!!.isPaused) {
                    mDanmakuView!!.resume()
                }
            }
        }


    override fun onResume() {
        super.onResume()
        if (mDanmakuView != null && mDanmakuView!!.isPrepared && mDanmakuView!!.isPaused) {
            mDanmakuView!!.seekTo(LastPosition as Long)
        }
        if (mPlayerView != null && !mPlayerView!!.isPlaying) {
            mPlayerView!!.seekTo(LastPosition.toLong())
        }
    }


    override fun onPause() {
        super.onPause()
        if (mPlayerView != null) {
            LastPosition = mPlayerView!!.currentPosition
            mPlayerView!!.pause()
        }
        if (mDanmakuView != null && mDanmakuView!!.isPrepared) {
            mDanmakuView!!.pause()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        if (mDanmakuView != null) {
            mDanmakuView!!.release()
            mDanmakuView = null
        }
        if (mLoadingAnim != null) {
            mLoadingAnim!!.stop()
            mLoadingAnim = null
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (mPlayerView != null && mPlayerView!!.isDrawingCacheEnabled) {
            mPlayerView!!.destroyDrawingCache()
        }
        if (mDanmakuView != null && mDanmakuView!!.isPaused) {
            mDanmakuView!!.release()
            mDanmakuView = null
        }
        if (mLoadingAnim != null) {
            mLoadingAnim!!.stop()
            mLoadingAnim = null
        }
    }


    /**
     * 弹幕开关回调
     */
    override fun setDanmakuShow(isShow: Boolean) {
        if (mDanmakuView != null) {
            if (isShow) {
                mDanmakuView!!.show()
            } else {
                mDanmakuView!!.hide()
            }
        }
    }


    /**
     * 退出界面回调
     */
    override fun back() {
        onBackPressed()
    }

}