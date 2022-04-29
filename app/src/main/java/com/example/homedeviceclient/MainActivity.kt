package com.example.homedeviceclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.homedeviceclient.adapter.BottomNavAdapter
import com.example.homedeviceclient.fragment.AccountFragment
import com.example.homedeviceclient.fragment.CartFragment
import com.example.homedeviceclient.fragment.CategoryFragment
import com.example.homedeviceclient.fragment.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val frags: ArrayList<Fragment> = ArrayList()
    private var loggedIn = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(frags) {
            add(HomeFragment())
            add(CategoryFragment())
            add(CartFragment())
            add(AccountFragment())
        }

        viewPager.adapter = BottomNavAdapter(this, frags)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNav.selectedItemId = bottomNav.menu.getItem(position).itemId
            }
        })

        bottomNav.setOnItemSelectedListener {
            if(it.itemId == R.id.itemHome) {
                viewPager.currentItem = 0
            } else if(it.itemId == R.id.itemKategori) {
                viewPager.currentItem = 1
            } else if(it.itemId == R.id.itemCart) {
                viewPager.currentItem = 2
            } else {
                if (loggedIn) {
                    viewPager.currentItem = 3
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
            true
        }
    }
}