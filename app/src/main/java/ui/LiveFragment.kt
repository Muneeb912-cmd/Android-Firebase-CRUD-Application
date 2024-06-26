import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.week3_challenge.*
import com.google.android.material.tabs.TabLayout
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class LiveFragment : Fragment(), RecyclerViewAdapter.OnItemClickListener {

    private lateinit var cardViewModel: CardDataViewModel
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private var dataList = ArrayList<DataClass>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = CardDataRepository()
        val factory = CardDataViewModelFactory(repository)
        cardViewModel = ViewModelProvider(this, factory)[CardDataViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_live, container, false)
        setUpRecyclerView(view)

        val repository = CardDataRepository()
        val factory = CardDataViewModelFactory(repository)
        cardViewModel = ViewModelProvider(this, factory)[CardDataViewModel::class.java]

        // Observe LiveData
        cardViewModel.cardData.observe(viewLifecycleOwner, Observer { data ->
            if (data.isNotEmpty()) {
                data?.let {
                    dataList.clear()
                    dataList.addAll(it)
                    print(it)
                    adapter.notifyDataSetChanged()
                }
            }
        })

        return view
    }

    private fun setUpRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        adapter = RecyclerViewAdapter(dataList, this)
        recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Defining custom Spinner
        val mySpinner = view.findViewById<Spinner>(R.id.spinner)
        val spinnerList = arrayOf("Today", "Tomorrow", "Yesterday")
        val mArrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, spinnerList)
        mArrayAdapter.setDropDownViewResource(R.layout.spinner_item)
        mySpinner.adapter = mArrayAdapter

        // Populating tab layout
        val tabsList = timeList()

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout2)
        for (time in tabsList) {
            val tab = tabLayout.newTab()
            tab.text = time
            tabLayout.addTab(tab)
        }

    }

    private fun timeList(): List<String> {
        val tabsList = listOf(
            "01:00 AM", "02:00 AM", "03:00 AM", "04:00 AM", "05:00 AM",
            "06:00 AM", "07:00 AM", "08:00 AM", "09:00 AM",
            "10:00 AM", "11:00 AM", "12:00 PM", "13:00 PM",
            "14:00 PM", "15:00 PM", "16:00 PM", "17:00 PM",
            "18:00 PM", "19:00 PM", "20:00 PM", "21:00 PM",
            "22:00 PM", "23:00 PM", "24:00 PM"
        )
        return tabsList
    }

    override fun onItemClick(position: Int) {
        val clickedItem = dataList[position]
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setTitle("Click Action")
            .setMessage("Click Action happened on: ${clickedItem.cardTitle}")
            .setIcon(R.drawable.ic_info)
            .setPositiveButton("Ok") { dialog, _ ->
                Toast.makeText(requireContext(), "Okay Clicked", Toast.LENGTH_SHORT).show()
            }
        alertDialogBuilder.show()
    }

    override fun onButtonClick(position: Int) {
        val clickedItem = dataList[position]
        val alertDialogBuilder = alertDialogBuilder(clickedItem)


        // Inflate custom layout for EditText
        val layoutInflater = LayoutInflater.from(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.edit_card_data, null)
        alertDialogBuilder.setView(dialogView)

        // Access the EditText fields in the dialog layout
        val editCardTitle = dialogView.findViewById<EditText>(R.id.editCardTitle)
        val editStartTime = dialogView.findViewById<EditText>(R.id.editStartTime)
        val editEndTime = dialogView.findViewById<EditText>(R.id.editEndTime)
        val editProgress = dialogView.findViewById<EditText>(R.id.editProgress)
        val editImgCaption = dialogView.findViewById<EditText>(R.id.editImgCaption)
        val editImage = dialogView.findViewById<ImageView>(R.id.editImage)
        val storageReference: StorageReference =
            FirebaseStorage.getInstance().getReferenceFromUrl(clickedItem.imgId)
        val uploadBtn = dialogView.findViewById<Button>(R.id.uploadPic)

        // Get the download URL
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(editImage.context)
                .load(uri.toString())
                .into(editImage)
        }.addOnFailureListener {
            editImage.setImageResource(R.drawable.ic_photo) // Set a placeholder image or handle error
        }

        populateData(
            editCardTitle,
            clickedItem,
            editStartTime,
            editEndTime,
            editProgress,
            editImgCaption,
            editImage
        )

        // Set positive button (Update)
        alertDialogBuilder.setPositiveButton("Update") { dialog, _ ->
            onUpdateClick(
                editCardTitle,
                editStartTime,
                editEndTime,
                editProgress,
                editImgCaption,
                clickedItem,
                editImage
            )
        }

        // Set negative button (Cancel)
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            // Cancel action, do nothing or dismiss dialog
            dialog.dismiss()
        }

        // Show the AlertDialog
        alertDialogBuilder.show()
    }

    private fun populateData(
        editCardTitle: EditText,
        clickedItem: DataClass,
        editStartTime: EditText,
        editEndTime: EditText,
        editProgress: EditText,
        editImgCaption: EditText,
        editImage: ImageView
    ) {
        // Populate EditText fields with current data
        editCardTitle.setText(clickedItem.cardTitle)
        editStartTime.setText(clickedItem.startTime)
        editEndTime.setText(clickedItem.endTime)
        editProgress.setText(clickedItem.progress.toString())
        editImgCaption.setText(clickedItem.imgCaption)
        editImage.setImageURI(clickedItem.imgId.toUri())
    }

    private fun onUpdateClick(
        editCardTitle: EditText,
        editStartTime: EditText,
        editEndTime: EditText,
        editProgress: EditText,
        editImgCaption: EditText,
        clickedItem: DataClass,
        editImage: ImageView
    ) {
        val updatedField1 = editCardTitle.text.toString()
        val updatedField2 = editStartTime.text.toString()
        val updatedField3 = editEndTime.text.toString()
        val updatedField4 = editProgress.text.toString()
        val updatedField5 = editImgCaption.text.toString()

        // Perform update action, e.g., update dataList or show a toast
        // Update clickedItem with new values
        clickedItem.cardTitle = updatedField1
        clickedItem.startTime = updatedField2
        clickedItem.endTime = updatedField3
        clickedItem.progress = updatedField4.toInt()
        clickedItem.imgCaption = updatedField5

        Toast.makeText(
            requireContext(),
            "${clickedItem.cardTitle} Successfully Updated!",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun alertDialogBuilder(clickedItem: DataClass): AlertDialog.Builder {
        // Build the AlertDialog
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Update ${clickedItem.cardTitle}")
        alertDialogBuilder.setMessage("Click Action happened on: ${clickedItem.cardTitle}")
        alertDialogBuilder.setIcon(R.drawable.ic_info)
        return alertDialogBuilder
    }

}
