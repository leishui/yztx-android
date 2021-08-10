package cn.entergx.yztx.fragment.video

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cn.entergx.yztx.R
import cn.entergx.yztx.adapter.CatalogueAdapter
import cn.entergx.yztx.bean.bean.LessonSet
import kotlinx.android.synthetic.main.fragment_catalogue.*

class CatalogueFragment(private val lessonSet: LessonSet) : Fragment() {
    private lateinit var listener:CatalogueAdapter.OnCatalogueItemClick

    fun setCatalogueItemClick(listener: CatalogueAdapter.OnCatalogueItemClick){
        this.listener = listener
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvCatalogue = rv_catalogue
        val catalogueAdapter = CatalogueAdapter(requireContext(), lessonSet)
        catalogueAdapter.setCatalogueItemClick(object :CatalogueAdapter.OnCatalogueItemClick{
            override fun onClick(position: Int) {
                listener.onClick(position)
            }
        })
        rvCatalogue.adapter = catalogueAdapter
        rvCatalogue.layoutManager = LinearLayoutManager(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalogue, container, false)
    }
}