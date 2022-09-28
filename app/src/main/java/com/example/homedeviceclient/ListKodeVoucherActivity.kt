package com.example.homedeviceclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homedeviceclient.adapter.ListAlamatAdapter
import com.example.homedeviceclient.adapter.ListKodePromoAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.example.homedeviceclient.model.Alamat
import com.example.homedeviceclient.model.KodePromo
import kotlinx.android.synthetic.main.activity_alamat.*
import kotlinx.android.synthetic.main.activity_kode_promo.*
import kotlinx.android.synthetic.main.activity_list_kode_voucher.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListKodeVoucherActivity : AppCompatActivity() {
    var listKodePromo: ArrayList<KodePromo> = ArrayList()
    lateinit var sp: SharedPrefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_kode_voucher)

        sp = SharedPrefs(this)

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        getKodePromo()
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

    private fun getKodePromo() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        ApiConfig.instanceRetrofit.getKodePromo(user.email).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    txtKosong.visibility = View.GONE
                    kodeView.visibility = View.VISIBLE
                    listKodePromo = response.kodepromo
                    updateList()
                } else {
                    Toast.makeText(this@ListKodeVoucherActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@ListKodeVoucherActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateList() {
        var kode = 0
        if (sp.getVoucher() == null) {
            kode = 0
        } else {
            val a = sp.getVoucher()!!
            kode = a.id
        }
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        kodeView.setLayoutManager(layout)
        kodeView.setHasFixedSize(true)
        kodeView.adapter = ListKodePromoAdapter(listKodePromo, kode, object: ListKodePromoAdapter.Listeners{
            override fun onClicked(data: KodePromo) {
                sp.setVoucher(data)
                onBackPressed()
            }
        })
    }
}