package com.example.homedeviceclient

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
import kotlinx.android.synthetic.main.activity_detail_alamat.*
import kotlinx.android.synthetic.main.activity_detail_alamat.txtJalan
import kotlinx.android.synthetic.main.activity_detail_alamat.txtKabKota
import kotlinx.android.synthetic.main.activity_detail_alamat.txtKecamatan
import kotlinx.android.synthetic.main.activity_detail_alamat.txtNamaPenerima
import kotlinx.android.synthetic.main.activity_detail_alamat.txtNtelp
import kotlinx.android.synthetic.main.activity_detail_alamat.txtProvinsi
import kotlinx.android.synthetic.main.activity_detail_alamat.txtRt
import kotlinx.android.synthetic.main.activity_detail_alamat.txtRw
import kotlinx.android.synthetic.main.activity_detail_alamat.txtTag
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailAlamatActivity : AppCompatActivity() {
    lateinit var sp: SharedPrefs
    var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_alamat)

        sp = SharedPrefs(this)

        id = intent.getStringExtra("ALAMAT_ID").toString()

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        init()

        btn_update.setOnClickListener {
            renewAlamat()
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

    fun init() {
        ApiConfig.instanceRetrofit.getAlamatDetail(id).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.body()!!.code == 200) {
                    val alamat = response.body()!!.alamat
                    txtNamaPenerima.setText(alamat.nama)
                    txtTag.setText(alamat.tag)
                    txtNtelp.setText(alamat.no_telp)
                    txtJalan.setText(alamat.jalan)
                    txtRt.setText(alamat.rt)
                    txtRw.setText(alamat.rw)
                    txtKecamatan.setText(alamat.kecamatan)
                    txtKabKota.setText(alamat.kab_kota)
                    txtProvinsi.setText(alamat.provinsi)
                } else {
                    Toast.makeText(this@DetailAlamatActivity, "Kesalahan : "+response.body()!!.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@DetailAlamatActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun renewAlamat() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        if (txtNamaPenerima.text!!.isEmpty()) {
            txtNamaPenerima.error = "Kolom nama penerima tidak boleh kosong"
            txtNamaPenerima.requestFocus()
            return
        } else if (txtTag.text!!.isEmpty()) {
            txtTag.error = "Kolom tag tidak boleh kosong"
            txtTag.requestFocus()
            return
        } else if (txtNtelp.text!!.isEmpty()) {
            txtNtelp.error = "Kolom nomor telepon tidak boleh kosong"
            txtNtelp.requestFocus()
            return
        } else if (txtJalan.text!!.isEmpty()) {
            txtJalan.error = "Kolom alamat tidak boleh kosong"
            txtJalan.requestFocus()
            return
        } else if (txtRt.text!!.isEmpty()) {
            txtRt.error = "Kolom nomor RT tidak boleh kosong"
            txtRt.requestFocus()
            return
        }  else if (txtRw.text!!.isEmpty()) {
            txtRw.error = "Kolom nomor RW tidak boleh kosong"
            txtRw.requestFocus()
            return
        }  else if (txtKecamatan.text!!.isEmpty()) {
            txtKecamatan.error = "Kolom nama kecamatan tidak boleh kosong"
            txtKecamatan.requestFocus()
            return
        }  else if (txtKabKota.text!!.isEmpty()) {
            txtKabKota.error = "Kolom nama kabupaten/kota tidak boleh kosong"
            txtKabKota.requestFocus()
            return
        }  else if (txtProvinsi.text!!.isEmpty()) {
            txtProvinsi.error = "Kolom nama provinsi tidak boleh kosong"
            txtProvinsi.requestFocus()
            return
        }

        pbAaa.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.updateAlamat(id, user.email, txtNamaPenerima.text.toString(), txtTag.text.toString(), txtJalan.text.toString(), txtRt.text.toString(), txtRw.text.toString(), txtKecamatan.text.toString(), txtKabKota.text.toString(), txtProvinsi.text.toString(), txtNtelp.text.toString()).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                pbAaa.visibility = View.GONE
                if (response.body()!!.code == 200) {
                    val intent = Intent(this@DetailAlamatActivity, AlamatActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@DetailAlamatActivity, "Alamat telah diubah!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@DetailAlamatActivity, "Kesalahan : "+response.body()!!.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                pbAaa.visibility = View.GONE
                Toast.makeText(this@DetailAlamatActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}