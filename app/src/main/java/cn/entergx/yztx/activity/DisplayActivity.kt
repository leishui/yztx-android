package cn.entergx.yztx.activity

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.webkit.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import cn.entergx.yztx.R
import kotlinx.android.synthetic.main.activity_display.*

class DisplayActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)
        val url = this.intent.getStringExtra("url")
        val title=this.intent.getStringExtra("title")
        webview.loadUrl(url)
        //webview相关设置
        /**
         * 由于应用中的WebView没有对file:///类型的url做限制，可能导致外部攻击者利用Javascript代码读取本地隐私数据。
         * 如果WebView不需要使用file协议，直接禁用所有与file协议相关的功能即可（不影响对assets和resources资源的加载）。
         */
        webview.settings.allowFileAccess = false
        webview.settings.allowFileAccessFromFileURLs = false
        webview.settings.allowUniversalAccessFromFileURLs = false

        webview.settings.javaScriptEnabled = true
        webview.settings.javaScriptCanOpenWindowsAutomatically = true
        webview.settings.loadsImagesAutomatically = true
        //允许缩放
        webview.settings.builtInZoomControls = true
        webview.settings.displayZoomControls = false

        webview.settings.useWideViewPort = true
        webview.settings.loadWithOverviewMode =true

        //加载进度监听
        webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                myProgressBar.progress = newProgress
            }
        }
        //下载事件监听
        webview.setDownloadListener { p0, _, _, _, _ ->
            val uri = Uri.parse(p0)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        //在webview内继续加载 而不是跳到浏览器
        webview.webViewClient = WebViewClient()

        setSupportActionBar(display_toolbar)
        display_title.text = title
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.menu_webview, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_menu_fresh -> webview.reload()
            R.id.item_menu_browser ->{
                val uri = Uri.parse(webview.url)
                startActivity(Intent(Intent.ACTION_VIEW,uri))
            }
            R.id.item_menu_copy ->{
                val cm = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val mClipData = ClipData.newPlainText("Label", webview.url)
                cm.setPrimaryClip(mClipData)
                Toast.makeText(this, "复制链接成功", Toast.LENGTH_SHORT).show()
            }
            android.R.id.home->finish()
        }
        return true
    }

    override fun onBackPressed() {
        if (webview.canGoBack())
            webview.goBack()
        else
            super.onBackPressed()
    }
}
