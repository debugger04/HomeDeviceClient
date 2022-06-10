package com.example.homedeviceclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.homedeviceclient.adapter.ProductAdapter
import com.example.homedeviceclient.adapter.ProductLamaAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.model.ProductLama
import kotlinx.android.synthetic.main.activity_all_flagship.*
import kotlinx.android.synthetic.main.activity_detail_merk.*
import kotlinx.android.synthetic.main.activity_search_merk.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllFlagshipActivity : AppCompatActivity() {
    var listFlagship:ArrayList<ProductLama> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_flagship)

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        getAllFlagship()

        btnSearch2.setOnClickListener {
            val intent = Intent(this, SearchFlagshipActivity::class.java)
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

    private fun getAllFlagship() {
        pbA1544.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.getAllFlagship().enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                pbA1544.visibility = View.GONE
                val response = response.body()!!
                if(response.code == 200) {
                    listFlagship = response.flagships
                    updateList()
                } else {
                    Toast.makeText(this@AllFlagshipActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                pbA1544.visibility = View.GONE
                Toast.makeText(this@AllFlagshipActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateList() {
        val sg = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        allFlagshipView.layoutManager = sg
        allFlagshipView.setHasFixedSize(true)
        allFlagshipView.adapter = ProductLamaAdapter(listFlagship)
    }
}