package com.example.homedeviceclient

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homedeviceclient.adapter.BundlingItemAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.example.homedeviceclient.model.BundlingItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_checkout_bundling.*
import kotlinx.android.synthetic.main.activity_checkout_discount.*
import kotlinx.android.synthetic.main.activity_detail_bundling.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class CheckoutBundlingActivity : AppCompatActivity() {
    var id = ""
    lateinit var sp: SharedPrefs
    var cek_koin = 0
    var totalBayar = 0
    val ongkir = 10000
    var listBundleItem = ArrayList<BundlingItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_bundling)

        sp = SharedPrefs(this)

        sp.setAlamat(null)

        id = intent.getStringExtra("B11_ID").toString()

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        tv_ongkirB.text = "Rp.10,000"

        val c = Calendar.getInstance()
        System.out.println("Current time => " + c.time)

        val df = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate = df.format(c.time)

        txtTglB.text = formattedDate

        detailProduct()
        getWallet()
        alamatUtama()

        btn_tambahAlamatB.setOnClickListener {
            val intent = Intent(this, ListAlamatActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        checkPoinB.setOnClickListener {
            if (checkPoinB.isChecked) {
                cek_koin = 1
            } else {
                cek_koin = 0
            }
        }

        btnBayarB.setOnClickListener {
            bayar()
        }
    }

    override fun onResume() {
        super.onResume()
        alamatUtama()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setCancelable(true)
                builder.setTitle("Batalkan Transaksi")
                builder.setMessage("Yakin untuk batal melakukan transaksi ?")
                builder.setPositiveButton("Batal",
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                        sp.setAlamat(null)
                        finish()
                    })
                builder.setNegativeButton("Lanjut",
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })

                val dialog: AlertDialog = builder.create()
                dialog.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("Batalkan Transaksi")
        builder.setMessage("Yakin untuk batal melakukan transaksi ?")
        builder.setPositiveButton("Batal",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                sp.setAlamat(null)
                super.onBackPressed()
            })
        builder.setNegativeButton("Lanjut",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun detailProduct() {
        ApiConfig.instanceRetrofit.bundlingDetails(id).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    val formatter: NumberFormat = DecimalFormat("#,###")
                    val formattedNumber: String = formatter.format(response.total)
                    tv_totalBelanjaB.text = "Rp."+formattedNumber
                    var totals = response.bundling.harga + ongkir
                    val formattedNumber2: String = formatter.format(totals)
                    tv_totalB.text = "Rp."+formattedNumber2
                    var promo = response.total - response.bundling.harga
                    val formattedNumber3: String = formatter.format(promo)
                    tv_promoB.text = "-Rp."+formattedNumber3
                    listBundleItem = response.bundle_item
                    totalBayar = totals
                    updateList()
                } else {
                    Toast.makeText(this@CheckoutBundlingActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@CheckoutBundlingActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateList() {
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        cBundleView.layoutManager = layout
        cBundleView.setHasFixedSize(true)
        cBundleView.adapter = BundlingItemAdapter(listBundleItem)
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

                    txtSaldoB.text = "Rp. "+formattedNumber
                    txtPoinB.text = formattedNumber2
                } else {
                    Toast.makeText(this@CheckoutBundlingActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@CheckoutBundlingActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun alamatUtama() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        if (sp.getAlamat() == null) {
            div_alamatB.visibility = View.GONE
            div_kosongB.visibility = View.VISIBLE
        } else {
            val alamat = sp.getAlamat()!!
            div_alamatB.visibility = View.VISIBLE
            div_kosongB.visibility = View.GONE
            tv_namaB.text = alamat.nama
            tv_phoneB.text = alamat.no_telp
            tv_alamatB.text =alamat.jalan + ", RT." + alamat.rt + "/RW." + alamat.rw + ", " + alamat.kecamatan + ", " + alamat.kab_kota + ", " + alamat.provinsi
        }
    }

    private fun bayar() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        if (sp.getAlamat() == null) {
            Toast.makeText(this@CheckoutBundlingActivity, "Pilih alamat terlebih dahulu !", Toast.LENGTH_SHORT).show()
            return
        } else {
            val alamat = sp.getAlamat()!!

            ApiConfig.instanceRetrofit.bundlePaid(user.email, alamat.id.toString(), id, totalBayar.toString(), cek_koin.toString()).enqueue(object :
                Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    val response = response.body()!!
                    if(response.code == 200) {
                        val intent = Intent(this@CheckoutBundlingActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                        Toast.makeText(this@CheckoutBundlingActivity, response.msg, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@CheckoutBundlingActivity, response.msg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(this@CheckoutBundlingActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}