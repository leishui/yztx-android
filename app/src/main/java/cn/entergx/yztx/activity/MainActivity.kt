package cn.entergx.yztx.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import cn.entergx.yztx.R
import cn.entergx.yztx.constant.MainConstValue
import cn.entergx.yztx.fragment.CommunityFragment
import cn.entergx.yztx.fragment.LessonFragment
import cn.entergx.yztx.fragment.MainFragment
import cn.entergx.yztx.fragment.MineFragment
import cn.entergx.yztx.model.UserModel
import cn.entergx.yztx.msg.SimpleMsg
import cn.entergx.yztx.network.Repo
import cn.entergx.yztx.utils.MyCallback
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(),MainConstValue{
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    //用于存放Fragment的，为了初始化ViewPager
    private val fragmentList = ArrayList<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    //初始化一些东西，例如找view，并调用其他init方法
    private fun init() {
        viewPager = vp_main
        viewPager.offscreenPageLimit = 3
        tabLayout = tb_layout_main
        initViewPager()
        initTabLayout()
    }

    //初始化ViewPager
    private fun initViewPager() {
        //往fragmentList里添加各个Fragment，下方顺序不能乱
        //添加主Fragment
        fragmentList.add(MainFragment())
        //添加课程Fragment
        fragmentList.add(LessonFragment())
        //添加社区Fragment
        fragmentList.add(CommunityFragment())
        //添加我的Fragment
        fragmentList.add(MineFragment())
        viewPager.adapter = object : FragmentPagerAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getItem(position: Int): Fragment {
                return fragmentList[position]
            }

            override fun getCount(): Int {
                return fragmentList.size
            }
        }
        //默认显示的是第一个找团队界面
        viewPager.currentItem = 0
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                //改变TabLayout图标
                tabLayout.getTabAt(position)?.select()
            }
        })
    }

    //初始化TabLayout的内容
    private fun initTabLayout() {
        //为TabLayout添加内容
        for (i: Int in tabText.indices) {
            if (i == 0) {
                tabLayout.addTab(
                    tabLayout.newTab().setText(tabText[0]).setIcon(tabSelectedDrawableIdList[0])
                )
            } else {
                tabLayout.addTab(
                    tabLayout.newTab().setText(tabText[i]).setIcon(tabUnselectedDrawableList[i])
                )
            }
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            //设置未选中图标
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.apply {
                    setIcon(tabUnselectedDrawableList[position])
                }
            }

            //设置选中图标
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.apply {
                    setIcon(tabSelectedDrawableIdList[position])
                    viewPager.currentItem = position
                }
            }
        })
    }
}