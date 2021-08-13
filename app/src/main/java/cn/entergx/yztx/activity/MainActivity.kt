package cn.entergx.yztx.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import cn.entergx.yztx.R
import cn.entergx.yztx.constant.MainConstValue
import cn.entergx.yztx.fragment.*
import cn.entergx.yztx.fragment.MainFragmentVLayout
import com.jpeng.jptabbar.JPTabBar
import com.jpeng.jptabbar.anno.NorIcons
import com.jpeng.jptabbar.anno.SeleIcons
import com.jpeng.jptabbar.anno.Titles
import com.xuexiang.xui.XUI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainConstValue {

    //==============需要注意的是，由于JPTabBar反射获取注解的是context，也就是容器Activity，因此需要将注解写在容器Activity内======================//
    @Titles
    val mTitles =
        intArrayOf(R.string.tab1, R.string.tab2, R.string.tab3, R.string.tab4)

    @SeleIcons
    val mSelectIcons = intArrayOf(
        R.drawable.ic_home_1,
        R.drawable.ic_lesson_1,
        //因为现在还没图标，所以先代替一下
        R.drawable.ic_community_1,
        R.drawable.ic_mine_1
    )

    @NorIcons
    val mNormalIcons = intArrayOf(
        R.drawable.ic_home,
        R.drawable.ic_lesson,
        //因为现在还没图标，所以先代替一下
        R.drawable.ic_community,
        R.drawable.ic_mine
    )
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: JPTabBar

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
        fragmentList.add(MainFragmentVLayout())
        //添加课程Fragment
        fragmentList.add(LessonFragmentVLayout())
        //添加社区Fragment
        fragmentList.add(CommunityFragmentVLayout())
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
    }

    //初始化TabLayout的内容
    private fun initTabLayout() {
        //页面可以滑动
        tabLayout.setGradientEnable(true)
        tabLayout.setPageAnimateEnable(true)
        tabLayout.setTabTypeFace(XUI.getDefaultTypeface())
        tabLayout.setContainer(viewPager)
        if (tabLayout.middleView != null) {
            tabLayout.middleView
                .setOnClickListener {
                    startActivity(Intent(this,WritePostActivity::class.java))
                }
        }
        //tabLayout.showBadge(2, "", true)
    }
}