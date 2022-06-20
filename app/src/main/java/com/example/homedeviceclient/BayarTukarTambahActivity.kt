package com.example.homedeviceclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_bayar_tukar_tambah.*
import kotlinx.android.synthetic.main.activity_tukar_tambah_detail.*
import kotlinx.android.synthetic.main.activity_tukar_tambah_detail.imageProdukBaruNotaTt
import kotlinx.android.synthetic.main.activity_tukar_tambah_detail.imageProdukLamaNotaTt
import kotlinx.android.synthetic.main.activity_tukar_tambah_detail.txtBrandProdukLamaNotaTt
import kotlinx.android.synthetic.main.activity_tukar_tambah_detail.txtHargaProdukBaruNotaTt
import kotlinx.android.synthetic.main.activity_tukar_tambah_detail.txtNamaProdukBaruNotaTt
import kotlinx.android.synthetic.main.activity_tukar_tambah_detail.txtNamaProdukLamaNotaTt
import kotlinx.android.synthetic.main.activity_tukar_tambah_detail.txtNoNotaTt
import kotlinx.android.synthetic.main.activity_tukar_tambah_detail.txtTglNotaTt
import kotlinx.android.synthetic.main.activity_wallet.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat

class BayarTukarTambahActivity : AppCompatActivity() {
    lateinit var sp: SharedPrefs
    var id = ""
    var urip = "http://192.168.0.104/ta/tugasakhir/public/product_images/"
    var urif = "http://192.168.0.104/ta/tugasakhir/public/flagship_images/"
    var cekKoin = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bayar_tukar_tambah)

        sp = SharedPrefs(this)

        id = intent.getStringExtra("O2_ID").toString()

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        detailTransaksi()

        getWallet()

        checkPoin.setOnClickListener {
            if (checkPoin.isChecked) {
                cekKoin = 1
            } else {
                cekKoin = 0
            }
        }

        btnBayarTtNow.setOnClickListener {
            bayar()
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
                    txtTglNotaTt2.text = response.tukartambah.tanggal
                    txtNoNotaTt2.text = response.tukartambah.nomor_nota

                    Picasso.get().load(urip+response.product.foto).into(imageProdukBaruNotaTt2)
                    txtNamaProdukBaruNotaTt2.text = response.product.nama
                    val formatter: NumberFormat = DecimalFormat("#,###")
                    val formattedNumber: String = formatter.format(response.product.harga)
                    txtHargaProdukBaruNotaTt2.text = "Rp."+formattedNumber

                    Picasso.get().load(urif+response.flagship.foto).into(imageProdukLamaNotaTt2)
                    txtNamaProdukLamaNotaTt2.text = response.flagship.nama
                    txtBrandProdukLamaNotaTt2.text = response.flagship.brand

                    val formattedNumber1: String = formatter.format(response.tukartambahdetail.selisih)
                    txtHargaDitambah.text = "Rp."+formattedNumber1
                } else {
                    Toast.makeText(this@BayarTukarTambahActivity, response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@BayarTukarTambahActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getWallet() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        ApiConfig.instanceRetrofit.getWallet(user.email).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    val formatter: NumberFormat = DecimalFormat("#,###")
                    val formattedNumber: String = formatter.format(response.wallet.saldo)
                    val formattedNumber2: String = formatter.format(response.wallet.poin)

                    txtSaldo2.text = "Rp. "+formattedNumber
                    txtPoin2.text = formattedNumber2
                } else {
                    Toast.makeText(this@BayarTukarTambahActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@BayarTukarTambahActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun bayar() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        ApiConfig.instanceRetrofit.tukarTambahBayar(user.email, id, cekKoin.toString()).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    val intent = Intent(this@BayarTukarTambahActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@BayarTukarTambahActivity, response.msg, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@BayarTukarTambahActivity, response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@BayarTukarTambahActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}