package com.example.homedeviceclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homedeviceclient.adapter.AlamatAdapter
import com.example.homedeviceclient.adapter.OngoingSaldoAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.RealPathUtil
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.example.homedeviceclient.model.WalletRequest
import kotlinx.android.synthetic.main.activity_alamat.*
import kotlinx.android.synthetic.main.activity_ongoing_saldo.*
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OngoingSaldoActivity : AppCompatActivity() {
    var listWalletreqs: ArrayList<WalletRequest> = ArrayList()
    var view: View? = null
    lateinit var sp: SharedPrefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ongoing_saldo)

        sp = SharedPrefs(this)

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        getOngoingSaldo()
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

    private fun getOngoingSaldo() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        pbOg.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.getWalletOngoing(user.email).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                pbOg.visibility = View.GONE
                if(response.code == 200) {
                    listWalletreqs = response.walletreqs
                    updateList()
                } else {
                    Toast.makeText(this@OngoingSaldoActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                pbOg.visibility = View.GONE
                Toast.makeText(this@OngoingSaldoActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateList() {
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        ongoingView.setLayoutManager(layout)
        ongoingView.setHasFixedSize(true)
        ongoingView.adapter = OngoingSaldoAdapter(listWalletreqs)
    }
}