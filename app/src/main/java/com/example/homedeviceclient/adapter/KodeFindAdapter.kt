package com.example.homedeviceclient.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.AlamatActivity
import com.example.homedeviceclient.DetailKodePromoActivity
import com.example.homedeviceclient.KodePromoActivity
import com.example.homedeviceclient.R
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.example.homedeviceclient.model.KodePromo
import kotlinx.android.synthetic.main.activity_search_kode_promo.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KodeFindAdapter (val kodes: ArrayList<KodePromo>): RecyclerView.Adapter<KodeFindAdapter.KodeFindViewHolder>() {
    lateinit var sp: SharedPrefs
    class KodeFindViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val imgKp: ImageView = v.findViewById(R.id.imgKp)
        val txtKode: TextView = v.findViewById(R.id.txtKodeP)
        val txtnama: TextView = v.findViewById(R.id.txtNamaKP)
        val txtTgl: TextView = v.findViewById(R.id.txtTanggalBerlaku)
        val btnDetil: TextView = v.findViewById(R.id.btnDetailKp)
        val btnKlaim: Button = v.findViewById(R.id.btnKlaimKp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = KodeFindAdapter.KodeFindViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.kode_search_card_layout, parent, false)
    )

    override fun onBindViewHolder(holder: KodeFindAdapter.KodeFindViewHolder, position: Int) {
        val kode = kodes[position]
        holder.txtKode.text = kode.kode
        holder.txtnama.text = kode.nama
        holder.txtTgl.text = kode.masa_berlaku
        if (kode.efek == "DC") {
            holder.imgKp.setImageResource(R.drawable.ic_saldo)
        } else {
            holder.imgKp.setImageResource(R.drawable.ic_coin)
        }
        holder.btnDetil.setOnClickListener {
            val intent = Intent(holder.v.context, DetailKodePromoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("KP_ID", kode.id.toString())
            holder.v.context.startActivity(intent)
        }
        holder.btnKlaim.setOnClickListener {
            sp = SharedPrefs(holder.v.context as Activity)
            val user = sp.getUser()!!
            ApiConfig.instanceRetrofit.claimKodePromo(user.email, kode.id.toString()).enqueue(object :
                Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    val response = response.body()!!
                    if(response.code == 200) {
                        val intent = Intent(holder.v.context, KodePromoActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        (holder.v.context as Activity).finish()
                        holder.v.context.startActivity(intent)
                        if(response.msg == "OK") {
                            Toast.makeText(holder.v.context, "Telah berhasil klaim kode promo!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(holder.v.context, response.msg, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(holder.v.context, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(holder.v.context, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return kodes.size
    }
}