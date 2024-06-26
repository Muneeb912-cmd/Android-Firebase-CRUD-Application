package com.example.todoapplication

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

import com.example.todoapplication.ui.*
import com.google.android.material.navigation.NavigationView

import android.view.Menu
import android.view.MenuItem
import android.util.Log

import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_TodoApp)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestsPermissions()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize DrawerLayout and Toolbar
        drawerLayout = findViewById(R.id.main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialize NavigationView and set its listener
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        // Setup ActionBarDrawerToggle
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Default fragment transaction
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
            navigationView.setCheckedItem(R.id.nav_home)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
            }
            R.id.nav_share -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ShareFragment()).commit()
            }
            R.id.nav_setting -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SettingsFragment()).commit()
            }
            R.id.nav_info -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, InfoFragment()).commit()
            }
            R.id.option1 -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, OptionFragment()).commit()
            }
            R.id.recyclerView -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    com.example.todoapplication.ui.RecyclerViewFragment()
                ).commit()
            }
            R.id.viewPager -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    com.example.todoapplication.ui.ViewPager()
                ).commit()
            }
            R.id.nav_logout -> {
                finish()
            }

            R.id.tab_Layout -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    com.example.todoapplication.ui.TabViewFragment()
                ).commit()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun hasExternalStoragePermission() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun hasForeBackgroundLocationPermission() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun hasBackgroundLocationPermission() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
        } else {
            TODO("VERSION.SDK_INT < Q")
        }

    private fun requestsPermissions() {
        val permissionsToRequest = mutableListOf<String>()
        if (!hasBackgroundLocationPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                permissionsToRequest.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
        }
        if (!hasForeBackgroundLocationPermission()) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (!hasExternalStoragePermission()) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission Requests", "onRequestPermissionsResult: ${permissions[i]} granted")
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tool_bar_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.theme_mode -> {
                Toast.makeText(this, "Theme Button Clicked", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.exit_app -> finish()
            R.id.toolbar_item1 -> Intent(this,TabViewActivity::class.java).also {
                startActivity(it)
            }
            R.id.toolbar_item2 -> Toast.makeText(this, "Toolbar Item 2 Clicked", Toast.LENGTH_SHORT).show()
            R.id.toolbar_item3 -> Toast.makeText(this, "Toolbar Item 3 Clicked", Toast.LENGTH_SHORT).show()
        }
        return true
    }


}
