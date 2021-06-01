package ru.wood.cuber.ui.diametrs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import ru.wood.cuber.adapters.MyPagerAdapter
import ru.wood.cuber.databinding.FragmentDiametrContainerBinding

class DiametrContainer : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding=FragmentDiametrContainerBinding.inflate(inflater)
        val view=binding.root

        val tabLayout=binding.tabs
        val viewPager=binding.pager
        val adapter: FragmentStateAdapter = MyPagerAdapter(requireActivity())
        viewPager.adapter = adapter
        TabLayoutMediator(
            tabLayout, viewPager
        ) { tab, position -> }.attach()
        return view
    }
}