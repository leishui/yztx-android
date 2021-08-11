package cn.entergx.yztx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import cn.entergx.yztx.R
import cn.entergx.yztx.bean.bean.Comment
import cn.entergx.yztx.utils.Utils
import com.bumptech.glide.Glide
import com.rishabhharit.roundedimageview.RoundedImageView
import com.xuexiang.xui.widget.textview.ExpandableTextView


class CommentAdapter(private val mContext: Context, private val comments: ArrayList<Comment>) :
    RecyclerView.Adapter<CommentAdapter.IntroViewHolder>() {

    private lateinit var tjList: ArrayList<Comment>

    fun setTJList(tjList: ArrayList<Comment>) {
        this.tjList = tjList
    }

    inner class IntroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val riAvatar: RoundedImageView? = itemView.findViewById(R.id.ri_avatar_comment)
        val tvUpName: TextView? = itemView.findViewById(R.id.tv_up_name_comment)
        val tvTime: TextView? = itemView.findViewById(R.id.tv_up_time_comment)
        //val tvContent: TextView? = itemView.findViewById(R.id.tv_comment_content)
        val tvContent: ExpandableTextView? = itemView.findViewById(R.id.expand_text_view)
        val v: View? = itemView.findViewById(R.id.v_item_comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroViewHolder {
        val view: View = if (viewType == 0)
            LayoutInflater.from(mContext).inflate(R.layout.item_comment_top, parent, false)
        else
            LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false)
        return IntroViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    override fun getItemCount(): Int {
        return comments.size + 1
    }

    override fun onBindViewHolder(holder: IntroViewHolder, position: Int) {
        if (position != 0) {
            val comment = comments[position - 1]
            holder.riAvatar?.let {
                Glide.with(holder.itemView).load(comment.commentator_url)
                    .placeholder(R.drawable.ic_gank)
                    .error(R.drawable.ic_error).into(it)
            }
            holder.tvUpName?.text = comment.commentator_name
            holder.tvTime?.text = Utils.transToString(comment.comment_time)
            holder.tvContent?.text = comment.comment_content
            if (position == comments.size){
                val layoutParams = holder.v?.layoutParams as LinearLayout.LayoutParams
                layoutParams.setMargins(0,0,0,Utils.dip2px(mContext,50f))
                holder.v.layoutParams = layoutParams
            }
            return
        }
    }
}