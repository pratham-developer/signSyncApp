package com.example.signsyncapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        // Find profile ImageView
        val logoutBut = view.findViewById<ImageView>(R.id.Sign_out)

        // Set click listener to navigate to Starting_page activity
        logoutBut.setOnClickListener {
            performSignOut()
        }
    }
    private fun performSignOut() {
        try {
            auth.signOut()
            val intent = Intent(requireContext(), Starting_page::class.java)
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("Home Fragment", "exception: ", e)
        }
    }
}
