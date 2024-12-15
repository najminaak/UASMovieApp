package com.example.uasmovieapp.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.uasmovieapp.adapter.LoginRegisterPagerAdapter
import com.example.uasmovieapp.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class LoginRegisterActivity : AppCompatActivity() {

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_register)

        val loginRegisterPagerAdapter = LoginRegisterPagerAdapter(this)
        val viewPager : ViewPager2 = findViewById(R.id.viewPager)

        viewPager.adapter = loginRegisterPagerAdapter


        val tabs : TabLayout = findViewById(R.id.tabLayout)
        TabLayoutMediator(tabs, viewPager) {
                tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])}.attach()
    }
}