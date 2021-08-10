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
import cn.entergx.yztx.bean.bean.Lesson
import cn.entergx.yztx.utils.Utils
import cn.entergx.yztx.widget.BannerView
import com.bumptech.glide.Glide

class MainAdapter(private val mContext: Context) : RecyclerView.Adapter<MainAdapter.Holder>() {
    private lateinit var mBannerList: ArrayList<Int>
    private lateinit var mVideoList: ArrayList<Lesson>
    private lateinit var mBannerClickListener: OnBannerItemClickListener
    private lateinit var mRecyclerClickListener: OnRecyclerItemClickListener
    private val bvHeight = (Utils.getScreenWidth(mContext) - Utils.dip2px(mContext, 30f)) / 2.5
    private lateinit var bvParams: ViewGroup.LayoutParams

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bv: BannerView? = itemView.findViewById(R.id.item_team_bv_ad)
        var iv1: ImageView? = itemView.findViewById(R.id.iv_cover_main_1)
        var iv2: ImageView? = itemView.findViewById(R.id.iv_cover_main_2)
        var tvCount1: TextView? = itemView.findViewById(R.id.tv_view_count_main_1)
        var tvCount2: TextView? = itemView.findViewById(R.id.tv_view_count_main_2)
        var tvLength1: TextView? = itemView.findViewById(R.id.tv_length_main_1)
        var tvLength2: TextView? = itemView.findViewById(R.id.tv_length_main_2)
        var tvTitle1: TextView? = itemView.findViewById(R.id.tv_title_main_1)
        var tvTitle2: TextView? = itemView.findViewById(R.id.tv_title_main_2)
        var cvVideo1: CardView? = itemView.findViewById(R.id.cv_video_main_1)
        var cvVideo2: CardView? = itemView.findViewById(R.id.cv_video_main_2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = if (viewType == 1)
            LayoutInflater.from(mContext).inflate(R.layout.item_ad, parent, false)
        else
            LayoutInflater.from(mContext).inflate(R.layout.item_main, parent, false)
        return Holder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 1 else 0
    }

    override fun getItemCount(): Int {
        return mVideoList.size / 2 + 1
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
            val lesson1 = mVideoList[position * 2 - 2]
            val lesson2 = mVideoList[position * 2 - 1]
            holder.tvCount1?.text = lesson1.view_count.toString()
            holder.tvCount2?.text = lesson2.view_count.toString()
            Glide.with(holder.itemView).load(lesson1.cover_url).placeholder(R.drawable.ic_gank)
                .error(R.drawable.ic_error).into(holder.iv1!!)
            Glide.with(holder.itemView).load(lesson2.cover_url).placeholder(R.drawable.ic_gank)
                .error(R.drawable.ic_error).into(holder.iv2!!)
            holder.tvTitle1?.text = lesson1.name
            holder.tvTitle2?.text = lesson2.name
            holder.cvVideo1!!.setOnClickListener {
                mRecyclerClickListener.onClick(
                    lesson1
                )
            }
            holder.cvVideo2!!.setOnClickListener {
                mRecyclerClickListener.onClick(
                    lesson2
                )
            }
        }
    }

    fun setBannerList(list: ArrayList<Int>) {
        mBannerList = list
    }

    fun setVideoList(list: ArrayList<Lesson>) {
        mVideoList = list
    }

    interface OnBannerItemClickListener {
        fun onClick(pos: Int)
    }

    interface OnRecyclerItemClickListener {
        fun onClick(
            lesson: Lesson
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