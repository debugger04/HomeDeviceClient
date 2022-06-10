package com.example.homedeviceclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homedeviceclient.adapter.SearchAdapter
import com.example.homedeviceclient.adapter.SearchFlagshipAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.model.ProductLama
import kotlinx.android.synthetic.main.activity_search_flagship.*
import kotlinx.android.synthetic.main.activity_search_merk.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFlagshipActivity : AppCompatActivity() {
    var listFlagship:ArrayList<ProductLama> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_flagship)

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        btnSearchFlg.setOnClickListener {
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
        pbA154w.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.searchProductFlg(txtProductFindFlg.text.toString()).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                pbA154w.visibility = View.GONE
                val response = response.body()!!
                if(response.code == 200) {
                    listFlagship = response.flagships
                    updateList()
                } else {
                    Toast.makeText(this@SearchFlagshipActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                pbA154w.visibility = View.GONE
                Toast.makeText(this@SearchFlagshipActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateList() {
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        searchFlgView.layoutManager = layout
        searchFlgView.setHasFixedSize(true)
        searchFlgView.adapter = SearchFlagshipAdapter(listFlagship)
    }
}