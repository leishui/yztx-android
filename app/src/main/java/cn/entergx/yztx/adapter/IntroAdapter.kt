package cn.entergx.yztx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.entergx.yztx.R
import cn.entergx.yztx.bean.bean.Lesson
import com.bumptech.glide.Glide
import com.rishabhharit.roundedimageview.RoundedImageView


class IntroAdapter(private val mContext: Context,private val lesson: Lesson): RecyclerView.Adapter<IntroAdapter.IntroViewHolder>() {

    private lateinit var tjList: ArrayList<Lesson>

    fun setTJList(tjList: ArrayList<Lesson>){
        this.tjList = tjList
    }
    inner class IntroViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val riAvatar: RoundedImageView? = itemView.findViewById(R.id.ri_avatar_video)
        val tvUpName: TextView? = itemView.findViewById(R.id.tv_up_name_video)
        val tvName: TextView? = itemView.findViewById(R.id.tv_name_video)
        val tvDes: TextView? = itemView.findViewById(R.id.tv_des_video)
        val tvUpDes: TextView? = itemView.findViewById(R.id.tv_up_time_comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroViewHolder {
        val view: View = if (viewType == 0)
            LayoutInflater.from(mContext).inflate(R.layout.item_intro, parent, false)
        else
            LayoutInflater.from(mContext).inflate(R.layout.item_tuijian, parent, false)
        return IntroViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: IntroViewHolder, position: Int) {
        if (position==0){
            holder.riAvatar?.let {
                Glide.with(holder.itemView).load(lesson.user?.avatar_url).placeholder(R.drawable.ic_gank)
                    .error(R.drawable.ic_error).into(it)
            }
            holder.tvName?.text = lesson.name
            holder.tvDes?.text = lesson.description
            holder.tvUpName?.text = lesson.user?.user_name
            return
        }
    }
}