package com.example.homedeviceclient.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.DetailAlamatActivity
import com.example.homedeviceclient.OngoingDetailActivity
import com.example.homedeviceclient.R
import com.example.homedeviceclient.model.WalletRequest
import java.text.DecimalFormat
import java.text.NumberFormat

class OngoingSaldoAdapter(val walletreqs: ArrayList<WalletRequest>): RecyclerView.Adapter<OngoingSaldoAdapter.OngoingSaldoViewHolder>() {
    class OngoingSaldoViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val txtTgl:TextView = v.findViewById(R.id.txtTanggals)
        val txtJumlah:TextView = v.findViewById(R.id.txtJumlahs)
        val txtStatus:TextView = v.findViewById(R.id.txtStatus)
        val btnDetail:Button = v.findViewById(R.id.btnDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OngoingSaldoAdapter.OngoingSaldoViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.ongoing_saldo_card_layout, parent, false)
    )

    override fun onBindViewHolder(holder: OngoingSaldoAdapter.OngoingSaldoViewHolder, position: Int) {
        val walletreq = walletreqs[position]
        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(walletreq.jumlah)
        holder.txtJumlah.text = formattedNumber
        holder.txtTgl.text = walletreq.tanggal
        if (walletreq.status == 0) {
            holder.txtStatus.text = "Menunggu persetujuan"
            holder.txtStatus.setTextColor(Color.parseColor("#B5B5B5"))
        } else if (walletreq.status == 1) {
            holder.txtStatus.text = "Berhasil"
            holder.txtStatus.setTextColor(Color.parseColor("#1AFF00"))
        } else {
            holder.txtStatus.text = "Ditolak"
            holder.txtStatus.setTextColor(Color.parseColor("#FA4932"))
        }
        holder.btnDetail.setOnClickListener {
            val intent = Intent(holder.v.context, OngoingDetailActivity::class.java)
            intent.putExtra("WQ_ID", walletreq.id.toString())
            holder.v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return walletreqs.size
    }
}