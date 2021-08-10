package cn.entergx.yztx.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import cn.entergx.yztx.R
import kotlinx.android.synthetic.main.activity_choose.*

class ChooseActivity : AppCompatActivity() {
    private lateinit var cvYZ:CardView
    private lateinit var cvBB:CardView
    private lateinit var tvLogin:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)
        initViews()
        initListeners()
    }

    private fun initListeners() {
        cvYZ.setOnClickListener { startActivity(Intent(this,YunZhongActivity::class.java)) }
        cvBB.setOnClickListener { startActivity(Intent(this,BabyActivity::class.java)) }
        tvLogin.setOnClickListener { startActivity(Intent(this,LoginActivity::class.java)) }
    }

    private fun initViews() {
        cvYZ = cv_choose_yunzhong
        cvBB = cv_choose_baobao
        tvLogin = tv_choose_login
    }
}