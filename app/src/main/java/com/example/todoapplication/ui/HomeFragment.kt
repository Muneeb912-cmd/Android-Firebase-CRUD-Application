package com.example.todoapplication.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.example.todoapplication.PreviewTodo
import com.example.todoapplication.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chipGroup = view.findViewById<ChipGroup>(R.id.priority_chips)
        val lowPriorityChip = view.findViewById<Chip>(R.id.low_priority)
        val highPriorityChip = view.findViewById<Chip>(R.id.high_priority)
        val veryHighPriorityChip = view.findViewById<Chip>(R.id.v_high_priority)

        val chips = listOf(lowPriorityChip, highPriorityChip, veryHighPriorityChip)

        for (chip in chips) {
            chip.isCheckable = true
            chip.setOnClickListener {
                for (c in chips) {
                    c.isChecked = c == chip
                }
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val isKeyboardVisible = imeInsets.bottom > systemBarsInsets.bottom
            v.updatePadding(bottom = if (isKeyboardVisible) imeInsets.bottom else systemBarsInsets.bottom)
            insets
        }



        var selectedText=""
        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val checkedId = checkedIds[0]
                val checkedChip = group.findViewById<Chip>(checkedId)
                val selectedChipText = checkedChip?.text?.toString()
                selectedText = selectedChipText.toString()

                selectedChipText?.let {
                    // Use 'it' (selectedChipText) to perform actions with the selected chip text
                    println("Selected Chip Text: $it")

                }
            } else {
                selectedText = "None"
                println("No chip is selected")
            }
        }



        val saveBtn=view.findViewById<Button>(R.id.btnSave)
        saveBtn.setOnClickListener {
            Intent(requireContext(),PreviewTodo::class.java).also {
                val todoTitle=view.findViewById<TextView>(R.id.todos_title).text.toString()
                val todoDeadline=view.findViewById<DatePicker>(R.id.deadline_picker)
                val todoDescription=view.findViewById<TextView>(R.id.todos_description).text.toString()

                val day = todoDeadline.dayOfMonth
                val month = todoDeadline.month
                val year = todoDeadline.year

                //Date to String
                val selectedDate = "$day/${month + 1}/$year"





                it.putExtra("TODO_TITLE",todoTitle)
                it.putExtra("TODO_DEADLINE",selectedDate)
                it.putExtra("TODO_DESCRIPTION",todoDescription)
                it.putExtra("TODO_PRIORITY",selectedText)

                startActivity(it)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {

            }
    }
}