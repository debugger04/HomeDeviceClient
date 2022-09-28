package com.example.homedeviceclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.example.homedeviceclient.model.ProductLama
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_flagship.*
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

class DetailFlagshipActivity : AppCompatActivity() {
    lateinit var sp: SharedPrefs
    var id = ""
    var uri = "http://192.168.0.104/ta/tugasakhir/public/flagship_images/"
    var flags = ProductLama()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_flagship)

        sp = SharedPrefs(this)

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        id = intent.getStringExtra("P3_ID").toString()

        detailProduct()
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
        ApiConfig.instanceRetrofit.flagshipDetails(id).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    flags = response.flagship
                    Picasso.get().load(uri+flags.foto).into(imageFDetail)
                    txtNamaFDetail.text = flags.nama
                    val formatter: NumberFormat = DecimalFormat("#,###")
                    val formattedNumber: String = formatter.format(flags.harga_jual)
                    txtHargaFDetail.text = "Rp."+formattedNumber
                    txtDecFDetail.text = flags.deskripsi
                    txtMerkFDetail.text = flags.brand
                } else {
                    Toast.makeText(this@DetailFlagshipActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@DetailFlagshipActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}