package cn.entergx.yztx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cn.entergx.yztx.model.UserModel
import cn.entergx.yztx.msg.SimpleMsg
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener { UserModel.getCode(editTextPhone.text.toString().toLong(),object :Callback<SimpleMsg>{
            override fun onFailure(call: Call<SimpleMsg>, t: Throwable) {
                println("1111111"+t.message)
                Toast.makeText(this@MainActivity,t.message,Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<SimpleMsg>, response: Response<SimpleMsg>) {
                println("1111111"+response.body()?.msg)
                Toast.makeText(this@MainActivity,response.body()?.msg.toString(),Toast.LENGTH_SHORT).show()
            }
        }) }
    }
}