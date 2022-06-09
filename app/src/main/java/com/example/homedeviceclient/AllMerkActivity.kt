package com.example.homedeviceclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.homedeviceclient.adapter.MerkAdapter
import com.example.homedeviceclient.adapter.ProductAdapter
import com.example.homedeviceclient.adapter.ProductDiscountAdapter
import com.example.homedeviceclient.adapter.ProductLamaAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.model.Merk
import kotlinx.android.synthetic.main.activity_all_merk.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllMerkActivity : AppCompatActivity() {
    var listBrand:ArrayList<Merk> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_merk)

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        getAllBrands()
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

    private fun getAllBrands() {
        ApiConfig.instanceRetrofit.getAllBrand().enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    listBrand = response.brands
                    updateList()
                } else {
                    Toast.makeText(this@AllMerkActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@AllMerkActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateList() {
        val sg = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        brandView.layoutManager = sg
        brandView.setHasFixedSize(true)
        brandView.adapter = MerkAdapter(listBrand)
    }
}