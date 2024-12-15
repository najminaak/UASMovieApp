package com.example.uasmovieapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.uasmovieapp.R
import com.example.uasmovieapp.activity.admin.HomeAdminActivity
import com.example.uasmovieapp.activity.MainActivity
import com.example.uasmovieapp.databinding.FragmentLoginBinding
import com.example.uasmovieapp.utils.PrefManager

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.buttonLogin.setOnClickListener {
            performLogin()
        }

        binding.registerText.setOnClickListener {
            (requireActivity().findViewById<ViewPager2>(R.id.viewPager)).currentItem = 1
        }

        return binding.root
    }

    private fun performLogin() {
        // simpan data yang dimasukkan user
        val usernameInput = binding.editEmailLogin.text.toString().trim()
        val passwordInput = binding.editPasswordLogin.text.toString().trim()

        // pastikan semua kolom keisi
        if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // ambil data from PrefManager
        val prefManager = PrefManager.getInstance(requireContext())
        val storedUsername = prefManager.getUsername()
        val storedPassword = prefManager.getPassword()
        val storedRole = prefManager.getRole()

        // cek kredensial
        if (usernameInput == storedUsername && passwordInput == storedPassword) {
            // kalau admin ke halaman admin
            if (storedRole == "admin") {
                val intent = Intent(requireContext(), HomeAdminActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                // kalau user ke halaman main
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        } else {
            Toast.makeText(requireContext(), "Invalid username or password", Toast.LENGTH_SHORT).show()
        }
    }
}
