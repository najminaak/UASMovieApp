package com.example.uasmovieapp.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.uasmovieapp.R
import com.example.uasmovieapp.fragment.HomeFragment
import com.example.uasmovieapp.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ambil data dengan intent
        val email = intent.getStringExtra("email")
        val username = intent.getStringExtra("username")
        val name = intent.getStringExtra("name")

        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, homeFragment)
            .commit()

        // set bottom navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        val navController = findNavController(R.id.nav_host_fragment)
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profileFragment -> {
                    // Pass data to ProfileFragment when Profile is selected
                    val profileFragment = ProfileFragment().apply {
                        arguments = Bundle().apply {
                            putString("email", email)
                            putString("username", username)
                            putString("name", name)
                        }
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, profileFragment)
                        .commit()
                    true
                }
                R.id.homeFragment -> {

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, homeFragment)
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}