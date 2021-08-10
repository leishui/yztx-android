package cn.entergx.yztx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.entergx.yztx.R
import cn.entergx.yztx.bean.bean.Lesson
import cn.entergx.yztx.bean.bean.LessonSet
import cn.entergx.yztx.utils.Utils
import com.bumptech.glide.Glide
import com.rishabhharit.roundedimageview.RoundedImageView


class CatalogueAdapter(private val mContext: Context, private val lessonSet: LessonSet) :
    RecyclerView.Adapter<CatalogueAdapter.CatalogueViewHolder>() {

    private lateinit var listener:OnCatalogueItemClick

    inner class CatalogueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView? = itemView.findViewById(R.id.tv_title_catalogue)
        val tvId: TextView? = itemView.findViewById(R.id.tv_id_catalogue)
        val tvTime: TextView? = itemView.findViewById(R.id.tv_time_catalogue)
        val llRoot: LinearLayout? = itemView.findViewById(R.id.ll_root_catalogue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogueViewHolder {
        return CatalogueViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.item_catalogue, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return lessonSet.lessons.size
    }

    override fun onBindViewHolder(holder: CatalogueViewHolder, position: Int) {
        holder.tvId?.text = (position + 1).toString()
        holder.tvTitle?.text = lessonSet.lessons[position].name
        holder.tvTime?.text = Utils.transToString(lessonSet.lessons[position].upload_time)
        holder.llRoot?.setOnClickListener { listener.onClick(position) }
    }
    fun setCatalogueItemClick(listener:OnCatalogueItemClick){
        this.listener = listener
    }
    interface OnCatalogueItemClick{
        fun onClick(position: Int)
    }
}