package cn.entergx.yztx.bean.page

import java.io.Serializable

data class SpringPage<T>(
    val content: ArrayList<T> = arrayListOf(),
    val empty: Boolean = false,
    val first: Boolean = false,
    val last: Boolean = false,
    val number: Int = 0,
    val numberOfElements: Int = 0,
    val pageable: Pageable = Pageable(),
    val size: Int = 0,
    val sort: Sort = Sort(),
    val totalElements: Int = 0,
    val totalPages: Int = 0
):Serializable