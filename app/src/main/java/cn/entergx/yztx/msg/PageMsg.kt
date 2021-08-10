package cn.entergx.yztx.msg

import cn.entergx.yztx.bean.page.SpringPage

data class PageMsg<T>(
    val status:Int,
    val content: SpringPage<T>
)