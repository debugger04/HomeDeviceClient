package com.example.homedeviceclient.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.DetailAlamatActivity
import com.example.homedeviceclient.DetailKodePromoActivity
import com.example.homedeviceclient.R
import com.example.homedeviceclient.model.KodePromo

class KodeDimilikiAdapter(val kodes: ArrayList<KodePromo>): RecyclerView.Adapter<KodeDimilikiAdapter.KodeDimilikiViewHolder>() {
    class KodeDimilikiViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val imgKp:ImageView = v.findViewById(R.id.imgKp)
        val txtKode:TextView = v.findViewById(R.id.txtKodeP)
        val txtnama:TextView = v.findViewById(R.id.txtNamaKP)
        val txtTgl:TextView = v.findViewById(R.id.txtTanggalBerlaku)
        val btnDetil:Button = v.findViewById(R.id.btnDetailCode)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = KodeDimilikiAdapter.KodeDimilikiViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.kode_dimiliki_card_layout, parent, false)
    )

    override fun onBindViewHolder(holder: KodeDimilikiViewHolder, position: Int) {
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
    }

    override fun getItemCount(): Int {
        return kodes.size
    }
}