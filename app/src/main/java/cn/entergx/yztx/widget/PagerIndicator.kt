package cn.entergx.yztx.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.LinearLayout

class PagerIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mCount = 5 // 指示器的个数
    private var mPad = 5f// 两个圆点之间的间隔
    private var mSeq = 0 // 当前指示器的序号
    private var mRatio = 0.0f // 已经移动的距离百分比
    private var mPaintC = Paint()// 声明一个画笔对象，用来画背景圆点
    private var mPaintRR = Paint()//声明一个画笔对象，用来画前景的长条
    private var mRadius = 10f//背景圆点的半径，以及前景长条（圆角矩形）的圆角
    private var mLength = 40f//前景长条（圆角矩形）的长度


    init {
        //初始化画笔属性
        mPaintC.color = Color.WHITE
        mPaintRR.color = Color.WHITE
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        //计算获取指示器最左边的位置坐标
        val left = (measuredWidth - (mCount - 1) * (mPad+2*mRadius) - mLength) / 2
        //获得垂直位置的中点坐标
        val y = (measuredHeight / 2).toFloat()
        //第一个圆点的位置，即绘制圆点的起始位置
        var x = left + mLength-mRadius
        //计算实时的坐标，绘制作为背景的几个白色圆点
        for (i in 0 until mCount) {
            when {
                i == mSeq -> canvas.drawCircle(x-(mLength/2 - mRadius)-mRatio*(mLength/2 - mRadius), y, mRadius, mPaintC)
                i == mSeq + 1 -> canvas.drawCircle(x-mRatio*(mLength/2 - mRadius), y, mRadius, mPaintC)
                i>mSeq+1 -> canvas.drawCircle(x, y, mRadius, mPaintC)
                else -> canvas.drawCircle(x - (mLength - 2*mRadius), y, mRadius, mPaintC)
            }
            x += 2 * mRadius + mPad
        }
        // 再绘制作为前景的白色长条，该长条随着翻页滑动而左右滚动
        val rectF = RectF(
            left+ (mSeq + mRatio) * (mPad+2*mRadius),
            y - mRadius,
            left + mLength+ (mSeq + mRatio) * (mPad+2*mRadius),
            y + mRadius
        )
        canvas.drawRoundRect(rectF, mRadius, mRadius, mPaintRR)
    }

    // 设置指示器的个数，以及指示器之间的距离
    fun setCount(count: Int, pad: Float) {
        mCount = count
        mPad = pad
        invalidate() // 立刻刷新视图
    }

    // 设置指示器当前移动到的位置，及其位移比率
    fun setCurrent(seq: Int, ratio: Float) {
        mSeq = seq
        mRatio = ratio
        invalidate() // 立刻刷新视图
    }

    //设置圆点大小，即长条高度
    fun setRadius(radius:Float){
        mRadius = radius
    }

    //设置长条长度
    fun setRRLength(length:Float){
        mLength = length
    }

}