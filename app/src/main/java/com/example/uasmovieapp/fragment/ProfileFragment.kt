package com.example.uasmovieapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uasmovieapp.R
import com.example.uasmovieapp.activity.LoginRegisterActivity
import com.example.uasmovieapp.databinding.FragmentProfileBinding
import com.example.uasmovieapp.utils.PrefManager

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Inisialisasi PrefManager
        prefManager = PrefManager.getInstance(requireContext())

        // Ambil data dari SharedPreferences melalui PrefManager
        val email = prefManager.getEmail() // Mengambil email
        val username = prefManager.getUsername() // Mengambil username
        val name = prefManager.getName() // Mengambil name

        // Tampilkan data ke dalam TextView
        binding.emailView.text = email
        binding.usernameView.text = username
        binding.nameView.text = name

        // Tombol logout
        binding.buttonLogout.setOnClickListener {
            // Clear SharedPreferences jika logout
            prefManager.clear()

            // Navigasi ke LoginRegisterActivity
            val intent = Intent(requireActivity(), LoginRegisterActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        // Tombol untuk premium (mengarahkan ke Netflix)
        binding.buttonPremium.setOnClickListener {
            val url = "https://www.netflix.com/signup"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = android.net.Uri.parse(url)
            }
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
