package cn.entergx.yztx.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import cn.entergx.yztx.R
import cn.entergx.yztx.activity.LoginActivity
import cn.entergx.yztx.bean.bean.User
import cn.entergx.yztx.constant.Identity
import cn.entergx.yztx.constant.Sex
import cn.entergx.yztx.constant.StatusType
import cn.entergx.yztx.msg.Msg
import cn.entergx.yztx.network.Repo
import cn.entergx.yztx.utils.MyCallback
import cn.entergx.yztx.utils.SPUtils
import cn.entergx.yztx.utils.Utils
import com.bumptech.glide.Glide
import com.rishabhharit.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.fragment_mine.*
import retrofit2.Call
import retrofit2.Response

class MineFragment : Fragment() {
    private lateinit var tvUNme: TextView
    private lateinit var tvUStatus: TextView
    private lateinit var tvUDes: TextView
    private lateinit var riUAvatar: RoundedImageView
    private lateinit var tvUSub: TextView
    private lateinit var tvUFollow: TextView
    private lateinit var tvUCoin: TextView
    private lateinit var btLogout: Button
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        initViewData()
    }

    @SuppressLint("SetTextI18n")
    private fun initViewData() {
        if (SPUtils.getIsLogin()) {
            btLogout.text = "注销"
            btLogout.setOnClickListener {
                SPUtils.saveIsLogin(false)
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                ActivityCompat.finishAffinity(requireActivity())
            }
            user = SPUtils.getUser()
            initUserInfo()
            refresh()
        } else {
            btLogout.text = "登录"
            btLogout.setOnClickListener { startActivity(Intent(context,LoginActivity::class.java)) }
            var status = ""
            val identity = SPUtils.getInitType()
            if (identity == Identity.YUN_ZHONG) {
                status += "怀孕中：" + Utils.transToString(SPUtils.getDate())
            } else if (identity == Identity.HAVE_BABY) {
                status += if (SPUtils.getBabySex() == Sex.MALE) {
                    "小王子：" + Utils.transToString(SPUtils.getDate())
                } else {
                    "小公主：" + Utils.transToString(SPUtils.getDate())
                }
            }
            tvUStatus.text = status
        }
    }

    private fun refresh() {
        Repo.getUserInfo(user.userId, object : MyCallback<Msg<User>> {
            override fun onResponse(call: Call<Msg<User>>, response: Response<Msg<User>>) {
                if (response.body()?.status == StatusType.SUCCESSFUL) {
                    user = response.body()!!.content
                    SPUtils.saveUser(user)
                    initUserInfo()
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun initUserInfo() {
        tvUNme.text = user.user_name
        tvUSub.text = "关注：${user.subscription_count}"
        tvUFollow.text = "粉丝：${user.fan_count}"
        tvUCoin.text = "积分：${user.wallet}"
        tvUDes.text = user.des
        var status = ""
        if (user.identity == Identity.YUN_ZHONG) {
            status += "怀孕中：" + Utils.transToString(user.date)
        } else if (user.identity == Identity.HAVE_BABY) {
            status += if (SPUtils.getBabySex() == Sex.MALE) {
                "小王子：" + Utils.transToString(user.date)
            } else {
                "小公主：" + Utils.transToString(user.date)
            }
        }
        tvUStatus.text = status
        Glide.with(requireContext()).load(user.avatar_url).placeholder(R.drawable.ic_gank)
            .error(R.drawable.ic_error).into(riUAvatar)
    }

    private fun initViews() {
        tvUNme = tv_mine_user_name
        tvUSub = tv_mine_subscribe
        tvUFollow = tv_mine_follower
        tvUCoin = tv_mine_coin
        riUAvatar = ri_mine_avatar
        tvUDes = tv_mine_des
        btLogout = bt_mine_logout
        tvUStatus = tv_mine_status
    }
}