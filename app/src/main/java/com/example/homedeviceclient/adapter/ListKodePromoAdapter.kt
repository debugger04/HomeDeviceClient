package com.example.homedeviceclient.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.DetailKodePromoActivity
import com.example.homedeviceclient.R
import com.example.homedeviceclient.model.KodePromo
import java.text.DecimalFormat
import java.text.NumberFormat

class ListKodePromoAdapter(val kodes: ArrayList<KodePromo>, var id_kode: Int, var listener: ListKodePromoAdapter.Listeners): RecyclerView.Adapter<ListKodePromoAdapter.ListKodePromoViewHolder>() {
    class ListKodePromoViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val txtNama: TextView = v.findViewById(R.id.tv_kodepromo)
        val txtMaxCut: TextView = v.findViewById(R.id.tv_maxCut)
        val txtMinSpend: TextView = v.findViewById(R.id.tv_minSpend)
        val txtMasa: TextView = v.findViewById(R.id.tv_berlaku)
        val rd: RadioButton = v.findViewById(R.id.rd_alamat)
        val layout: CardView = v.findViewById(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ListKodePromoAdapter.ListKodePromoViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_kode_layout, parent, false)
    )

    override fun onBindViewHolder(holder: ListKodePromoViewHolder, position: Int) {
        val kode = kodes[position]
        holder.txtNama.text = kode.kode
        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(kode.maximum_cut)
        val formattedNumber1: String = formatter.format(kode.minimum_spend)
        if (kode.efek == "DC") {
            holder.txtMaxCut.text = "Discount "+kode.nilai+"% dengan maksimal Rp."+formattedNumber
        } else {
            holder.txtMaxCut.text = "Cashback "+kode.nilai+"% dengan maksimal "+formattedNumber+" Koin"
        }
        holder.txtMinSpend.text = "Minimal belanja Rp."+formattedNumber1
        holder.txtMasa.text = "Berlaku Hingga "+kode.masa_berlaku
        holder.layout.setOnClickListener {
            listener.onClicked(kode)
        }
        holder.rd.setOnClickListener {
            listener.onClicked(kode)
        }
        if (kode.id == id_kode) {
            holder.rd.isChecked = true
        }
    }

    override fun getItemCount(): Int {
        return kodes.size
    }

    interface Listeners{
        fun onClicked(data: KodePromo)
    }
}