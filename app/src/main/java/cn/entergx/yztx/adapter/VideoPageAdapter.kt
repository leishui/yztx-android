package cn.entergx.yztx.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class VideoPageAdapter(private var context: Context, fm: FragmentManager, private val fragmentT: Fragment, private val fragmentF: Fragment) : FragmentPagerAdapter(
    fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return fragmentT
            1 -> return fragmentF
        }
        return fragmentT
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0->return "简介"
            1->return "评论"
        }
        return null
    }


}