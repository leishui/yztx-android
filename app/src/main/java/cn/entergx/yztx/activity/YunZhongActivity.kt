package cn.entergx.yztx.activity

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import cn.entergx.yztx.R
import cn.entergx.yztx.constant.Identity
import cn.entergx.yztx.constant.Sex
import cn.entergx.yztx.utils.SPUtils
import cn.entergx.yztx.utils.Utils
import com.xuexiang.xui.widget.picker.widget.TimePickerView
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener
import kotlinx.android.synthetic.main.activity_yun_zhong.*
import java.util.*


class YunZhongActivity : AppCompatActivity() {
    private lateinit var ivBack: ImageView
    private lateinit var tvBa: TextView
    private lateinit var tvMa: TextView
    private lateinit var tvIntro: TextView
    private lateinit var tvDate: TextView
    private lateinit var btStart: Button
    private lateinit var mDatePicker: TimePickerView
    private var sex = Sex.FEMALE
    private var date = System.currentTimeMillis() / 1000 + Identity.DATE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yun_zhong)
        initViews()
        initPicker()
        initListeners()
    }

    private fun initListeners() {
        ivBack.setOnClickListener { finish() }
        tvDate.setOnClickListener { mDatePicker.show() }
        tvMa.setOnClickListener { choose(Sex.FEMALE) }
        tvBa.setOnClickListener { choose(Sex.MALE) }
        btStart.setOnClickListener { startMainActivity() }
    }

    private fun choose(sex: Int) {
        this.sex = sex
        if (sex == Sex.FEMALE) {
            tvMa.background = resources.getDrawable(R.drawable.bg_bt_normal_r, null)
            tvBa.setTextColor(resources.getColor(R.color.gray_dark))
            tvBa.background = resources.getDrawable(R.drawable.bg_bt_call, null)
            tvMa.setTextColor(Color.WHITE)
            tvIntro.text = "预产期"
        } else {
            tvBa.background = resources.getDrawable(R.drawable.bg_bt_normal_r, null)
            tvBa.setTextColor(Color.WHITE)
            tvMa.background = resources.getDrawable(R.drawable.bg_bt_call, null)
            tvMa.setTextColor(resources.getColor(R.color.gray_dark))
            tvIntro.text = "她的预产期"
        }
    }

    private fun initViews() {
        ivBack = iv_yz_back
        tvBa = tv_yz_ba
        tvMa = tv_yz_ma
        tvDate = tv_yz_date
        btStart = bt_yz_start
        tvIntro = tv_yz_intro
        tvDate.text = Utils.transToString(date)
    }

    private fun initPicker() {
        SPUtils.saveYz(sex, date)
        mDatePicker = TimePickerBuilder(this,
            OnTimeSelectListener { date, _ ->
                tvDate.text = Utils.transToString(date.time / 1000)
                this.date = date.time / 1000
            })
            .setTitleText("预产期选择")
            .setDate(Calendar.getInstance().also { it.time = Date(date * 1000) })
            .setRangDate(
                Calendar.getInstance().also { it.set(Utils.getYear().toInt(), 0, 1) },
                Calendar.getInstance().also { it.set(Utils.getYear().toInt() + 1, 11, 31) })
            .build()
    }

    private fun startMainActivity() {
        SPUtils.saveYz(sex, date)
        startActivity(Intent(this, SignInActivity::class.java).also {
            it.putExtra("sex", sex)
            it.putExtra("date", date)
        })
    }
}