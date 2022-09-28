package com.example.homedeviceclient

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homedeviceclient.adapter.TransaksiDetailAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.example.homedeviceclient.model.TransaksiDetail
import kotlinx.android.synthetic.main.activity_bayar_tukar_tambah.*
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_checkout.checkPoin
import kotlinx.android.synthetic.main.list_promo_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class CheckoutActivity : AppCompatActivity() {
    var listTdetail: ArrayList<TransaksiDetail> = ArrayList()
    lateinit var sp: SharedPrefs
    var cekKoin = 0
    val biaya = 10000
    var totalBelanja = 0
    var totalPromo = 0
    var totalKode = 0
    var totalBayar = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        sp = SharedPrefs(this)

        sp.setAlamat(null)
        sp.setPromo(null)
        sp.setTipePromo(null)
        sp.setVoucher(null)

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        getWallet()
        cartList()
        hitungTotal()
        alamatUtama()
        promoUtama()
        hitungPromo()
        checkCode()
        hitungCode(totalBelanja)
        hitungBayar()

        tv_ongkir.text = "Rp.10,000"

        val c = Calendar.getInstance()
        System.out.println("Current time => " + c.time)

        val df = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate = df.format(c.time)

        txtTglCk.text = formattedDate

        btn_tambahAlamat.setOnClickListener {
            val intent = Intent(this, ListAlamatActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        btn_promo.setOnClickListener {
            val intent = Intent(this, ListPromoActivity::class.java)
            intent.putExtra("total", totalBelanja)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        btn_pilih_kode.setOnClickListener {
            if (txtkodeproms.text!!.isEmpty()) {
                val intent = Intent(this, ListKodeVoucherActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                findCode()
            }
        }

        checkPoin.setOnClickListener {
            if (checkPoin.isChecked) {
                cekKoin = 1
            } else {
                cekKoin = 0
            }
        }

        btnBayar.setOnClickListener {
            bayar()
        }
    }

    override fun onResume() {
        super.onResume()
        alamatUtama()
        promoUtama()
        hitungPromo()
        checkCode()
        hitungCode(totalBelanja)
        hitungBayar()
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
                sp.setPromo(null)
                sp.setTipePromo(null)
                sp.setVoucher(null)
                super.onBackPressed()
            })
        builder.setNegativeButton("Lanjut",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        val dialog: AlertDialog = builder.create()
        dialog.show()
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
                        sp.setPromo(null)
                        sp.setTipePromo(null)
                        sp.setVoucher(null)
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

    private fun getWallet() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        ApiConfig.instanceRetrofit.getWallet(user.email).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    val formatter: NumberFormat = DecimalFormat("#,###")
                    val formattedNumber: String = formatter.format(response.wallet.saldo)
                    val formattedNumber2: String = formatter.format(response.wallet.poin)

                    txtSaldo4.text = "Rp. "+formattedNumber
                    txtPoin4.text = formattedNumber2
                } else {
                    Toast.makeText(this@CheckoutActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@CheckoutActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cartList() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        ApiConfig.instanceRetrofit.checkoutCart(user.email).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    listTdetail = response.detailtransaksi
                    updateList()
                } else {
                    Toast.makeText(this@CheckoutActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@CheckoutActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateList() {
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        cekoutView.setLayoutManager(layout)
        cekoutView.setHasFixedSize(true)
        cekoutView.adapter = TransaksiDetailAdapter(listTdetail)
    }

    fun hitungTotal() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        ApiConfig.instanceRetrofit.totalCart(user.email).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    val formatter: NumberFormat = DecimalFormat("#,###")
                    val formattedNumber: String = formatter.format(response.total)
                    tv_totalBelanja.text = "Rp."+formattedNumber
                    totalBelanja = response.total
                    hitungBayar()
                } else {
                    Toast.makeText(this@CheckoutActivity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@CheckoutActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
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
            div_alamat.visibility = View.GONE
            div_kosong.visibility = View.VISIBLE
        } else {
            val alamat = sp.getAlamat()!!
            div_alamat.visibility = View.VISIBLE
            div_kosong.visibility = View.GONE
            tv_nama.text = alamat.nama
            tv_phone.text = alamat.no_telp
            tv_alamat.text =alamat.jalan + ", RT." + alamat.rt + "/RW." + alamat.rw + ", " + alamat.kecamatan + ", " + alamat.kab_kota + ", " + alamat.provinsi
        }
    }

    private fun promoUtama() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        if (sp.getPromo() == null) {
            div_promo.visibility = View.GONE
        } else {
            val promo = sp.getPromo()!!
            var tipe = ""
            div_promo.visibility = View.VISIBLE
            if (promo.tipe == "CB") {
                tipe = "Cashback"
            } else {
                tipe = "Discount"
            }
            div_promo.text = promo.nama+" "+tipe+" "+promo.nilai+"%"
        }
    }

    private fun hitungPromo() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!
        if (sp.getPromo() == null) {
            div_promo_harga.visibility = View.GONE
            vv.visibility = View.GONE
        } else {
            val promo = sp.getPromo()!!
            val typ = sp.getTipePromo()!!
            Log.d("EMAIL", promo.id.toString())
            ApiConfig.instanceRetrofit.hitungPromo(user.email, promo.id.toString(), typ.toString()).enqueue(object :
                Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    val response = response.body()!!
                    if(response.code == 200) {
                        val formatter: NumberFormat = DecimalFormat("#,###")
                        val formattedNumber: String = formatter.format(response.total)
                        div_promo_harga.visibility = View.VISIBLE
                        vv.visibility = View.VISIBLE
                        if (promo.tipe == "DC") {
                            tv_promo.text = "- Rp."+formattedNumber
                        } else {
                            tv_promo.text = "+"+formattedNumber+" Koin"
                        }
                        totalPromo = response.total
                    } else {
                        Toast.makeText(this@CheckoutActivity, response.msg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(this@CheckoutActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun findCode() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        ApiConfig.instanceRetrofit.findCode(user.email, txtkodeproms!!.text.toString()).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    txtKetPr.visibility = View.VISIBLE
                    txtKetPr.text = response.msg
                    txtKetPr.setTextColor(Color.parseColor("#71FF22"))
                    sp.setVoucher(response.kode)
                } else {
                    txtKetPr.visibility = View.VISIBLE
                    txtKetPr.text = response.msg
                    txtKetPr.setTextColor(Color.parseColor("#FF4922"))
                    sp.setVoucher(null)
                }
                checkCode()
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@CheckoutActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun checkCode() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        if (sp.getVoucher() == null) {

        } else {
            val vc = sp.getVoucher()!!
            txtKetPr.visibility = View.VISIBLE
            txtKetPr.text = "Kode dapat digunakan. Selamat menikmati promo!"
            txtKetPr.setTextColor(Color.parseColor("#71FF22"))
            txtkodeproms.setText(vc.kode)
        }
        hitungCode(totalBelanja)
    }

    private fun hitungCode(total:Int): Int {
        var hitungan = 0
        if (sp.getVoucher() == null) {
            div_code_harga.visibility = View.GONE
            va.visibility = View.GONE
        } else {
            div_code_harga.visibility = View.VISIBLE
            va.visibility = View.VISIBLE
            val vc = sp.getVoucher()!!
            if ((totalBelanja * vc.nilai)/100 > vc.maximum_cut) {
                hitungan = vc.maximum_cut
            } else {
                hitungan = (total * vc.nilai)/100
            }
            val formatter: NumberFormat = DecimalFormat("#,###")
            val formattedNumber: String = formatter.format(hitungan)
            if (vc.efek == "DC") {
                tv_code.text = "- Rp."+formattedNumber
            } else {
                tv_code.text = "+"+formattedNumber+" Koin"
            }
            totalKode = hitungan
        }
        return hitungan
    }

    private fun hitungBayar() {
        if (sp.getVoucher() == null) {
            if (sp.getPromo() == null) {
                totalBayar = totalBelanja + biaya
            } else {
                val pr = sp.getPromo()!!
                if (pr.tipe == "DC") {
                    totalBayar = totalBelanja - totalPromo + biaya
                } else {
                    totalBayar = totalBelanja + biaya
                }
            }
        } else {
            val vc = sp.getVoucher()!!
            if (sp.getPromo() == null) {
                if (vc.efek == "DC") {
                    totalBayar = totalBelanja - totalKode + biaya
                } else {
                    totalBayar = totalBelanja + biaya
                }
            } else {
                val pr = sp.getPromo()!!
                if (vc.efek == "DC") {
                    if (pr.tipe == "DC") {
                        totalBayar = totalBelanja - totalKode - totalPromo + biaya
                    } else {
                        totalBayar = totalBelanja - totalKode + biaya
                    }
                } else {
                    if (pr.tipe == "DC") {
                        totalBayar = totalBelanja - totalPromo + biaya
                    } else {
                        totalBayar = totalBelanja + biaya
                    }
                }
            }
        }
        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(totalBayar)
        tv_total.text = "Rp."+formattedNumber
    }

    private fun bayar() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        if (sp.getAlamat() == null) {
            Toast.makeText(this@CheckoutActivity, "Pilih alamat terlebih dahulu !", Toast.LENGTH_SHORT).show()
            return
        } else {
            val alamat = sp.getAlamat()!!

            var kode_id = 0
            var tipe_kode = ""
            var nilai_kode = 0

            var promo_id = 0
            var tipe_promo = 0
            var use_promo = ""

            if (sp.getVoucher() == null) {
                kode_id = 0
                tipe_kode = ""
                nilai_kode = 0
            } else {
                val vc = sp.getVoucher()!!
                kode_id = vc.id
                tipe_kode = vc.efek
                nilai_kode = totalKode
            }

            if (sp.getPromo() == null) {
                promo_id = 0
                tipe_promo = 2
            } else {
                val pr = sp.getPromo()!!
                val ty = sp.getTipePromo()!!
                promo_id = pr.id
                tipe_promo = ty
                use_promo = pr.use_for
            }

            ApiConfig.instanceRetrofit.paid(user.email, cekKoin.toString(), alamat.id.toString(), totalBayar.toString(), kode_id.toString(), tipe_kode, nilai_kode.toString(), promo_id.toString(), tipe_promo.toString(), use_promo).enqueue(object :
                Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    val response = response.body()!!
                    if(response.code == 200) {
                        val intent = Intent(this@CheckoutActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                        Toast.makeText(this@CheckoutActivity, response.msg, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@CheckoutActivity, response.msg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(this@CheckoutActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}