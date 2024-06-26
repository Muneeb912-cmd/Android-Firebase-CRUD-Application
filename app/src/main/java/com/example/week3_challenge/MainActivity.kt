package com.example.week3_challenge

import android.content.res.Resources
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ui.LiveFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Week3_Challenge)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tabLayout=findViewById<TabLayout>(R.id.tabLayout)
        val viewPager=findViewById<ViewPager2>(R.id.viewPager)

        viewPager.adapter=ViewPagerAdapter(this)
        TabLayoutMediator(tabLayout,viewPager){
            tab,index->
            tab.text=when(index){
                0->{"LIVE"}
                1->{"CHANNELS"}
                else->{throw  Resources.NotFoundException("Position not found")}
            }
        }.attach()

    }
}