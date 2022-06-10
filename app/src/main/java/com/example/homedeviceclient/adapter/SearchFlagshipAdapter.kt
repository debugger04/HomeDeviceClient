package com.example.homedeviceclient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.R
import com.example.homedeviceclient.model.Product
import com.example.homedeviceclient.model.ProductLama
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.NumberFormat

class SearchFlagshipAdapter (val flagships: ArrayList<ProductLama>): RecyclerView.Adapter<SearchFlagshipAdapter.SearchFlagshipViewHolder>() {
    var uri = "http://192.168.0.104/ta/tugasakhir/public/flagship_images/"
    class SearchFlagshipViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val txtNama: TextView = v.findViewById(R.id.txtNamaProdukSearch)
        val txtHarga: TextView = v.findViewById(R.id.txtHargaProdukSearch)
        val imgProg: ImageView = v.findViewById(R.id.imgProductSearch)
        val btnDetail: Button = v.findViewById(R.id.btnDetailProdukSearch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchFlagshipViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.product_search_card_layout, parent, false)
    )

    override fun onBindViewHolder(holder: SearchFlagshipViewHolder, position: Int) {
        val flagship = flagships[position]
        holder.txtNama.text = flagship.nama
        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(flagship.harga_jual)
        holder.txtHarga.text = formattedNumber
        Picasso.get().load(uri+flagship.foto).into(holder.imgProg)
//        holder.btnDetail.setOnClickListener {
//            val intent = Intent(holder.v.context, TransaksiDetailActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//            intent.putExtra("ORDER_ID", transaksi.order_id.toString())
//            holder.v.context.startActivity(intent)
//        }
    }

    override fun getItemCount(): Int {
        return flagships.size
    }
}