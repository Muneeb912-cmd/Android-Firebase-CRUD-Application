package com.example.todoapplication.ui


import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.model.AdapterClass


import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.todoapplication.DataClass
import com.example.todoapplication.R
import com.example.todoapplication.RecyclerViewAdapterClass

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RecyclerViewFragment : Fragment(), RecyclerViewAdapterClass.OnItemClickListener {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass>
    private lateinit var imageList: Array<Int>
    private lateinit var titleList: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recycler_view, container, false)

        imageList = arrayOf(
            R.drawable.ic_add,
            R.drawable.ic_fav,
            R.drawable.ic_home,
            R.drawable.ic_info,
            R.drawable.ic_list,
            R.drawable.ic_fav_outline,
            R.drawable.ic_logout
        )

        titleList = arrayOf(
            "Add",
            "Fav",
            "Home",
            "Info",
            "List",
            "Fav_Outlined",
            "Logout"
        )

        recyclerView = view.findViewById(R.id.recyclerView1)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        dataList = arrayListOf()
        getData()

        // Set up ItemTouchHelper for swipe functionality
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                dataList.removeAt(position)
                recyclerView.adapter?.notifyItemRemoved(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecyclerViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun getData() {
        for (i in imageList.indices) {
            val dataClass = DataClass(imageList[i], titleList[i])
            dataList.add(dataClass)
        }
        recyclerView.adapter = RecyclerViewAdapterClass(dataList, this)
    }


    val positiveButtonClick={ dialog:DialogInterface, which:Int->
        Toast.makeText(requireContext(),"Okay Clicked",Toast.LENGTH_SHORT).show()

    }

    override fun onItemClick(position: Int) {
        val clickedItem = dataList[position]
        val alertDialogBuilder = AlertDialog.Builder(requireContext()).setTitle("Click Action").setMessage("Click Action happened on : ${clickedItem.dataTitle}")
        alertDialogBuilder.setIcon(R.drawable.ic_info)
        alertDialogBuilder.setPositiveButton("Ok",positiveButtonClick)
        alertDialogBuilder.show()
//        Toast.makeText(requireContext(), "Clicked: ${clickedItem.dataTitle}", Toast.LENGTH_SHORT).show()
    }
}
