package com.example.homedeviceclient.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.R
import com.example.homedeviceclient.TransaksiDetailActivity
import com.example.homedeviceclient.model.Transaksi

class TransaksiAdapter (val transaksis: ArrayList<Transaksi>): RecyclerView.Adapter<TransaksiAdapter.TransaksiViewHolder>() {
    class TransaksiViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val txtNoNota:TextView = v.findViewById(R.id.txtNomorNota)
        val txtTanggal:TextView = v.findViewById(R.id.txtTanggalTr)
        val txtStatus:TextView = v.findViewById(R.id.txtStatusTr)
        val btnDetail:Button = v.findViewById(R.id.btnDetailTr)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TransaksiViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.transaksi_card_layout, parent, false)
    )

    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {
        val transaksi = transaksis[position]
        holder.txtNoNota.text = transaksi.nomor_nota
        holder.txtTanggal.text = transaksi.tanggal
        if (transaksi.status == 0) {
            holder.txtStatus.text = "Menunggu"
        } else if (transaksi.status == 1) {
            holder.txtStatus.text = "Telah dikirim"
        } else if (transaksi.status == 2) {
            holder.txtStatus.text = "Telah diterima"
        } else if (transaksi.status == 3) {
            holder.txtStatus.text = "Selesai"
        } else {
            holder.txtStatus.text = "Dibatalkan"
        }
        holder.btnDetail.setOnClickListener {
            val intent = Intent(holder.v.context, TransaksiDetailActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("ORDER_ID", transaksi.order_id.toString())
            holder.v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return transaksis.size
    }
}