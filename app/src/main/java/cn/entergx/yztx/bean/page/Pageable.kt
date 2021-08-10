package cn.entergx.yztx.bean.page

import java.io.Serializable

data class Pageable(
    val offset: Int = 0,
    val pageNumber: Int = 0,
    val pageSize: Int = 0,
    val paged: Boolean = false,
    val sort: Sort = Sort(),
    val unpaged: Boolean = false
):Serializable