package com.example.homedeviceclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.homedeviceclient.adapter.ProductAdapter
import com.example.homedeviceclient.adapter.ProductLamaAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.example.homedeviceclient.model.Product
import com.example.homedeviceclient.model.ProductLama
import kotlinx.android.synthetic.main.activity_all_product.*
import kotlinx.android.synthetic.main.activity_all_tukar_tambah.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllTukarTambahActivity : AppCompatActivity() {
    var listProduct:ArrayList<Product> = ArrayList()
    lateinit var sp: SharedPrefs
    var email = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_tukar_tambah)

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        getAllProduk()

        btnSearch5t.setOnClickListener {
            val intent = Intent(this, SearchTukarTambahActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
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

    private fun getAllProduk() {
        ApiConfig.instanceRetrofit.getAllTukarTambah().enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    listProduct = response.products
                    updateList()
                } else {
                    Toast.makeText(this@AllTukarTambahActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@AllTukarTambahActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateList() {
        val user = sp.getUser()
        if (user != null) {
            email = user.email
        }
        val sg = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        allTtView.layoutManager = sg
        allTtView.setHasFixedSize(true)
        allTtView.adapter = ProductAdapter(listProduct, email)
    }
}