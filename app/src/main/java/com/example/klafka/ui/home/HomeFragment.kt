
package com.example.klafka.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.graphics.Rect
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.klafka.R
import com.example.klafka.databinding.FragmentHomeBinding
import com.example.klafka.model.JenisKain
import com.example.klafka.repository.JenisKainRepository
import com.example.klafka.adapter.JenisKainAdapter
import com.example.klafka.viewmodel.JenisKainViewModel
import com.example.klafka.adapter.SliderAdapter

class HomeFragment : Fragment(), JenisKainAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: JenisKainViewModel
    private lateinit var sliderHandler: Handler
    private lateinit var sliderRunnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[JenisKainViewModel::class.java]

        // Setup ViewPager2 (slider)
        binding.viewPager.adapter = SliderAdapter(this)
        val space = resources.getDimensionPixelSize(R.dimen.slider_gap)
        binding.viewPager.addItemDecoration(SliderItemDecoration(space))

        sliderHandler = Handler(Looper.getMainLooper())
        sliderRunnable = object : Runnable {
            override fun run() {
                val itemCount = binding.viewPager.adapter?.itemCount ?: 0
                if (itemCount > 0) {
                    val nextItem = (binding.viewPager.currentItem + 1) % itemCount
                    binding.viewPager.setCurrentItem(nextItem, true)
                    sliderHandler.postDelayed(this, 10000)
                }
            }
        }
        sliderHandler.postDelayed(sliderRunnable, 1000)

        // Setup RecyclerView for JenisKain
        val adapter = JenisKainAdapter(JenisKainRepository.getData(), this)
        binding.recyclerViewJenisKain.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewJenisKain.adapter = adapter

        // Optional: action on Selengkapnya
        binding.btnSelengkapnya.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_caraKerjaFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sliderHandler.removeCallbacks(sliderRunnable)
        _binding = null
    }

    override fun onItemClick(item: JenisKain) {
        val action = HomeFragmentDirections.actionHomeFragmentToKainDetailFragment(item)
        findNavController().navigate(action)
    }

    class SliderItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.left = space
            outRect.right = space
        }
    }
}
