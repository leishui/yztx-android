package cn.entergx.yztx.bean.page

import java.io.Serializable

data class SortX(
    val empty: Boolean = false,
    val sorted: Boolean = false,
    val unsorted: Boolean = false
): Serializable