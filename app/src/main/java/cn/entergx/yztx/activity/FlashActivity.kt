package cn.entergx.yztx.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cn.entergx.yztx.R
import cn.entergx.yztx.utils.SPUtils
import kotlin.concurrent.thread

class FlashActivity : AppCompatActivity() {
    private val TAG = "FlashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash)
        init()
    }

    private fun init() {
        thread(start = true) {
            Thread.sleep(2000)
            if (SPUtils.getIsLogin(this))
                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    )
                )
            else
                startActivity(Intent(this, LoginActivity::class.java))
            Log.d(TAG, "init: "+SPUtils.getIsLogin(this))
            finish()
        }
    }
}
