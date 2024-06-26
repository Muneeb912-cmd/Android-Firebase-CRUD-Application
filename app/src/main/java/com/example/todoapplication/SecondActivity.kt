package com.example.todoapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.todoapplication.ui.HomeFragment
import com.example.todoapplication.ui.InfoFragment
import com.example.todoapplication.ui.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TodoApp)
        setContentView(R.layout.activity_second)

        val homeFragment=HomeFragment()
        val infoFragment=InfoFragment()
        val settingsFragment=SettingsFragment()

        setCurrentFragment(homeFragment)

        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigation)

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bottomMenu_Home -> setCurrentFragment(homeFragment)
                R.id.bottomMenu_info -> setCurrentFragment(infoFragment)
                R.id.bottomMenu_settings -> setCurrentFragment(settingsFragment)
            }
            true
        }

    }


    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout2,fragment).commit()
        }
}