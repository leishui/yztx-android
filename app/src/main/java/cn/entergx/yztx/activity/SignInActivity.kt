package cn.entergx.yztx.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import cn.entergx.yztx.CodeCountDownTimer
import cn.entergx.yztx.R
import cn.entergx.yztx.constant.StatusType
import cn.entergx.yztx.msg.SimpleMsg
import cn.entergx.yztx.network.Repo
import cn.entergx.yztx.utils.MyCallback
import cn.entergx.yztx.utils.SPUtils
import cn.entergx.yztx.utils.Utils
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Response

class SignInActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var countDownTimer: CodeCountDownTimer
    private lateinit var etPhone: EditText
    private lateinit var etVerification: EditText
    private lateinit var etPassword: EditText
    private lateinit var etRepeatPassword: EditText
    private lateinit var ibClose: ImageButton
    private lateinit var tvLogin: TextView
    private lateinit var btRegister: Button
    private lateinit var btGetCode: Button

    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        init()
    }

    private fun init() {
        initViews()
        countDownTimer = CodeCountDownTimer(btGetCode)
        initListener()
    }

    private fun initViews() {
        initAlertDialog()
        tvLogin = activity_register_tv_login
        etPhone = activity_register_et_phone
        etVerification = activity_register_et_verification
        etPassword = activity_register_et_password
        etRepeatPassword = activity_register_et_repeat_password
        ibClose = activity_register_ib_close
        btRegister = activity_register_bt_register
        btGetCode = activity_register_bt_get_verification_code
        //不允许输入空格
        Utils.setEditTextInhibitInputSpace(etPassword)
        Utils.setEditTextInhibitInputSpace(etRepeatPassword)
    }

    private fun initAlertDialog() {
        alertDialog = AlertDialog.Builder(this).setMessage("正在注册...").setCancelable(false).create()
    }

    private fun initListener() {
        btRegister.setOnClickListener(this)
        tvLogin.setOnClickListener(this)
        ibClose.setOnClickListener(this)
        btGetCode.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            btRegister.id -> {
                if (
                    etPhone.text.isNotBlank() &&
                    etVerification.text.isNotBlank() &&
                    etPassword.text.isNotBlank() &&
                    etRepeatPassword.text.isNotBlank()
                ) {
                    if (etPassword.text.toString() == etRepeatPassword.text.toString()) {
                        alertDialog.show()
                        Repo.signIn(
                            etPhone.text.toString().toLong(),
                            etPassword.text.toString(),
                            etVerification.text.toString().toInt(),
                            object : MyCallback<SimpleMsg> {
                                override fun onResponse(
                                    call: Call<SimpleMsg>,
                                    response: Response<SimpleMsg>
                                ) {
                                    if (response.body()?.status == StatusType.SUCCESSFUL) {
                                        Utils.toast(
                                            this@SignInActivity,
                                            "注册成功：" + response.body()?.msg.toString()
                                        )
                                        alertDialog.dismiss()
                                        SPUtils.saveIsLogin(this@SignInActivity,true)
                                        startActivity(
                                            Intent(
                                                this@SignInActivity,
                                                MainActivity::class.java
                                            ).apply {
                                                flags =
                                                    Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_NEW_TASK
                                            })
                                        ActivityCompat.finishAffinity(this@SignInActivity)
                                    } else {
                                        Utils.toast(
                                            this@SignInActivity,
                                            "注册失败：" + response.body()?.msg.toString()
                                        )
                                        alertDialog.dismiss()
                                    }
                                }
                            })
                    } else
                        Utils.toast(this, "密码不一致")
                } else if (etPhone.text.length != 11)
                    Utils.toast(this, "请输入正确的11位手机号")
                else
                    Utils.toast(this, "请将信息填写完整")
            }
            tvLogin.id -> finish()
            ibClose.id -> finish()
            btGetCode.id -> {
                if (etPhone.text.length != 11)
                    Utils.toast(this, "请输入正确的11位手机号")
                else {
                    countDownTimer.start()
                    Repo.getCode(etPhone.text.toString().toLong(), object : MyCallback<SimpleMsg> {
                        override fun onResponse(
                            call: Call<SimpleMsg>,
                            response: Response<SimpleMsg>
                        ) {
                            if (response.body()?.status == StatusType.SUCCESSFUL) {
                                Utils.toast(
                                    this@SignInActivity,
                                    "获取成功：" + response.body()?.msg.toString()
                                )
                            } else {
                                Utils.toast(
                                    this@SignInActivity,
                                    "获取失败：" + response.body()?.msg.toString()
                                )
                                countDownTimer.cancel()
                                countDownTimer.onFinish()
                            }
                        }

                        override fun onFailure(call: Call<SimpleMsg>, t: Throwable) {
                            super.onFailure(call, t)
                            Utils.toast(this@SignInActivity, "获取失败：网络请求错误")
                            countDownTimer.cancel()
                            countDownTimer.onFinish()
                        }
                    })
                }
            }
        }
    }
}