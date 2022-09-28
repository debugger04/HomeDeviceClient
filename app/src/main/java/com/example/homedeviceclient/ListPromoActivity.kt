package com.example.homedeviceclient

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.homedeviceclient.adapter.ListAlamatAdapter
import com.example.homedeviceclient.adapter.PromoAdapter
import com.example.homedeviceclient.adapter.PromoNavAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.fragment.ListBrdFragment
import com.example.homedeviceclient.fragment.ListCtgFragment
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.example.homedeviceclient.model.Alamat
import com.example.homedeviceclient.model.Promo
import kotlinx.android.synthetic.main.activity_alamat.*
import kotlinx.android.synthetic.main.activity_list_alamat.*
import kotlinx.android.synthetic.main.activity_list_alamat.alamatView
import kotlinx.android.synthetic.main.activity_list_promo.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListPromoActivity : AppCompatActivity() {
    var frag: ArrayList<Fragment> = ArrayList()
    lateinit var sp: SharedPrefs
    var total = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_promo)

        sp = SharedPrefs(this)

        total = intent.getIntExtra("total", 0)

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        with(frag) {
            add(ListCtgFragment())
            add(ListBrdFragment())
        }

        vp1.adapter = PromoNavAdapter(this, frag)

        vp1.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    btn_brd.setTextColor(Color.parseColor("#5E5E5E"))
                    btn_ctg.setTextColor(Color.parseColor("#00C2CB"))
                } else if (position == 1) {
                    btn_ctg.setTextColor(Color.parseColor("#5E5E5E"))
                    btn_brd.setTextColor(Color.parseColor("#00C2CB"))
                }
            }
        })

        btn_ctg.setOnClickListener {
            vp1.currentItem = 0
        }

        btn_brd.setOnClickListener {
            vp1.currentItem = 1
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    public fun totalBelanja() : Int {
        return total
    }
}