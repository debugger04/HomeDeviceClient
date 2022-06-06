package com.example.homedeviceclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_kode_promo.*
import kotlinx.android.synthetic.main.activity_ongoing_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat

class DetailKodePromoActivity : AppCompatActivity() {
    lateinit var sp: SharedPrefs
    var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_kode_promo)

        sp = SharedPrefs(this)

        id = intent.getStringExtra("KP_ID").toString()

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        detailKode()
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

    fun detailKode() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        pb2A1.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.getKodeDetail(id).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                pb2A1.visibility = View.GONE
                val response = response.body()!!
                if (response.code == 200) {
                    txtCodess.text = response.kode.kode
                    val formatter: NumberFormat = DecimalFormat("#,###")
                    val formattedNumber1: String = formatter.format(response.kode.maximum_cut)
                    val formattedNumber2: String = formatter.format(response.kode.minimum_spend)
                    txtBerlaku.text = response.kode.tanggal_dibuat+" hingga "+response.kode.masa_berlaku
                    if (response.kode.efek == "DC") {
                        txtEfek.text = "Discount "+response.kode.nilai+"%"
                        txtPenjelas.text = "Dapatkan potongan diskon senilai "+response.kode.nilai+"% dengan maksimum potongan sebesar Rp."+formattedNumber1+" dengan syarat minimal berbelanja sebesar Rp."+formattedNumber2+".\nKode voucher ini hanya dapat digunakan sekali.\nPastikan untuk mengecek tanggal berlaku !"
                    } else {
                        txtEfek.text = "Cashback "+response.kode.nilai+"%"
                        txtPenjelas.text = "Dapatkan cashback senilai "+response.kode.nilai+"% dengan maksimum cashback"+formattedNumber1+" poin dengan syarat minimal berbelanja sebesar Rp."+formattedNumber2+".\n" +
                                "Kode voucher ini hanya dapat digunakan sekali.\nPastikan untuk mengecek tanggal berlaku !"
                    }
                } else {
                    Toast.makeText(this@DetailKodePromoActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                pb2A1.visibility = View.GONE
                Toast.makeText(this@DetailKodePromoActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}