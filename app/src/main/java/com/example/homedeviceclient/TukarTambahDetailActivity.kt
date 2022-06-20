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
import kotlinx.android.synthetic.main.activity_transaksi_detail.*
import kotlinx.android.synthetic.main.activity_tukar_tambah_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat

class TukarTambahDetailActivity : AppCompatActivity() {
    lateinit var sp: SharedPrefs
    var id = ""
    var urip = "http://192.168.0.104/ta/tugasakhir/public/product_images/"
    var urif = "http://192.168.0.104/ta/tugasakhir/public/flagship_images/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tukar_tambah_detail)

        sp = SharedPrefs(this)

        id = intent.getStringExtra("ORDER_ID").toString()

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        detailTransaksi()

        btnChatPetugas.setOnClickListener {
            val intent = Intent(this, ForumActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("O1_ID", id)
            startActivity(intent)
        }

        btnBayarTt.setOnClickListener {
            val intent = Intent(this, BayarTukarTambahActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("O2_ID", id)
            startActivity(intent)
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

    fun detailTransaksi() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        ApiConfig.instanceRetrofit.getTransaksiTDetail(id).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if (response.code == 200) {
                    txtTglNotaTt.text = response.tukartambah.tanggal
                    txtNoNotaTt.text = response.tukartambah.nomor_nota

                    Picasso.get().load(urip+response.product.foto).into(imageProdukBaruNotaTt)
                    txtNamaProdukBaruNotaTt.text = response.product.nama
                    val formatter: NumberFormat = DecimalFormat("#,###")
                    val formattedNumber: String = formatter.format(response.product.harga)
                    txtHargaProdukBaruNotaTt.text = "Rp."+formattedNumber

                    Picasso.get().load(urif+response.flagship.foto).into(imageProdukLamaNotaTt)
                    txtNamaProdukLamaNotaTt.text = response.flagship.nama
                    txtBrandProdukLamaNotaTt.text = response.flagship.brand

                    if (response.tukartambah.status == 0) {
                        imgStatusNotaTt.setImageResource(R.drawable.ic_baseline_access_time_filled_24)
                        imgStatusNotaTt.setColorFilter(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                        txtStatusNotaTt.text = "Menunggu persetujuan"
                        txtKeteranganNotaTt.text = "Mohon ditunggu, tim kami sedang mengecek permintaan kamu!"
                        txtHargaNotaTt.text = "Belum diterbitkan"
                        txtResiNotaTt.text = "Belum diterbitkan"
                        btnBayarTt.visibility = View.GONE
                    } else if (response.tukartambah.status == 1) {
                        imgStatusNotaTt.setImageResource(R.drawable.ic_baseline_check_circle_24)
                        imgStatusNotaTt.setColorFilter(ContextCompat.getColor(applicationContext, R.color.check))
                        txtStatusNotaTt.text = "Disetujui"
                        txtKeteranganNotaTt.text = "Permintaanmu sudah disetujui, harap segera lakukan pembayaran!"
                        val formattedNumber1: String = formatter.format(response.tukartambahdetail.selisih)
                        txtHargaNotaTt.text = "Rp."+formattedNumber1
                        txtResiNotaTt.text = "Belum diterbitkan"
                    } else if (response.tukartambah.status == 2) {
                        imgStatusNotaTt.setImageResource(R.drawable.ic_baseline_cancel_24)
                        imgStatusNotaTt.setColorFilter(ContextCompat.getColor(applicationContext, R.color.cancel))
                        txtStatusNotaTt.text = "Ditolak"
                        txtKeteranganNotaTt.text = "Terdapat kendala pada barangmu."
                        txtHargaNotaTt.text = "-"
                        txtResiNotaTt.text = "-"
                        btnBayarTt.visibility = View.GONE
                        cardChat.visibility = View.GONE
                    } else if (response.tukartambah.status == 3) {
                        imgStatusNotaTt.setImageResource(R.drawable.ic_saldo)
                        txtStatusNotaTt.text = "Sudah dibayar"
                        txtKeteranganNotaTt.text = "Pembayaran sudah kami terima, barangmu akan segera dikirim!"
                        val formattedNumber1: String = formatter.format(response.tukartambahdetail.selisih)
                        txtHargaNotaTt.text = "Rp."+formattedNumber1
                        txtResiNotaTt.text = "Belum diterbitkan"
                        btnBayarTt.visibility = View.GONE
                    } else if (response.tukartambah.status == 4) {
                        imgStatusNotaTt.setImageResource(R.drawable.ic_baseline_delivery_dining_24)
                        imgStatusNotaTt.setColorFilter(ContextCompat.getColor(applicationContext, R.color.colorPrimaryVariant))
                        txtStatusNotaTt.text = "Dalam perjalanan"
                        txtKeteranganNotaTt.text = "Pembayaran sudah kami terima, barangmu akan segera dikirim!"
                        val formattedNumber1: String = formatter.format(response.tukartambahdetail.selisih)
                        txtHargaNotaTt.text = "Rp."+formattedNumber1
                        txtResiNotaTt.text = response.tukartambah.resi
                        btnBayarTt.visibility = View.GONE
                    } else {
                        imgStatusNotaTt.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)
                        imgStatusNotaTt.setColorFilter(ContextCompat.getColor(applicationContext, R.color.check))
                        txtStatusNotaTt.text = "Selesai"
                        txtKeteranganNotaTt.text = "Transaksi telah selesai."
                        val formattedNumber1: String = formatter.format(response.tukartambahdetail.selisih)
                        txtHargaNotaTt.text = "Rp."+formattedNumber1
                        txtResiNotaTt.text = response.tukartambah.resi
                        btnBayarTt.visibility = View.GONE
                        cardChat.visibility = View.GONE
                    }
                } else {
                    Toast.makeText(this@TukarTambahDetailActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@TukarTambahDetailActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}