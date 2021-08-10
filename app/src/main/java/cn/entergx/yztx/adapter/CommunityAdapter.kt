package cn.entergx.yztx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import cn.entergx.yztx.R
import cn.entergx.yztx.bean.bean.Post
import cn.entergx.yztx.utils.Utils
import cn.entergx.yztx.widget.BannerView
import com.bumptech.glide.Glide
import com.rishabhharit.roundedimageview.RoundedImageView

class CommunityAdapter(private val mContext: Context) : RecyclerView.Adapter<CommunityAdapter.Holder>() {
    private lateinit var mBannerList: ArrayList<Int>
    private lateinit var mPostList: ArrayList<Post>
    private lateinit var mBannerClickListener: OnBannerItemClickListener
    private lateinit var mRecyclerClickListener: OnRecyclerItemClickListener
    private val bvHeight = (Utils.getScreenWidth(mContext) - Utils.dip2px(mContext, 30f)) / 2.5
    private lateinit var bvParams: ViewGroup.LayoutParams

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bv: BannerView? = itemView.findViewById(R.id.item_team_bv_ad)
        var ivComment: ImageView? = itemView.findViewById(R.id.iv_community_comment)
        var ivZan: ImageView? = itemView.findViewById(R.id.iv_community_zan)
        var riAvatar: RoundedImageView? = itemView.findViewById(R.id.ri_avatar_community)
        var tvUpName: TextView? = itemView.findViewById(R.id.tv_community_up_name)
        var tvTitle: TextView? = itemView.findViewById(R.id.tv_community_post_title)
        var tvDes: TextView? = itemView.findViewById(R.id.tv_community_post_des)
        var tvTypeAndCount: TextView? = itemView.findViewById(R.id.tv_community_post_type_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = if (viewType == 1)
            LayoutInflater.from(mContext).inflate(R.layout.item_ad, parent, false)
        else
            LayoutInflater.from(mContext).inflate(R.layout.item_community_post, parent, false)
        return Holder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 1 else 0
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (position == 0) {
            holder.bv!!.apply {
                setOnBannerListener(object : BannerView.BannerClickListener {
                    override fun onBannerClick(position: Int) {
                        mBannerClickListener.onClick(position)
                    }
                })
                bvParams = this.layoutParams
                bvParams.height = bvHeight.toInt()
                layoutParams = bvParams
            }
            if (holder.bv!!.isListEmpty())
                holder.bv!!.setImage(mBannerList)
        } else {
        }
    }

    fun setBannerList(list: ArrayList<Int>) {
        mBannerList = list
    }

    fun setVideoList(list: ArrayList<Post>) {
        mPostList = list
    }

    interface OnBannerItemClickListener {
        fun onClick(pos: Int)
    }

    interface OnRecyclerItemClickListener {
        fun onClick(
            post: Post
        )
    }

    fun setOnItemClickListener(listener: OnBannerItemClickListener) {
        mBannerClickListener = listener
    }

    fun setOnItemClickListener(listener: OnRecyclerItemClickListener) {
        mRecyclerClickListener = listener
    }

    override fun onViewDetachedFromWindow(holder: Holder) {
        super.onViewDetachedFromWindow(holder)
        holder.bv?.stop()
    }

    override fun onViewAttachedToWindow(holder: Holder) {
        super.onViewAttachedToWindow(holder)
        holder.bv?.start()
    }
}