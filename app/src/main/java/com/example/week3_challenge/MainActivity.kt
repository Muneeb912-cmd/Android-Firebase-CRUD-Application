package com.example.week3_challenge

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var changeImage: ActivityResultLauncher<Intent>
    private var currentEditImage: ImageView? = null
    private var selectedImageUri: Uri? = null
    private lateinit var cardViewModel: CardDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Week3_Challenge)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, index ->
            tab.text = when (index) {
                0 -> "LIVE"
                1 -> "CHANNELS"
                else -> throw Resources.NotFoundException("Position not found")
            }
        }.attach()

        // Initialize DrawerLayout and Toolbar
        drawerLayout = findViewById(R.id.main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialize NavigationView and set its listener
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        // Setup ActionBarDrawerToggle
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        changeImage = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val imgUri = data?.data
                selectedImageUri = imgUri
                currentEditImage?.setImageURI(imgUri)
                // You might want to store imgUri somewhere for later use
            }
        }

        val repository = CardDataRepository()
        val factory = CardDataViewModelFactory(repository)
        cardViewModel = ViewModelProvider(this, factory)[CardDataViewModel::class.java]

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                return true
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addCard -> {
                addCardAlertDialog()
            }
        }
        return true
    }

    private fun alertDialogBuilder(): AlertDialog.Builder {
        // Build the AlertDialog
        val alertDialogBuilder = AlertDialog.Builder(this)
        return alertDialogBuilder
    }

    private fun addCardAlertDialog() {
        val alertDialogBuilder = alertDialogBuilder()
        val layoutInflater = LayoutInflater.from(this)
        val dialogView = layoutInflater.inflate(R.layout.edit_card_data, null)
        alertDialogBuilder.setView(dialogView)

        // Access the EditText fields in the dialog layout
        val editCardTitle = dialogView.findViewById<EditText>(R.id.editCardTitle)
        val editStartTime = dialogView.findViewById<EditText>(R.id.editStartTime)
        val editEndTime = dialogView.findViewById<EditText>(R.id.editEndTime)
        val editProgress = dialogView.findViewById<EditText>(R.id.editProgress)
        val editImgCaption = dialogView.findViewById<EditText>(R.id.editImgCaption)
        val editImage = dialogView.findViewById<ImageView>(R.id.editImage)
        val uploadBtn = dialogView.findViewById<Button>(R.id.uploadPic)

        alertDialogBuilder.setTitle("Create Card")
        alertDialogBuilder.setIcon(R.drawable.ic_add)

        // Set positive button (Update)
        alertDialogBuilder.setPositiveButton("Create") { dialog, _ ->
            onCreateClick(
                editImage,
                editCardTitle,
                editStartTime,
                editEndTime,
                editProgress,
                editImgCaption
            )
        }

        // Set negative button (Cancel)
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            // Cancel action, do nothing or dismiss dialog
            dialog.dismiss()
        }

        // Show the AlertDialog
        alertDialogBuilder.show()

        uploadBtn.setOnClickListener {
            currentEditImage = editImage // Set the ImageView where the image will be displayed
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            changeImage.launch(pickImg)
        }

    }

    private fun onCreateClick(
        editImage: ImageView,
        editCardTitle: EditText,
        editStartTime: EditText,
        editEndTime: EditText,
        editProgress: EditText,
        editImgCaption: EditText
    ) {
        val dataClass = DataClass(
            cardTitle = editCardTitle.text.toString(),
            startTime = editStartTime.text.toString(),
            endTime = editEndTime.text.toString(),
            progress = editProgress.text.toString().toInt(),
            imgCaption = editImgCaption.text.toString(),
            imgId = selectedImageUri.toString()
        )
        try {
            cardViewModel.addTask(dataClass, imageUri = selectedImageUri as Uri)
        }catch (e:Exception){
            Log.d("Error", "onCreateClick: $e")
        }

    }
}
