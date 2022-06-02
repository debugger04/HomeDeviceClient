package com.example.homedeviceclient

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_ongoing_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream
import java.net.URL
import java.text.DecimalFormat
import java.text.NumberFormat


class OngoingDetailActivity : AppCompatActivity() {
    lateinit var sp: SharedPrefs
    var id = ""
    var uri = "http://192.168.0.104/ta/tugasakhir/public/bukti_transfer/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ongoing_detail)

        sp = SharedPrefs(this)

        id = intent.getStringExtra("WQ_ID").toString()

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        detailOngoing()
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

    fun detailOngoing() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        pbOdeta.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.getWalletOngoingDetail(id).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                pbOdeta.visibility = View.GONE
                val response = response.body()!!
                if (response.code == 200) {
                    txtTgl.text = response.walletreq.tanggal
                    val formatter: NumberFormat = DecimalFormat("#,###")
                    val formattedNumber: String = formatter.format(response.walletreq.jumlah)
                    txtJumlah.text = "Rp. "+formattedNumber
                    Picasso.get().load(uri+response.walletreq.bukti_transfer).into(imgBuktiTrf)
                    if (response.walletreq.status == 0) {
                        imgStatus.setImageResource(R.drawable.ic_baseline_access_time_filled_24)
                        imgStatus.setColorFilter(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                        txtStatusDetail.text = "Menunggu persetujuan"
                        txtKeterangan.text = "Mohon ditunggu, tim kami sedang mengecek uang yang kamu kirim!"
                        cardVerf.visibility = View.GONE
                    } else if(response.walletreq.status == 1) {
                        imgStatus.setImageResource(R.drawable.ic_baseline_check_circle_24)
                        imgStatus.setColorFilter(ContextCompat.getColor(applicationContext, R.color.check))
                        txtStatusDetail.text = "Berhasil"
                        txtKeterangan.text = "Saldo sudah kami tambahkan ke e-wallet kamu!"
                        txt2.visibility = View.GONE
                        txtKomen.visibility = View.GONE
                        txtTglVerf.text = response.walletreq.tanggal_verifikasi
                    } else {
                        imgStatus.setImageResource(R.drawable.ic_baseline_cancel_24)
                        imgStatus.setColorFilter(ContextCompat.getColor(applicationContext, R.color.cancel))
                        txtStatusDetail.text = "Ditolak"
                        txt41.text = "Tanggal Ditolak"
                        txtKeterangan.text = "Maaf, permohonan kami tolak harap cek kolom alasan dibawah!"
                        txtKomen.text = response.walletreq.komentar
                        txtTglVerf.text = response.walletreq.tanggal_verifikasi
                    }
                } else {
                    Toast.makeText(this@OngoingDetailActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                pbOdeta.visibility = View.GONE
                Toast.makeText(this@OngoingDetailActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}