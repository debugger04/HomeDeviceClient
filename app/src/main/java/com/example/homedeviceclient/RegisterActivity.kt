package com.example.homedeviceclient

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.btn_kembali
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    lateinit var sp: SharedPrefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sp = SharedPrefs(this)

        btn_kembali.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        txtDatePickerReg.setOnClickListener {
            var today = Calendar.getInstance()
            var year = today.get(Calendar.YEAR)
            var month = today.get(Calendar.MONTH)
            var day = today.get(Calendar.DAY_OF_MONTH)

            var picker = DatePickerDialog(this,
                { datePicker, selYear, selMonth, selDay ->
                    val calendar = Calendar.getInstance()
                    calendar.set(selYear, selMonth, selDay)
                    var dateFormat = SimpleDateFormat("dd MMMM yyyy")
                    var str = dateFormat.format(calendar.time)
                    txtDatePickerReg.setText(str)
            }, year, month, day)

            picker.show()
        }

        btn_register.setOnClickListener {
            register()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun register() {
        if (txtNamaReg.text!!.isEmpty()) {
            txtNamaReg.error = "Kolom nama tidak boleh kosong"
            txtNamaReg.requestFocus()
            return
        } else if (txtEmailReg.text!!.isEmpty()) {
            txtEmailReg.error = "Kolom e-mail tidak boleh kosong"
            txtEmailReg.requestFocus()
            return
        } else if (txtDatePickerReg.text!!.isEmpty()) {
            txtDatePickerReg.error = "Kolom tanggal lahir tidak boleh kosong"
            txtDatePickerReg.requestFocus()
            return
        } else if (txtPasswordReg.text!!.isEmpty()) {
            txtPasswordReg.error = "Kolom password tidak boleh kosong"
            txtPasswordReg.requestFocus()
            return
        }

        pbReg.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.register(txtNamaReg.text.toString(), txtEmailReg.text.toString(), txtPasswordReg.text.toString(), txtDatePickerReg.text.toString()).enqueue(object : Callback<ResponseModel>{
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                pbReg.visibility = View.GONE
                if (response.body()!!.code == 200) {
                    sp.setLogin(true)
                    sp.setUser(response.body()!!.user)
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@RegisterActivity, "Selamat Datang "+response.body()!!.user.name, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@RegisterActivity, "Kesalahan : "+response.body()!!.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                pbReg.visibility = View.GONE
                Toast.makeText(this@RegisterActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}