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
import com.example.homedeviceclient.adapter.MembershipAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.example.homedeviceclient.model.LogWallet
import com.example.homedeviceclient.model.Membership
import kotlinx.android.synthetic.main.activity_detail_membership.*
import kotlinx.android.synthetic.main.activity_log_wallet.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMembershipActivity : AppCompatActivity() {
    var listMemberships: ArrayList<Membership> = ArrayList()
    var tipe: String = ""
    var member_id = 0
    lateinit var sp: SharedPrefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_membership)

        sp = SharedPrefs(this)

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        memberships()
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

    private fun memberships() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        pb987.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.getMembership(user.email).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                pb987.visibility = View.GONE
                val response = response.body()!!
                if(response.code == 200) {
                    listMemberships = response.memberships
                    tipe = response.membersettings.tipe
                    member_id = response.member_id
                    updateList()
                } else {
                    Toast.makeText(this@DetailMembershipActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                pb987.visibility = View.GONE
                Toast.makeText(this@DetailMembershipActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateList() {
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        membershipView.layoutManager = layout
        membershipView.setHasFixedSize(true)
        membershipView.adapter = MembershipAdapter(listMemberships, tipe, member_id)
    }
}