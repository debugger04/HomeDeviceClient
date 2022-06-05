package com.example.homedeviceclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homedeviceclient.adapter.TransaksiAdapter
import com.example.homedeviceclient.adapter.TransaksiDetailAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.example.homedeviceclient.model.TransaksiDetail
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_ongoing_detail.*
import kotlinx.android.synthetic.main.activity_transaksi.*
import kotlinx.android.synthetic.main.activity_transaksi_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat

class TransaksiDetailActivity : AppCompatActivity() {
    var listTdetail: ArrayList<TransaksiDetail> = ArrayList()
    var id = ""
    lateinit var sp: SharedPrefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi_detail)

        sp = SharedPrefs(this)

        id = intent.getStringExtra("ORDER_ID").toString()

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        detailTransaksi()
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

    fun detailTransaksi() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        pbTd.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.getTransaksiDetail(id).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                pbTd.visibility = View.GONE
                val response = response.body()!!
                if (response.code == 200) {
                    txtTglTr.text = response.transaksi.tanggal
                    txtNnota.text = response.transaksi.nomor_nota
                    val formatter: NumberFormat = DecimalFormat("#,###")
                    val formattedNumber: String = formatter.format(response.tdtotal)
                    txtTotalTr.text = "Rp. "+formattedNumber
                    if (response.transaksi.status == 0) {
                        imgStatusNota.setImageResource(R.drawable.ic_baseline_access_time_filled_24)
                        imgStatusNota.setColorFilter(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                        txtStatusNota.text = "Menunggu persetujuan"
                        txtKeteranganNota.text = "Mohon ditunggu, tim kami sedang mengecek barang kamu!"
                    } else if (response.transaksi.status == 1) {
                        imgStatusNota.setImageResource(R.drawable.ic_baseline_delivery_dining_24)
                        imgStatusNota.setColorFilter(ContextCompat.getColor(applicationContext, R.color.colorPrimaryVariant))
                        txtStatusNota.text = "Dalam pengiriman"
                        txtKeteranganNota.text = "Barang kamu sedang dalam perjalanan!"
                    } else if (response.transaksi.status == 2) {
                        imgStatusNota.setImageResource(R.drawable.ic_baseline_check_circle_24)
                        imgStatusNota.setColorFilter(ContextCompat.getColor(applicationContext, R.color.check))
                        txtStatusNota.text = "Telah sampai"
                        txtKeteranganNota.text = "Barang sudah sampai ke tempat kamu!"
                    } else {
                        imgStatusNota.setImageResource(R.drawable.ic_baseline_cancel_24)
                        imgStatusNota.setColorFilter(ContextCompat.getColor(applicationContext, R.color.cancel))
                        txtStatusNota.text = "Gagal"
                        txtKeteranganNota.text = "Terdapat kendala pada barangmu."
                    }

                    if (response.transaksi.resi == "-") {
                        txtResiNota.text = "Belum diterbitkan"
                    } else {
                        txtResiNota.text = response.transaksi.resi
                    }

                    listTdetail = response.detailtransaksi
                    updateList()
                } else {
                    Toast.makeText(this@TransaksiDetailActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                pbTd.visibility = View.GONE
                Toast.makeText(this@TransaksiDetailActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun updateList() {
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        notaView.setLayoutManager(layout)
        notaView.setHasFixedSize(true)
        notaView.adapter = TransaksiDetailAdapter(listTdetail)
    }
}