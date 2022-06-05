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
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.example.homedeviceclient.model.Transaksi
import kotlinx.android.synthetic.main.activity_ongoing_saldo.*
import kotlinx.android.synthetic.main.activity_transaksi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransaksiActivity : AppCompatActivity() {
    lateinit var sp: SharedPrefs
    var listTransaksi: ArrayList<Transaksi> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi)

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

        pbTr.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.getTransaksi(user.email).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                pbTr.visibility = View.GONE
                val response = response.body()!!
                if(response.code == 200) {
                    listTransaksi = response.transaksis
                    updateList()
                } else {
                    Toast.makeText(this@TransaksiActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                pbTr.visibility = View.GONE
                Toast.makeText(this@TransaksiActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateList() {
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        transView.setLayoutManager(layout)
        transView.setHasFixedSize(true)
        transView.adapter = TransaksiAdapter(listTransaksi)
    }
}