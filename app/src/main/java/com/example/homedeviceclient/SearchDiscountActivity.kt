package com.example.homedeviceclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homedeviceclient.adapter.SearchAdapter
import com.example.homedeviceclient.adapter.SearchDiscountAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.model.Product
import kotlinx.android.synthetic.main.activity_search_discount.*
import kotlinx.android.synthetic.main.activity_search_kategori.*
import kotlinx.android.synthetic.main.activity_search_kategori.txtProductFindKtg
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchDiscountActivity : AppCompatActivity() {
    var listProducts: ArrayList<Product> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_discount)

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        btnSearchDc.setOnClickListener {
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
        pbA154r.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.searchProductDc(txtProductFindDc.text.toString()).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                pbA154r.visibility = View.GONE
                val response = response.body()!!
                if(response.code == 200) {
                    listProducts = response.products
                    updateList()
                } else {
                    Toast.makeText(this@SearchDiscountActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                pbA154r.visibility = View.GONE
                Toast.makeText(this@SearchDiscountActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateList() {
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        searchDcView.layoutManager = layout
        searchDcView.setHasFixedSize(true)
        searchDcView.adapter = SearchDiscountAdapter(listProducts)
    }
}