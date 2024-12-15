package com.example.uasmovieapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.uasmovieapp.utils.PrefManager
import com.example.uasmovieapp.R
import com.example.uasmovieapp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRegister.setOnClickListener {
            // simpan data yang dimasukkan di aplikasi
            val name = binding.editNameRegis.text.toString().trim()
            val username = binding.editUsernameRegis.text.toString().trim()
            val email = binding.editEmailRegis.text.toString().trim()
            val password = binding.editPasswordRegis.text.toString().trim()

            // periksa keisi semua apa gak kolomnya
            if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val prefManager = PrefManager.getInstance(requireContext())

                // simpan user detail ke shared preference
                prefManager.saveName(name)
                prefManager.saveUsername(username)
                prefManager.saveEmail(email)
                prefManager.savePassword(password)
                prefManager.saveRole("admin")

                Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_SHORT).show()

                // pindah ke halaman login
                (requireActivity().findViewById<ViewPager2>(R.id.viewPager)).currentItem = 0
            }
        }

        // pindah ke halaman login
        binding.loginText.setOnClickListener {
            (requireActivity().findViewById<ViewPager2>(R.id.viewPager)).currentItem = 0
        }
    }
}
