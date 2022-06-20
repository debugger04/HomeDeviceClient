package com.example.homedeviceclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_tukar_tambah.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat

class DetailTukarTambahActivity : AppCompatActivity() {
    var id = ""
    var uri = "http://192.168.0.104/ta/tugasakhir/public/product_images/"
    lateinit var sp: SharedPrefs
    var email = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tukar_tambah)

        sp = SharedPrefs(this)

        id = intent.getStringExtra("P7_ID").toString()

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        detailProduct()

        btnGoTt.setOnClickListener {
            val intent = Intent(this, PengajuanTukarTambahActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("TT1_ID", id)
            startActivity(intent)
        }

        btnAtcTT.setOnClickListener {
            addToCart()
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

    private fun detailProduct() {
        ApiConfig.instanceRetrofit.productDetails(id).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    Picasso.get().load(uri+response.product.foto).into(imageTtDetail)
                    txtNamaTtDetail.text = response.product.nama
                    txtBarTtDetail.text = response.product.barcode
                    val formatter: NumberFormat = DecimalFormat("#,###")
                    val formattedNumber: String = formatter.format(response.product.harga)
                    txtHargaTtDetail.text = "Rp."+formattedNumber
                    txtDecTtDetail.text = response.product.deskripsi
                    txtMerkTtDetail.text = response.brand.nama
                } else {
                    Toast.makeText(this@DetailTukarTambahActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@DetailTukarTambahActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addToCart() {
        val user = sp.getUser()
        if (user != null) {
            email = user.email
            ApiConfig.instanceRetrofit.addCart(email, id).enqueue(object :
                Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    val response = response.body()!!
                    if(response.code == 200) {
                        Toast.makeText(this@DetailTukarTambahActivity, "Berhasil tambah ke keranjang", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@DetailTukarTambahActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(this@DetailTukarTambahActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}