package com.example.homedeviceclient

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import kotlinx.android.synthetic.main.activity_detail_user.*
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DetailUserActivity : AppCompatActivity() {
    lateinit var sp:SharedPrefs
    override fun onCreate(savedInstanceState: Bundle?) {
        sp = SharedPrefs(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        init()

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        txtDatePickerProfil.setOnClickListener {
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

        btn_renew.setOnClickListener {
            detailProfil()
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

    private fun init() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        txtNamaProfil.setText(user.name)
        txtDatePickerProfil.setText(user.date_of_birth)
    }

    fun detailProfil() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        if (txtNamaProfil.text!!.isEmpty()) {
            txtNamaProfil.error = "Kolom nama tidak boleh kosong"
            txtNamaProfil.requestFocus()
            return
        } else if (txtDatePickerProfil.text!!.isEmpty()) {
            txtDatePickerProfil.error = "Kolom tanggal lahir tidak boleh kosong"
            txtDatePickerProfil.requestFocus()
            return
        }

        pbDu.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.detailProfil(user.email, txtNamaProfil.text.toString(), txtDatePickerProfil.text.toString()).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                pbDu.visibility = View.GONE
                if (response.body()!!.code == 200) {
                    sp.setUser(response.body()!!.user)
                    val intent = Intent(this@DetailUserActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@DetailUserActivity, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@DetailUserActivity, "Kesalahan : "+response.body()!!.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                pbDu.visibility = View.GONE
                Toast.makeText(this@DetailUserActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}