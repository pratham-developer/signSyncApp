package com.example.signsyncapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find profile ImageView
        val profileImage = view.findViewById<ImageView>(R.id.Sign_out)

        // Set click listener to navigate to Starting_page activity
        profileImage.setOnClickListener {
            val intent = Intent(requireContext(), Starting_page::class.java)
            startActivity(intent)
        }
    }
}
