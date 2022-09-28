package com.example.homedeviceclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homedeviceclient.adapter.ListAlamatAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.example.homedeviceclient.model.Alamat
import kotlinx.android.synthetic.main.activity_alamat.alamatView
import kotlinx.android.synthetic.main.activity_alamat.fabAdd
import kotlinx.android.synthetic.main.activity_list_alamat.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListAlamatActivity : AppCompatActivity() {
    var listAlamat: ArrayList<Alamat> = ArrayList()
    lateinit var sp: SharedPrefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_alamat)

        sp = SharedPrefs(this)

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        fabAdd.setOnClickListener{
            val intent = Intent(this, AddAlamatActivity::class.java)
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

    override fun onResume() {
        super.onResume()

        getAlamat()
    }

    private fun getAlamat() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        ApiConfig.instanceRetrofit.getAlamat(user.email).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    txtKosong.visibility = View.GONE
                    listAlamat = response.alamats
                    updateList()
                } else {
                    txtKosong.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@ListAlamatActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateList() {
        var alamat = 0
        if (sp.getAlamat() == null) {
            alamat = 0
        } else {
            val a = sp.getAlamat()!!
            alamat = a.id
        }
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        alamatView.setLayoutManager(layout)
        alamatView.setHasFixedSize(true)
        alamatView.adapter = ListAlamatAdapter(listAlamat, alamat, object: ListAlamatAdapter.Listeners{
            override fun onClicked(data: Alamat) {
                sp.setAlamat(data)
                onBackPressed()
            }
        })
    }
}