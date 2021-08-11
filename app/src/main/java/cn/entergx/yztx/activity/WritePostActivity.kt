package cn.entergx.yztx.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.entergx.yztx.R
import cn.entergx.yztx.adapter.ImageSelectGridAdapter
import cn.entergx.yztx.bean.bean.Post
import cn.entergx.yztx.constant.SourceType
import cn.entergx.yztx.constant.StatusType
import cn.entergx.yztx.msg.Msg
import cn.entergx.yztx.msg.SimpleMsg
import cn.entergx.yztx.network.Repo
import cn.entergx.yztx.utils.SPUtils
import cn.entergx.yztx.utils.Utils
import com.google.gson.Gson
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import com.xuexiang.xui.widget.edittext.MultiLineEditText
import kotlinx.android.synthetic.main.activity_write_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlin.collections.ArrayList

class WritePostActivity : AppCompatActivity(), ImageSelectGridAdapter.OnAddPicClickListener {
    private var mAdapter: ImageSelectGridAdapter? = null
    private var mSelectList: List<LocalMedia> = ArrayList()
    private lateinit var meTitle: MultiLineEditText
    private lateinit var meContent: MultiLineEditText
    private lateinit var tvSend: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var alertDialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_post)
        initViews()
    }

    private fun initViews() {
        meContent = me_wp_content
        meTitle = me_wp_title
        alertDialog = AlertDialog.Builder(this).setMessage("上传中...").setCancelable(false).create()
        tvSend = tv_wp_send
        tvSend.setOnClickListener {
            upload()
        }
        recyclerView = rv_wp
        val manager =
            GridLayoutManager(this, 4, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = manager
        recyclerView.adapter = ImageSelectGridAdapter(this, this).also { mAdapter = it }
        mAdapter!!.setSelectList(mSelectList)
        mAdapter!!.setSelectMax(8)
        mAdapter!!.setOnItemClickListener { position, v ->
            PictureSelector.create(this).themeStyle(R.style.XUIPictureStyle)
                .openExternalPreview(position, mSelectList)
        }
    }

    private fun upload() {
        if (meContent.isEmpty or meTitle.isEmpty) {
            Toast.makeText(this@WritePostActivity, "标题/内容均不能为空", Toast.LENGTH_SHORT).show()
            return
        }
        val userId = SPUtils.getUser().userId
        if (mSelectList.isEmpty()){
            alertDialog.show()
            uploadContent(userId,"")
            return
        }
        alertDialog.setMessage("上传图片中...")
        alertDialog.show()
        Repo.uploadFiles(getFiles(), userId, object : Callback<Msg<ArrayList<String>>> {
            override fun onFailure(call: Call<Msg<ArrayList<String>>>, t: Throwable) {
                Log.e("yztx", "onFailure: ", t)
                alertDialog.dismiss()
                Toast.makeText(this@WritePostActivity, "图片上传失败", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<Msg<ArrayList<String>>>,
                response: Response<Msg<ArrayList<String>>>
            ) {
                Log.e("yztx", "on: $response")
                alertDialog.setMessage("上传内容中")
                uploadContent(userId, Gson().toJson(response.body()!!.content))
            }
        })
    }

    private fun uploadContent(userId: Long, resources: String?) {
        Repo.uploadPost(
            meTitle.contentText,
            meContent.contentText,
            resources,
            userId,
            SourceType.SOURCE_ORIGINAL,
            1,
            object : Callback<SimpleMsg> {
                override fun onFailure(call: Call<SimpleMsg>, t: Throwable) {
                    alertDialog.dismiss()
                    Toast.makeText(this@WritePostActivity, "上传失败", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<SimpleMsg>, response: Response<SimpleMsg>) {
                    alertDialog.dismiss()
                    if (response.body()!!.status == StatusType.SUCCESSFUL) {
                        finish()
                        Toast.makeText(this@WritePostActivity, "上传成功", Toast.LENGTH_SHORT).show()
                    } else {
                        finish()
                        Toast.makeText(
                            this@WritePostActivity,
                            "上传失败：" + response.body()!!.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

    private fun getFiles(): java.util.ArrayList<File> {
        val files = ArrayList<File>()
        for (media in mSelectList) {
            files.add(File(media.path))
        }
        return files
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    // 图片选择
                    mSelectList = PictureSelector.obtainMultipleResult(data)
                    mAdapter!!.setSelectList(mSelectList)
                    mAdapter!!.notifyDataSetChanged()
                }
                else -> {
                }
            }
        }
    }

    override fun onAddPicClick() {
        Utils.getPictureSelector(this)
            .selectionMedia(mSelectList)
            .forResult(PictureConfig.CHOOSE_REQUEST)
    }
}