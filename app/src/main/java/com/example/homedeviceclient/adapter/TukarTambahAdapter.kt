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
import com.example.homedeviceclient.TukarTambahDetailActivity
import com.example.homedeviceclient.model.TukarTambah

class TukarTambahAdapter (val tukarTambahs: ArrayList<TukarTambah>): RecyclerView.Adapter<TukarTambahAdapter.TukarTambahViewHolder>() {
    class TukarTambahViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val txtNoNota: TextView = v.findViewById(R.id.txtNomorNota)
        val txtTanggal: TextView = v.findViewById(R.id.txtTanggalTr)
        val txtStatus: TextView = v.findViewById(R.id.txtStatusTr)
        val btnDetail: Button = v.findViewById(R.id.btnDetailTr)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TukarTambahAdapter.TukarTambahViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.transaksi_card_layout, parent, false)
    )

    override fun onBindViewHolder(holder: TukarTambahAdapter.TukarTambahViewHolder, position: Int) {
        val tt = tukarTambahs[position]
        holder.txtNoNota.text = tt.nomor_nota
        holder.txtTanggal.text = tt.tanggal
        if (tt.status == 0) {
            holder.txtStatus.text = "Menunggu"
        } else if (tt.status == 1) {
            holder.txtStatus.text = "Diterima"
        } else if (tt.status == 2) {
            holder.txtStatus.text = "Ditolak"
        } else if (tt.status == 3) {
            holder.txtStatus.text = "Sudah dibayar"
        } else if (tt.status == 4) {
            holder.txtStatus.text = "Sudah dikirim"
        }
        holder.btnDetail.setOnClickListener {
            val intent = Intent(holder.v.context, TukarTambahDetailActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("ORDER_ID", tt.order_id.toString())
            holder.v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return tukarTambahs.size
    }
}