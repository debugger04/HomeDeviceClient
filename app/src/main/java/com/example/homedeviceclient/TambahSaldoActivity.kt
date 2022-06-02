package com.example.homedeviceclient

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.RealPathUtil
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_tambah_saldo.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class TambahSaldoActivity : AppCompatActivity() {
    lateinit var sp: SharedPrefs
    var path:String = ""
    lateinit var rpu: RealPathUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_saldo)

        sp = SharedPrefs(this)

        rpu = RealPathUtil()

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        btn_choose.setOnClickListener {
            if(ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, 10)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
        }

        btn_send.setOnClickListener {
            addSaldo()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 10 && resultCode == Activity.RESULT_OK) {
            val filePath: Uri = data!!.data!!
            path = rpu.getRealPath(this, filePath).toString()
            val bitmap: Bitmap = BitmapFactory.decodeFile(path)
            imageView.setImageBitmap(bitmap)
        }
    }

    private fun addSaldo() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        if (txtJumlahSaldo.text!!.isEmpty()) {
            txtJumlahSaldo.error = "Kolom jumlah permintaan tidak boleh kosong"
            txtJumlahSaldo.requestFocus()
            return
        }

        val user = sp.getUser()!!

        var file: File = File(path)
        var fileReq: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        var body: MultipartBody.Part = MultipartBody.Part.createFormData("bukti_transfer", file.name, fileReq)

        var userEmail: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), user.email)
        var jumlahs: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), txtJumlahSaldo.text.toString())

        ApiConfig.instanceRetrofit.tambahSaldo(userEmail, jumlahs, body).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    val intent = Intent(this@TambahSaldoActivity, WalletActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@TambahSaldoActivity, "Berhasil melakukan permintaan", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@TambahSaldoActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@TambahSaldoActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}