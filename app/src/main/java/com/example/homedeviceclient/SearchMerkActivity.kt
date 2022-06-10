package com.example.homedeviceclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homedeviceclient.adapter.SearchAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.model.Product
import kotlinx.android.synthetic.main.activity_search_kategori.*
import kotlinx.android.synthetic.main.activity_search_merk.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchMerkActivity : AppCompatActivity() {
    var listProducts: ArrayList<Product> = ArrayList()
    var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_merk)

        id = intent.getStringExtra("M1_ID").toString()

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        btnSearchBrd.setOnClickListener {
            searchProduk()
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

    private fun searchProduk() {
        pbA1541.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.searchProductBrd(txtProductFindBrd.text.toString(), id).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                pbA1541.visibility = View.GONE
                val response = response.body()!!
                if(response.code == 200) {
                    listProducts = response.products
                    updateList()
                } else {
                    Toast.makeText(this@SearchMerkActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                pbA1541.visibility = View.GONE
                Toast.makeText(this@SearchMerkActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateList() {
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        searchBrdView.layoutManager = layout
        searchBrdView.setHasFixedSize(true)
        searchBrdView.adapter = SearchAdapter(listProducts)
    }
}