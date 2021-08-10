package cn.entergx.yztx.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.entergx.yztx.R
import cn.entergx.yztx.constant.Sex
import cn.entergx.yztx.utils.SPUtils
import cn.entergx.yztx.utils.Utils
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView
import com.xuexiang.xui.widget.picker.widget.TimePickerView
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder
import com.xuexiang.xui.widget.picker.widget.listener.OnOptionsSelectListener
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener
import kotlinx.android.synthetic.main.activity_baby.*
import kotlinx.android.synthetic.main.activity_yun_zhong.*


class BabyActivity : AppCompatActivity() {
    private val mSexOption: Array<String> = arrayOf("小王子","小公主")
    private lateinit var ivBack: ImageView
    private lateinit var tvBa: TextView
    private lateinit var tvMa: TextView
    private lateinit var tvBabySex:TextView
    private lateinit var tvDate: TextView
    private lateinit var pvOptions: OptionsPickerView<Any>
    private lateinit var btStart: Button
    private var sex = Sex.FEMALE
    private var babySex = Sex.MALE
    private lateinit var mDatePicker: TimePickerView
    private var date = System.currentTimeMillis() / 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baby)
        initViews()
        initPicker()
        initListeners()
    }
    private fun initListeners() {
        ivBack.setOnClickListener { finish() }
        tvBabySex.setOnClickListener { pvOptions.show() }
        tvDate.setOnClickListener { mDatePicker.show() }
        tvMa.setOnClickListener { choose(Sex.FEMALE) }
        tvBa.setOnClickListener { choose(Sex.MALE) }
        btStart.setOnClickListener { startMainActivity() }
    }

    private fun choose(sex: Int) {
        this.sex = sex
        if (sex == Sex.FEMALE){
            tvMa.background = resources.getDrawable(R.drawable.bg_bt_normal_r,null)
            tvBa.setTextColor(resources.getColor(R.color.gray_dark))
            tvBa.background = resources.getDrawable(R.drawable.bg_bt_call,null)
            tvMa.setTextColor(Color.WHITE)
        }else{
            tvBa.background = resources.getDrawable(R.drawable.bg_bt_normal_r,null)
            tvBa.setTextColor(Color.WHITE)
            tvMa.background = resources.getDrawable(R.drawable.bg_bt_call,null)
            tvMa.setTextColor(resources.getColor(R.color.gray_dark))
        }
    }

    private fun initViews() {
        ivBack = iv_bb_back
        tvBa = tv_bb_ba
        tvMa = tv_bb_ma
        tvDate = tv_bb_baby_date
        btStart = bt_bb_start
        tvBabySex = tv_bb_baby_sex
        tvDate.text = Utils.transToString(date)
    }

    private fun initPicker() {
        initSexPicker()
        initDatePicker()
    }

    private fun initDatePicker() {
        mDatePicker = TimePickerBuilder(this,
            OnTimeSelectListener { date, v ->
                tvDate.text = Utils.transToString(date.time / 1000)
                this.date = date.time / 1000
            })
            .setTitleText("选择宝宝出生日期")
            .build()
    }

    private fun initSexPicker() {
        pvOptions = OptionsPickerBuilder(this,
            OnOptionsSelectListener { _: View?, options1: Int, _: Int, _: Int ->
                tvBabySex.text = mSexOption[options1]
                babySex = options1
                //Toast.makeText(this, options1, Toast.LENGTH_SHORT).show()
                false
            }
        )
            .setTitleText("选择宝宝性别")
            .setSelectOptions(0)
            .build<Any>()
        pvOptions.setPicker(mSexOption)
    }

    private fun startMainActivity() {
        SPUtils.saveBb(sex,babySex, date)
        startActivity(Intent(this, SignInActivity::class.java).also {
            it.putExtra("sex", sex)
            it.putExtra("babySex",babySex)
            it.putExtra("date", date)
        })
    }
}