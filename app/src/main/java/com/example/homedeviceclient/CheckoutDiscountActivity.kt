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
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_checkout_discount.*
import kotlinx.android.synthetic.main.activity_checkout_discount.btn_tambahAlamat
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlinx.android.synthetic.main.activity_detail_tukar_tambah.*
import kotlinx.android.synthetic.main.activity_detail_tukar_tambah.imageTtDetail
import kotlinx.android.synthetic.main.activity_detail_tukar_tambah.txtBarTtDetail
import kotlinx.android.synthetic.main.activity_detail_tukar_tambah.txtDecTtDetail
import kotlinx.android.synthetic.main.activity_detail_tukar_tambah.txtHargaTtDetail
import kotlinx.android.synthetic.main.activity_detail_tukar_tambah.txtMerkTtDetail
import kotlinx.android.synthetic.main.activity_detail_tukar_tambah.txtNamaTtDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class CheckoutDiscountActivity : AppCompatActivity() {
    var id = ""
    var uri = "http://192.168.0.104/ta/tugasakhir/public/product_images/"
    lateinit var sp: SharedPrefs
    var cek_koin = 0
    var totalBayar = 0
    val ongkir = 10000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_discount)

        sp = SharedPrefs(this)

        sp.setAlamat(null)

        id = intent.getStringExtra("P72_ID").toString()

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        getWallet()
        alamatUtama()
        detailProduct()

        tv_ongkirCkDsc.text = "Rp.10,000"

        val c = Calendar.getInstance()
        System.out.println("Current time => " + c.time)

        val df = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate = df.format(c.time)

        txtTglDsc.text = formattedDate

        btn_tambahAlamat.setOnClickListener {
            val intent = Intent(this, ListAlamatActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        checkPoinX.setOnClickListener {
            if (checkPoinX.isChecked) {
                cek_koin = 1
            } else {
                cek_koin = 0
            }
        }

        btnBayarA.setOnClickListener {
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

                    txtSaldo7.text = "Rp. "+formattedNumber
                    txtPoin7.text = formattedNumber2
                } else {
                    Toast.makeText(this@CheckoutDiscountActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@CheckoutDiscountActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
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
            div_alamatA.visibility = View.GONE
            div_kosongA.visibility = View.VISIBLE
        } else {
            val alamat = sp.getAlamat()!!
            div_alamatA.visibility = View.VISIBLE
            div_kosongA.visibility = View.GONE
            tv_namaA.text = alamat.nama
            tv_phoneA.text = alamat.no_telp
            tv_alamatA.text =alamat.jalan + ", RT." + alamat.rt + "/RW." + alamat.rw + ", " + alamat.kecamatan + ", " + alamat.kab_kota + ", " + alamat.provinsi
        }
    }

    private fun detailProduct() {
        ApiConfig.instanceRetrofit.productDetails(id).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    Picasso.get().load(uri+response.product.foto).into(imageCkDsc)
                    txtNamaCkDsc.text = response.product.nama
                    val formatter: NumberFormat = DecimalFormat("#,###")
                    val formattedNumber: String = formatter.format(response.product.harga)
                    txtHargaCkDsc.text = "Rp."+formattedNumber
                    tv_totalCkDsc.text = "Rp."+formattedNumber
                    var totalDsc = response.product.harga * response.nilai / 100
                    val formattedNumber2: String = formatter.format(totalDsc)
                    tv_promoCkDsc.text = "Rp."+formattedNumber2
                    var final = response.product.harga - totalDsc + ongkir
                    val formattedNumber3: String = formatter.format(final)
                    tv_finalCkDsc.text = "Rp."+formattedNumber3
                    totalBayar = final
                } else {
                    Toast.makeText(this@CheckoutDiscountActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@CheckoutDiscountActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
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

        if (sp.getAlamat() == null) {
            Toast.makeText(this@CheckoutDiscountActivity, "Pilih alamat terlebih dahulu !", Toast.LENGTH_SHORT).show()
            return
        } else {
            val alamat = sp.getAlamat()!!

            ApiConfig.instanceRetrofit.discPaid(user.email, alamat.id.toString(), id, totalBayar.toString(), cek_koin.toString()).enqueue(object :
                Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    val response = response.body()!!
                    if(response.code == 200) {
                        val intent = Intent(this@CheckoutDiscountActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                        Toast.makeText(this@CheckoutDiscountActivity, response.msg, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@CheckoutDiscountActivity, response.msg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(this@CheckoutDiscountActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}