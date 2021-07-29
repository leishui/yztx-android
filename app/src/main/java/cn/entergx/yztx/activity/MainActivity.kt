package cn.entergx.yztx.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cn.entergx.yztx.R
import cn.entergx.yztx.model.UserModel
import cn.entergx.yztx.msg.SimpleMsg
import cn.entergx.yztx.network.Repo
import cn.entergx.yztx.utils.MyCallback
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bt_login.setOnClickListener {
            Repo.getCode(editTextPhone.text.toString().toLong(), object :MyCallback<SimpleMsg>{
                override fun onResponse(call: Call<SimpleMsg>, response: Response<SimpleMsg>) {
                    println("1111111" + response.body()?.msg)
                    Toast.makeText(
                        this@MainActivity,
                        response.body()?.msg.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}