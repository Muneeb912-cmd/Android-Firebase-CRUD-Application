package com.example.week3_challenge

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ui.ChannelFragment
import ui.LiveFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->LiveFragment()
            1->ChannelFragment()
            else ->{throw Resources.NotFoundException("Position not found")}
        }
    }
}