package com.example.homedeviceclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homedeviceclient.adapter.LogWalletAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.example.homedeviceclient.model.LogWallet
import kotlinx.android.synthetic.main.activity_log_wallet.*
import kotlinx.android.synthetic.main.activity_ongoing_saldo.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogWalletActivity : AppCompatActivity() {
    var listLogWallet: ArrayList<LogWallet> = ArrayList()
    lateinit var sp: SharedPrefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_wallet)

        sp = SharedPrefs(this)

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        getLogWallet()
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

    private fun getLogWallet() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        pbLg.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.getLogWallet(user.email).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                pbLg.visibility = View.GONE
                val response = response.body()!!
                if(response.code == 200) {
                    listLogWallet = response.logwallets
                    updateList()
                } else {
                    Toast.makeText(this@LogWalletActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                pbLg.visibility = View.GONE
                Toast.makeText(this@LogWalletActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateList() {
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        logWalletView.layoutManager = layout
        logWalletView.setHasFixedSize(true)
        logWalletView.adapter = LogWalletAdapter(listLogWallet)
    }
}