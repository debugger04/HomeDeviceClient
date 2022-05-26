package com.example.homedeviceclient

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import kotlinx.android.synthetic.main.activity_ganti_password.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btn_kembali
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GantiPasswordActivity : AppCompatActivity() {
    lateinit var sp: SharedPrefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ganti_password)
        sp = SharedPrefs(this)

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        btn_ganti.setOnClickListener {
            gantiPassword()
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

    fun gantiPassword() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        if (txtOldPass.text!!.isEmpty()) {
            txtOldPass.error = "Kolom password lama tidak boleh kosong"
            txtOldPass.requestFocus()
            return
        } else if (txtNewPass.text!!.isEmpty()) {
            txtNewPass.error = "Kolom password baru tidak boleh kosong"
            txtNewPass.requestFocus()
            return
        } else if (txtUNewPass.text!!.isEmpty()) {
            txtUNewPass.error = "Kolom ulang password baru tidak boleh kosong"
            txtUNewPass.requestFocus()
            return
        }

        pbGp.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.gantiPassword(user.email, txtOldPass.text.toString(), txtNewPass.text.toString(), txtUNewPass.text.toString()).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                pbGp.visibility = View.GONE
                if (response.body()!!.code == 200) {
                    onBackPressed()
                    Toast.makeText(this@GantiPasswordActivity, "Password telah berhasil diubah!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@GantiPasswordActivity, "Kesalahan : "+response.body()!!.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                pbGp.visibility = View.GONE
                Toast.makeText(this@GantiPasswordActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}