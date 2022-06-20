package com.example.homedeviceclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.homedeviceclient.adapter.MerkAdapter
import com.example.homedeviceclient.adapter.ProductAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.example.homedeviceclient.model.Merk
import com.example.homedeviceclient.model.Product
import kotlinx.android.synthetic.main.activity_all_merk.*
import kotlinx.android.synthetic.main.activity_all_product.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllProductActivity : AppCompatActivity() {
    var listProduct:ArrayList<Product> = ArrayList()
    lateinit var sp:SharedPrefs
    var email = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_product)

        sp = SharedPrefs(this)

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        getAllProduk()

        btnSearch1.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
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
        ApiConfig.instanceRetrofit.getAllProduct().enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    listProduct = response.products
                    updateList()
                } else {
                    Toast.makeText(this@AllProductActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@AllProductActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateList() {
        val user = sp.getUser()
        if (user != null) {
            email = user.email
        }
        val sg = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        allProductView.layoutManager = sg
        allProductView.setHasFixedSize(true)
        allProductView.adapter = ProductAdapter(listProduct, email)
    }
}