package com.example.klafka.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.klafka.ui.home.ItemCardSlide1Fragment
import com.example.klafka.ui.home.ItemCardSlide2Fragment
import com.example.klafka.ui.home.ItemCardSlide3Fragment

class SliderAdapter(fragement : Fragment) : FragmentStateAdapter(fragement){
    override fun getItemCount(): Int = 3
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ItemCardSlide1Fragment()
            1 -> ItemCardSlide2Fragment()
            2 -> ItemCardSlide3Fragment()
            else -> ItemCardSlide1Fragment()
    }
    }
}