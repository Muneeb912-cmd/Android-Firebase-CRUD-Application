package com.example.todoapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PreviewTodo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_preview_todo)
        setTheme(R.style.Theme_TodoApp_Dark)
        val todoTitle = intent.getStringExtra("TODO_TITLE")
        val todoDeadline = intent.getStringExtra("TODO_DEADLINE")
        val todoDescription = intent.getStringExtra("TODO_DESCRIPTION")
        val todoPriority = intent.getStringExtra("TODO_PRIORITY")

        val titlePreview: TextView = findViewById(R.id.titlePreview)
        val deadlinePreview: TextView = findViewById(R.id.deadlinePreview)
        val descriptionPreview: TextView = findViewById(R.id.decriptionPreview)
        val priorityPreview: TextView = findViewById(R.id.priorityPreview)

        titlePreview.text = "Title: $todoTitle"
        deadlinePreview.text = "Deadline: $todoDeadline"
        descriptionPreview.text = "Description: $todoDescription"
        priorityPreview.text = "Priority: $todoPriority"

        val btnOkay = findViewById<Button>(R.id.btnOkay)

        btnOkay.setOnClickListener {
            finish()
        }
    }
}