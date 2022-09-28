package com.example.homedeviceclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homedeviceclient.adapter.BundlingItemAdapter
import com.example.homedeviceclient.adapter.KodeDimilikiAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.example.homedeviceclient.model.BundlingItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_bundling.*
import kotlinx.android.synthetic.main.activity_detail_flagship.*
import kotlinx.android.synthetic.main.activity_kode_promo.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat

class DetailBundlingActivity : AppCompatActivity() {
    var id = ""
    var uri = "http://192.168.0.104/ta/tugasakhir/public/bundling_images/"
    lateinit var sp: SharedPrefs
    var listBundleItem = ArrayList<BundlingItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_bundling)

        sp = SharedPrefs(this)

        id = intent.getStringExtra("B1_ID").toString()

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        detailProduct()

        btnAtcB.setOnClickListener {
            val user = sp.getUser()
            if (user != null) {
                val intent = Intent(this, CheckoutBundlingActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("B11_ID", id)
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
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
        ApiConfig.instanceRetrofit.bundlingDetails(id).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    Picasso.get().load(uri+response.bundling.foto).into(imageBDetail)
                    txtNamaBDetail.text = response.bundling.nama
                    val formatter: NumberFormat = DecimalFormat("#,###")
                    val formattedNumber: String = formatter.format(response.total)
                    txtHargaBDetail.text = "Rp."+formattedNumber
                    val formattedNumber2: String = formatter.format(response.bundling.harga)
                    txtHargaDiskonDetailB.text = "Rp."+formattedNumber2
                    txtDecBDetail.text = response.bundling.deskripsi
                    txtTglBDetail.text = response.bundling.masa_berlaku
                    listBundleItem = response.bundle_item
                    updateList()
                } else {
                    Toast.makeText(this@DetailBundlingActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@DetailBundlingActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateList() {
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        bundleItemView.layoutManager = layout
        bundleItemView.setHasFixedSize(true)
        bundleItemView.adapter = BundlingItemAdapter(listBundleItem)
    }
}