package com.example.homedeviceclient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.R
import com.example.homedeviceclient.model.LogWallet
import kotlinx.android.synthetic.main.log_wallet_card_layout.view.*
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.abs

class LogWalletAdapter(val logwallets: ArrayList<LogWallet>): RecyclerView.Adapter<LogWalletAdapter.LogWalletViewHolder>() {
    class LogWalletViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val imgIcon:ImageView = view.findViewById(R.id.imgIcon)
        val txtTipe:TextView = view.findViewById(R.id.txtTipe)
        val txtTanggal:TextView = view.findViewById(R.id.txtTanggal)
        val txtJumlah:TextView = view.findViewById(R.id.txtJumlah)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LogWalletViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.log_wallet_card_layout, parent, false)
    )

    override fun onBindViewHolder(holder: LogWalletViewHolder, position: Int) {
        val logwallet = logwallets[position]
        holder.txtTanggal.text = logwallet.tanggal
        if (logwallet.tipe == "CB") {
            holder.imgIcon.setImageResource(R.drawable.ic_coin)
            if (logwallet.jumlah < 0) {
                holder.txtTipe.text = "Pemakaian Poin"
                var kesbek = abs(logwallet.jumlah)
                val formatter: NumberFormat = DecimalFormat("#,###")
                val formattedNumber: String = formatter.format(kesbek)
                holder.txtJumlah.text = "-$formattedNumber"
            } else {
                holder.txtTipe.text = "Cashback"
                var kesbek = abs(logwallet.jumlah)
                val formatter: NumberFormat = DecimalFormat("#,###")
                val formattedNumber: String = formatter.format(kesbek)
                holder.txtJumlah.text = "+$formattedNumber"
            }
        } else {
            if (logwallet.jumlah < 0) {
                holder.imgIcon.setImageResource(R.drawable.ic_saldo)
                holder.txtTipe.text = "Pembayaran"
                var saldo = abs(logwallet.jumlah)
                val formatter: NumberFormat = DecimalFormat("#,###")
                val formattedNumber: String = formatter.format(saldo)
                holder.txtJumlah.text = "-$formattedNumber"
            } else {
                holder.imgIcon.setImageResource(R.drawable.ic_add_saldo)
                holder.txtTipe.text = "Top up"
                var saldo = abs(logwallet.jumlah)
                val formatter: NumberFormat = DecimalFormat("#,###")
                val formattedNumber: String = formatter.format(saldo)
                holder.txtJumlah.text = "+$formattedNumber"
            }
        }
    }

    override fun getItemCount(): Int {
        return logwallets.size
    }
}