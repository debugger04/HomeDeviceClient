package com.example.homedeviceclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homedeviceclient.adapter.KodeDimilikiAdapter
import com.example.homedeviceclient.adapter.KodeFindAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.example.homedeviceclient.model.KodePromo
import kotlinx.android.synthetic.main.activity_kode_promo.*
import kotlinx.android.synthetic.main.activity_search_kode_promo.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchKodePromoActivity : AppCompatActivity() {
    var listKodePromo: ArrayList<KodePromo> = ArrayList()
    lateinit var sp: SharedPrefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_kode_promo)

        sp = SharedPrefs(this)

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        btn_search.setOnClickListener {
            searchKodePromo()
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

    private fun searchKodePromo() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        pbA12.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.findKodePromo(txtCodeFind.text.toString()).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                pbA12.visibility = View.GONE
                val response = response.body()!!
                if(response.code == 200) {
                    listKodePromo = response.kodepromo
                    updateList()
                } else {
                    Toast.makeText(this@SearchKodePromoActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                pbA12.visibility = View.GONE
                Toast.makeText(this@SearchKodePromoActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateList() {
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        searchCodeView.setLayoutManager(layout)
        searchCodeView.setHasFixedSize(true)
        searchCodeView.adapter = KodeFindAdapter(listKodePromo)
    }
}