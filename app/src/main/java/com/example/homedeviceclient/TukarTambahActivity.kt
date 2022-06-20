package com.example.homedeviceclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homedeviceclient.adapter.TransaksiAdapter
import com.example.homedeviceclient.adapter.TukarTambahAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.example.homedeviceclient.model.Transaksi
import com.example.homedeviceclient.model.TukarTambah
import kotlinx.android.synthetic.main.activity_transaksi.*
import kotlinx.android.synthetic.main.activity_tukar_tambah.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TukarTambahActivity : AppCompatActivity() {
    lateinit var sp: SharedPrefs
    var listTransaksi: ArrayList<TukarTambah> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tukar_tambah)

        sp = SharedPrefs(this)

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        getTransaksi()
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

    private fun getTransaksi() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        pbTt12.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.getTransaksiT(user.email).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                pbTt12.visibility = View.GONE
                val response = response.body()!!
                if(response.code == 200) {
                    listTransaksi = response.tukartambahs
                    updateList()
                } else {
                    Toast.makeText(this@TukarTambahActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                pbTt12.visibility = View.GONE
                Toast.makeText(this@TukarTambahActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateList() {
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        ttView.setLayoutManager(layout)
        ttView.setHasFixedSize(true)
        ttView.adapter = TukarTambahAdapter(listTransaksi)
    }
}