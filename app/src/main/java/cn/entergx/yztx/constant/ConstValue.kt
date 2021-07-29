package cn.entergx.yztx.constant

import cn.entergx.yztx.R

interface MainConstValue {
    val tabSelectedDrawableIdList: Array<Int>
        get() = arrayOf(
            R.drawable.ic_home_1,
            R.drawable.ic_lesson_1,
            //因为现在还没图标，所以先代替一下
            R.drawable.ic_community_1,
            R.drawable.ic_mine_1
        )

    val tabUnselectedDrawableList: Array<Int>
        get() = arrayOf(
            R.drawable.ic_home,
            R.drawable.ic_lesson,
            //因为现在还没图标，所以先代替一下
            R.drawable.ic_community,
            R.drawable.ic_mine
        )

    val tabText: Array<String> get() = arrayOf("主页", "课程", "社区", "我的")
}