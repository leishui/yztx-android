package cn.entergx.yztx.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.text.InputFilter
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.entergx.yztx.R
import com.luck.picture.lib.PictureSelectionModel
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.xuexiang.xui.widget.imageview.preview.PreviewBuilder
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object Utils {


    fun transToString(time:Long):String{
        return SimpleDateFormat("YYYY-MM-dd").format(Date(time*1000))
    }
    fun getYear(): String {
        return SimpleDateFormat("YYYY").format(System.currentTimeMillis())
    }
    // 根据手机的分辨率从 dp 的单位 转成为 px(像素)
    fun dip2px(context: Context, dpValue: Float): Int { // 获取当前手机的像素密度
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt() // 四舍五入取整
    }

    // 获得屏幕的宽度
    fun getScreenWidth(ctx: Context): Int { // 从系统服务中获取窗口管理器
        val wm =
            ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        // 从默认显示器中获取显示参数保存到dm对象中
        wm.defaultDisplay.getMetrics(dm)
        return dm.widthPixels // 返回屏幕的宽度数值
    }

    //弹出短时的Toast
    fun toast(context: Context,string: CharSequence){
        Toast.makeText(context,string,Toast.LENGTH_SHORT).show()
    }

    /**
     * 设置背景颜色
     *
     * @param bgAlpha
     */
    fun setBackgroundAlpha(bgAlpha:Float, mContext:Context) {
        val lp = (mContext as Activity).window.attributes
        lp.alpha = bgAlpha
        mContext.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        mContext.window.attributes = lp
    }

    /**
     * 禁止EditText输入空格
     * @param editText
     */
    fun setEditTextInhibitInputSpace(editText: EditText) {
        val filter = InputFilter { source, _, _, _, _, _ ->
            if (source == " ")
                ""
            else
                source!!
        }
        editText.filters = arrayOf(filter)
    }
    //==========图片选择===========//

    //==========图片选择===========//
    /**
     * 获取图片选择的配置
     *
     * @param fragment
     * @return
     */
    fun getPictureSelector(fragment: Fragment?): PictureSelectionModel {
        return PictureSelector.create(fragment)
            .openGallery(PictureMimeType.ofImage())
            .maxSelectNum(8)
            .minSelectNum(1)
            .selectionMode(PictureConfig.MULTIPLE)
            .previewImage(true)
            .isCamera(true)
            .enableCrop(false)
            .compress(true)
            .previewEggs(true)
    }

    fun getPictureSelector(activity: Activity?): PictureSelectionModel {
        return PictureSelector.create(activity)
            .openGallery(PictureMimeType.ofImage())
            .maxSelectNum(8)
            .minSelectNum(1)
            .selectionMode(PictureConfig.MULTIPLE)
            .previewImage(true)
            .isCamera(true)
            .enableCrop(false)
            .compress(true)
            .previewEggs(true)
    }


}