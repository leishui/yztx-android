package cn.entergx.yztx.fragment.video

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cn.entergx.yztx.R
import cn.entergx.yztx.adapter.IntroAdapter
import cn.entergx.yztx.bean.bean.Lesson
import kotlinx.android.synthetic.main.fragment_intro.*

class IntroFragment(private val lesson: Lesson) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_intro.adapter = IntroAdapter(requireContext(),lesson)
        rv_intro.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }
}