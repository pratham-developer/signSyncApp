package com.example.signsyncapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class UploadFragment : Fragment() {

    private var selectedVideoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.upload_fragment, container, false)

        val uploadVideo = view.findViewById<ImageView>(R.id.upload_video)

        // Set click listener
        uploadVideo.setOnClickListener {
            openGallery()
        }

        return view
    }

    private val pickVideoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                selectedVideoUri = data?.data

                if (selectedVideoUri != null) {
                    Toast.makeText(requireContext(), "Video Selected: $selectedVideoUri", Toast.LENGTH_SHORT).show()
                    // You can now upload or process the video
                }
            }
        }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "video/*"
        pickVideoLauncher.launch(intent)
    }
}
