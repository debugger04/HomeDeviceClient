package com.example.homedeviceclient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.R
import com.example.homedeviceclient.model.TransaksiDetail
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.NumberFormat

class TransaksiDetailAdapter(val transaksidetails: ArrayList<TransaksiDetail>): RecyclerView.Adapter<TransaksiDetailAdapter.TransaksiDetailViewHolder>() {
    var uri = "http://192.168.0.104/ta/tugasakhir/public/product_images/"
    class TransaksiDetailViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val imgFoto: ImageView = v.findViewById(R.id.imgPfoto)
        val txtNama: TextView = v.findViewById(R.id.txtPnama)
        val txtHarga: TextView = v.findViewById(R.id.txtpHarga)
        val txtJumlah: TextView = v.findViewById(R.id.txtpJumlah)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TransaksiDetailViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.transaksi_detail_card_layout, parent, false)
    )

    override fun onBindViewHolder(holder: TransaksiDetailViewHolder, position: Int) {
        val transaksidetail = transaksidetails[position]
        Picasso.get().load(uri+transaksidetail.pfoto).into(holder.imgFoto)
        holder.txtNama.text = transaksidetail.pnama
        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(transaksidetail.harga)
        holder.txtHarga.text ="Rp."+formattedNumber
        holder.txtJumlah.text = transaksidetail.jumlah.toString() + " unit"
    }

    override fun getItemCount(): Int {
        return transaksidetails.size
    }
}