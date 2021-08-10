package cn.entergx.yztx.msg

import cn.entergx.yztx.bean.bean.Lesson
import cn.entergx.yztx.bean.page.SpringPage

data class LessonMsg(
    val status:Int,
    val content: SpringPage<Lesson>
)