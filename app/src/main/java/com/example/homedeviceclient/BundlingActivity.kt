package com.example.homedeviceclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.homedeviceclient.adapter.BundlingAdapter
import com.example.homedeviceclient.adapter.MerkAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.model.Bundling
import com.example.homedeviceclient.model.Merk
import kotlinx.android.synthetic.main.activity_all_merk.*
import kotlinx.android.synthetic.main.activity_bundling.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BundlingActivity : AppCompatActivity() {
    var listBundle:ArrayList<Bundling> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bundling)

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        getAllBundle()
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

    private fun getAllBundle() {
        ApiConfig.instanceRetrofit.getBundling().enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    listBundle = response.bundlings
                    updateList()
                } else {
                    Toast.makeText(this@BundlingActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@BundlingActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateList() {
        val sg = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        bundlingView.layoutManager = sg
        bundlingView.setHasFixedSize(true)
        bundlingView.adapter = BundlingAdapter(listBundle)
    }
}