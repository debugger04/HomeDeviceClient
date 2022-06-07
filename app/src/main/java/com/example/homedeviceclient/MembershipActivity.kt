package com.example.homedeviceclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_membership.*
import kotlinx.android.synthetic.main.activity_ongoing_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat

class MembershipActivity : AppCompatActivity() {
    lateinit var sp: SharedPrefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_membership)

        sp = SharedPrefs(this)

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        getUserMember()

        btnSemua.setOnClickListener {
            val intent = Intent(this, DetailMembershipActivity::class.java)
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

    fun getUserMember() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        pb731.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.userMember(user.email).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                pb731.visibility = View.GONE
                val formatter: NumberFormat = DecimalFormat("#,###")
                val response = response.body()!!
                if (response.code == 200) {
                    txtNamaMember.text = user.name
                    txtLevel.text = response.member.nama
                    val formattedNumber2: String = formatter.format(response.member.total_poin)
                    txtTotalPoin.text = formattedNumber2
                    txtLvl.text = response.member.nama
                    if (response.membersettings.tipe == "DC") {
                        txtSettingsMember.text = "Diskon "+response.member.potongan+"% untuk setiap transaksi."
                    } else {
                        txtSettingsMember.text = "Cashback koin "+response.member.potongan+"% untuk setiap transaksi."
                    }
                    val formattedNumber: String = formatter.format(response.membersettings.poinperbelanja)
                    txtPenjelasSetting.text = "1 poin diperoleh setiap transaksi sebesar Rp."+formattedNumber+", berlaku kelipatan"
                    if (response.msg == "NN") {
                        txtNextLevel.text = "Kamu sudah berada di tingkat Member paling tinggi!"
                    } else {
                        val formattedNumber4: String = formatter.format(response.nextmember.minimal_poin)
                        if (response.membersettings.tipe == "DC") {
                            txtNextLevel.text = response.nextmember.nama+"\n"+formattedNumber4+" poin\nDiskon "+response.nextmember.potongan+"% untuk setiap transaksi."
                        } else {
                            txtNextLevel.text = response.nextmember.nama+"\n"+formattedNumber4+" poin\nCashback poin sebesar "+response.nextmember.potongan+"% untuk setiap transaksi."
                        }
                    }
                } else {
                    Toast.makeText(this@MembershipActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                pb731.visibility = View.GONE
                Toast.makeText(this@MembershipActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}