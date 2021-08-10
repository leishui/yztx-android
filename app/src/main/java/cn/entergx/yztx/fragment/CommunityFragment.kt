package cn.entergx.yztx.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cn.entergx.yztx.R
import cn.entergx.yztx.adapter.CommunityAdapter
import kotlinx.android.synthetic.main.fragment_community.*

class CommunityFragment : Fragment() {

    private lateinit var adapter: CommunityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CommunityAdapter(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_community, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_community.adapter = adapter.also {
            it.setBannerList(
            arrayListOf(
                R.drawable.ic_bv_1,
                R.drawable.ic_bv_2,
                R.drawable.ic_bv_3,
                R.drawable.ic_bv_4
            )
        ) }
        rv_community.layoutManager = LinearLayoutManager(requireContext())
    }

}