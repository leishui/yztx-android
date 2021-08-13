package cn.entergx.yztx.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.entergx.yztx.R
import cn.entergx.yztx.bean.bean.User
import cn.entergx.yztx.constant.StatusType
import cn.entergx.yztx.msg.Msg
import cn.entergx.yztx.msg.SimpleMsg
import cn.entergx.yztx.network.Repo
import cn.entergx.yztx.utils.MyCallback
import cn.entergx.yztx.utils.SPUtils
import cn.entergx.yztx.utils.Utils
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var etPhone: EditText
    private lateinit var etPassword: EditText
    private lateinit var btLogin: Button
    private lateinit var ibClose: ImageButton
    private lateinit var tvRegister: TextView
    private lateinit var tvForgetPassword: TextView

    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init() {
        initViews()
        initControllers()
    }

    private fun initControllers() {
        btLogin.setOnClickListener {
            when {
                etPhone.text.length != 11 -> Utils.toast(this, "请输入11位正确手机号")
                etPassword.text.isBlank() -> Utils.toast(this, "密码未填写")
                else -> {
                    alertDialog.show()
                    Repo.loginPhone(etPhone.text.toString().toLong(),
                        etPassword.text.toString(), object : MyCallback<Msg<User>> {
                            override fun onFailure(call: Call<Msg<User>>, t: Throwable) {
                                super.onFailure(call, t)
                                Utils.toast(this@LoginActivity, "登录失败")
                                alertDialog.dismiss()
                            }

                            override fun onResponse(
                                call: Call<Msg<User>>,
                                response: Response<Msg<User>>
                            ) {
                                Log.d("TAG", "onResponse: " + response.body()?.msg.toString())
                                if (response.body()?.status == StatusType.SUCCESSFUL) {
                                    Utils.toast(this@LoginActivity, "登录成功")
                                    SPUtils.saveIsLogin(true)
                                    SPUtils.saveUser(response.body()!!.content)
                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            MainActivity::class.java
                                        )
                                    )
                                    finish()
                                } else {
                                    Utils.toast(
                                        this@LoginActivity,
                                        "登录失败：" + response.body()?.msg.toString()
                                    )
                                }
                                alertDialog.dismiss()
                            }
                        })
                }
            }

        }
        tvForgetPassword.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    RetrievePasswordActivity::class.java
                )
            )
        }
        tvRegister.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
        ibClose.setOnClickListener { finish() }
    }

    private fun initViews() {
        alertDialog = AlertDialog.Builder(this).setMessage("登录中...").setCancelable(false).create()
        etPhone = activity_login_et_phone
        etPassword = activity_login_et_password
        btLogin = activity_login_bt_login
        ibClose = activity_login_ib_close
        tvRegister = activity_login_tv_register
        tvForgetPassword = activity_login_tv_forget_password
        //不允许输入空格
        Utils.setEditTextInhibitInputSpace(etPassword)
    }
}