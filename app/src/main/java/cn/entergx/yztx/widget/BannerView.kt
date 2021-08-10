package cn.entergx.yztx.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import cn.entergx.yztx.R
import cn.entergx.yztx.utils.Utils
import com.rishabhharit.roundedimageview.RoundedImageView

class BannerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private val mContext: Context = context
    private val vpBanner: ViewPager
    private val piBanner: PagerIndicator
    private val mViewList = ArrayList<ImageView>()
    private var mInterval = 4000L//轮播间隔

    // 初始化视图
    init {
        // 根据布局文件banner_indicator.xml生成视图对象
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.banner_indicator, null)
        // 从布局文件中获取名叫vp_banner的翻页视图
        vpBanner = view.findViewById(R.id.vp_banner)
        // 从布局文件中获取名叫pi_banner的翻页指示器
        piBanner = view.findViewById(R.id.pi_banner)
        addView(view) // 将该布局视图添加到横幅指示器中
    }

    //判断mViewList是否为空
    fun isListEmpty(): Boolean {
        return mViewList.isEmpty()
    }

    // 设置广告图片队列
    fun setImage(imageList: ArrayList<Int>) {
        val padding = Utils.dip2px(context,30f)

        // 根据图片队列生成图像视图队列
        for (i in imageList.indices) {
            val imageID = imageList[i]
            val iv = RoundedImageView(mContext)
            //设置圆角ImageView的各种属性
            iv.apply {
                layoutParams = LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setCornerRadius(Utils.dip2px(context,4F))
                setRoundedCorners(RoundedImageView.ALL_ROUNDED_CORNERS_VALUE)
                scaleType = ImageView.ScaleType.CENTER_CROP
                //setPadding(Utils.dip2px(context, 15f), 0, Utils.dip2px(context, 15f), 0)
                setImageResource(imageID)
                setOnClickListener(this@BannerView)
            }
            mViewList.add(iv)
        }
        vpBanner.apply {
            // 设置翻页视图的图像翻页适配器
            adapter = ImageAdapter()
            // 给翻页视图添加页面变更监听器
            addOnPageChangeListener(BannerChangeListener())
            // 设置翻页视图默认显示第一页
            currentItem = mViewList.size * 500
        }
        //初始化指示器
        piBanner.apply {
            setCount(imageList.size, 15f)
            piBanner.setRRLength(50f)
            piBanner.setRadius(8f)
        }
    }

    //设置自动播放间隔
    fun setInterval(interval:Long){
        mInterval = interval
    }

    //--------------------------点击监听相关---------------------

    override fun onClick(v: View?) {
        // 获取翻页视图当前页面项的序号
        val position = vpBanner.currentItem % mViewList.size
        // 触发点击监听器的onBannerClick方法
        mListener!!.onBannerClick(position)
    }

    // 声明一个广告图点击的监听器对象
    private var mListener: BannerClickListener? = null

    // 设置广告图的点击监听器
    fun setOnBannerListener(listener: BannerClickListener?) {
        mListener = listener
    }

    // 定义一个广告图片的点击监听器接口
    interface BannerClickListener {
        fun onBannerClick(position: Int)
    }

    //--------------------定义图像翻页适配器-----------------------

    inner class ImageAdapter : PagerAdapter() {
        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
            return arg0 == arg1
        }

        override fun getCount(): Int {
            return Int.MAX_VALUE
        }

        // 从容器中销毁指定位置的页面
        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(mViewList[position % mViewList.size])
        }

        // 实例化指定位置的页面，并将其添加到容器中
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(mViewList[position % mViewList.size])
            return mViewList[position % mViewList.size]
        }
    }

    //------------------------定义广告轮播监听器--------------------

    inner class BannerChangeListener : ViewPager.OnPageChangeListener {
        // 翻页状态改变时触发
        override fun onPageScrollStateChanged(arg0: Int) {
        }

        // 在翻页过程中触发
        @SuppressLint("ClickableViewAccessibility")
        override fun onPageScrolled(
            seq: Int,
            ratio: Float,
            offset: Int
        ) {
            //触摸时暂停轮播
            vpBanner.setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_MOVE)
                    stop()
                else
                    start()
                false
            }
            // 设置指示器长条的位置

            if (seq % mViewList.size != mViewList.size - 1)
            piBanner.setCurrent(seq % mViewList.size, ratio)
        }

        // 在翻页结束后触发
        override fun onPageSelected(seq: Int) {
            // 设置指示器长条的位置
            piBanner.setCurrent(seq % mViewList.size, 0f)
        }
    }


    //-----------------------------自动轮播相关------------------------


    //开始轮播
    fun start() {
        mHandler.removeCallbacks(mScroll)
        mHandler.postDelayed(mScroll, mInterval)
    }

    //停止轮播
    fun stop() {
        mHandler.removeCallbacks(mScroll)
    }

    // 声明一个处理器对象
    private var mHandler = Handler()
    // 定义一个滚动任务
    private val mScroll = object : Runnable {
        override fun run() {
            scrollToNext() // 滚动广告图片
            // 延迟若干秒后继续启动滚动任务
            mHandler.postDelayed(this, mInterval)
        }
    }

    // 滚动到下一张广告图
    fun scrollToNext() {
        // 设置翻页视图显示指定位置的页面
        vpBanner.currentItem = 1 + vpBanner.currentItem
    }

}