package com.example.homedeviceclient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.R
import com.example.homedeviceclient.model.BundlingItem
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.NumberFormat

class BundlingItemAdapter (val bitems: ArrayList<BundlingItem>): RecyclerView.Adapter<BundlingItemAdapter.BundlingItemViewHolder>()  {
    var uri = "http://192.168.0.104/ta/tugasakhir/public/product_images/"
    class BundlingItemViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val imgFoto: ImageView = v.findViewById(R.id.imgPfoto)
        val txtNama: TextView = v.findViewById(R.id.txtPnama)
        val txtHarga: TextView = v.findViewById(R.id.txtpHarga)
        val txtUnit: TextView = v.findViewById(R.id.txtpJumlah)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BundlingItemViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.transaksi_detail_card_layout, parent, false)
    )

    override fun onBindViewHolder(holder: BundlingItemViewHolder, position: Int) {
        val b = bitems[position]
        Picasso.get().load(uri+b.foto).into(holder.imgFoto)
        holder.txtNama.text = b.nama
        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(b.harga)
        holder.txtHarga.text = "Rp."+formattedNumber
        holder.txtUnit.text = b.jumlah.toString()+" unit"
    }

    override fun getItemCount(): Int {
        return bitems.size
    }
}