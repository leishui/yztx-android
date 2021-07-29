package cn.entergx.yztx

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.CountDownTimer
import android.widget.Button

class CodeCountDownTimer(private val button: Button) :
    CountDownTimer(60000, 1000) {
    override fun onFinish() {
        button.apply {
            isClickable = true
            setBackgroundResource(R.drawable.bg_bt_get_verification_code)
            text = "重新获取"
            setTextColor(Color.parseColor("#EC808D"))
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onTick(millisUntilFinished: Long) {
        button.apply {
            isClickable = false
            setBackgroundResource(R.drawable.bg_bt_unclickable)
            setTextColor(Color.parseColor("#cccccc"))
            text = "${millisUntilFinished/1000}s"
        }
    }
}