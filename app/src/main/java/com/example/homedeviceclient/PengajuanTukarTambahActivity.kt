package com.example.homedeviceclient

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.RealPathUtil
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_checkout_bundling.*
import kotlinx.android.synthetic.main.activity_detail_tukar_tambah.*
import kotlinx.android.synthetic.main.activity_pengajuan_tukar_tambah.*
import kotlinx.android.synthetic.main.activity_tambah_saldo.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.DecimalFormat
import java.text.NumberFormat

class PengajuanTukarTambahActivity : AppCompatActivity() {
    var id = ""
    var uri = "http://192.168.0.104/ta/tugasakhir/public/product_images/"
    lateinit var sp: SharedPrefs
    var path:String = ""
    lateinit var rpu: RealPathUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengajuan_tukar_tambah)

        id = intent.getStringExtra("TT1_ID").toString()

        sp = SharedPrefs(this)

        sp.setAlamat(null)

        rpu = RealPathUtil()

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        detailProduct()

        btnFotoPengajuanTt.setOnClickListener {
            if(ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, 10)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
        }

        btnSendPengajuanTt.setOnClickListener {
            pengajuanTukarTambah()
        }

        btn_tambahAlamatX.setOnClickListener {
            val intent = Intent(this, ListAlamatActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        alamatUtama()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setCancelable(true)
                builder.setTitle("Batalkan Transaksi")
                builder.setMessage("Yakin untuk batal melakukan transaksi ?")
                builder.setPositiveButton("Batal",
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                        sp.setAlamat(null)
                        finish()
                    })
                builder.setNegativeButton("Lanjut",
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })

                val dialog: AlertDialog = builder.create()
                dialog.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("Batalkan Transaksi")
        builder.setMessage("Yakin untuk batal melakukan transaksi ?")
        builder.setPositiveButton("Batal",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                sp.setAlamat(null)
                super.onBackPressed()
            })
        builder.setNegativeButton("Lanjut",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 10 && resultCode == Activity.RESULT_OK) {
            val filePath: Uri = data!!.data!!
            path = rpu.getRealPath(this, filePath).toString()
            val bitmap: Bitmap = BitmapFactory.decodeFile(path)
            imageFotoPengajuanTt.setImageBitmap(bitmap)
        }
    }

    private fun detailProduct() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        ApiConfig.instanceRetrofit.productDetails(id).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    Picasso.get().load(uri+response.product.foto).into(imagePengajuanTt)
                    txtNamaPengajuanTt.text = response.product.nama
                    val formatter: NumberFormat = DecimalFormat("#,###")
                    val formattedNumber: String = formatter.format(response.product.harga)
                    txtHargaPengajuanTt.text = "Rp."+formattedNumber
                } else {
                    Toast.makeText(this@PengajuanTukarTambahActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@PengajuanTukarTambahActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun alamatUtama() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        if (sp.getAlamat() == null) {
            div_alamatX.visibility = View.GONE
            div_kosongX.visibility = View.VISIBLE
        } else {
            val alamat = sp.getAlamat()!!
            div_alamatX.visibility = View.VISIBLE
            div_kosongX.visibility = View.GONE
            tv_namaX.text = alamat.nama
            tv_phoneX.text = alamat.no_telp
            tv_alamatX.text =alamat.jalan + ", RT." + alamat.rt + "/RW." + alamat.rw + ", " + alamat.kecamatan + ", " + alamat.kab_kota + ", " + alamat.provinsi
        }
    }

    private fun pengajuanTukarTambah() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        if (txtNamaProdukBekas.text!!.isEmpty()) {
            txtNamaProdukBekas.error = "Nama produk tidak boleh kosong"
            txtNamaProdukBekas.requestFocus()
            return
        } else if (txtMerkProdukBekas.text!!.isEmpty()) {
            txtMerkProdukBekas.error = "Merk produk tidak boleh kosong"
            txtMerkProdukBekas.requestFocus()
            return
        } else if (txtDescProdukBekas.text!!.isEmpty()) {
            txtDescProdukBekas.error = "Deskripsi tidak boleh kosong"
            txtDescProdukBekas.requestFocus()
            return
        }

        val user = sp.getUser()!!

        if (sp.getAlamat() == null) {
            Toast.makeText(this@PengajuanTukarTambahActivity, "Pilih alamat terlebih dahulu !", Toast.LENGTH_SHORT).show()
            return
        } else {
            val alamat = sp.getAlamat()!!

            var file: File = File(path)
            var fileReq: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            var body: MultipartBody.Part = MultipartBody.Part.createFormData("foto", file.name, fileReq)

            var userEmail: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), user.email)
            var alamat_id: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), alamat.id.toString())
            var id_produk: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), id)
            var nama_produk: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), txtNamaProdukBekas.text.toString())
            var merk_produk: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), txtMerkProdukBekas.text.toString())
            var deskripsi_produk: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), txtDescProdukBekas.text.toString())

            ApiConfig.instanceRetrofit.pengajuanTukarTambah(userEmail, alamat_id, id_produk, nama_produk, merk_produk, deskripsi_produk, body).enqueue(object :
                Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    val response = response.body()!!
                    if(response.code == 200) {
                        val intent = Intent(this@PengajuanTukarTambahActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                        Toast.makeText(this@PengajuanTukarTambahActivity, "Berhasil melakukan pengajuan", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@PengajuanTukarTambahActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(this@PengajuanTukarTambahActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}