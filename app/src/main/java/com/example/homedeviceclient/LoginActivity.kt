package com.example.homedeviceclient

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btn_kembali
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var sp:SharedPrefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sp = SharedPrefs(this)

        btn_kembali.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        btn_signup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btn_login.setOnClickListener {
            login()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun login() {
        if (txtEmail.text!!.isEmpty()) {
            txtEmail.error = "Kolom e-mail tidak boleh kosong"
            txtEmail.requestFocus()
            return
        } else if (txtPassword.text!!.isEmpty()) {
            txtPassword.error = "Kolom password tidak boleh kosong"
            txtPassword.requestFocus()
            return
        }

        pbLogin.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.login(txtEmail.text.toString(), txtPassword.text.toString()).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                pbLogin.visibility = View.GONE
                if (response.body()!!.code == 200) {
                    sp.setLogin(true)
                    sp.setUser(response.body()!!.user)
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@LoginActivity, "Selamat Datang "+response.body()!!.user.name, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LoginActivity, "Kesalahan : "+response.body()!!.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                pbLogin.visibility = View.GONE
                Toast.makeText(this@LoginActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}