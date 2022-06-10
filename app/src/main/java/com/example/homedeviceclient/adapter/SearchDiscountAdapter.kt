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
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.NumberFormat

class SearchDiscountAdapter (val products: ArrayList<Product>): RecyclerView.Adapter<SearchDiscountAdapter.SearchDiscountViewHolder>() {
    var uri = "http://192.168.0.104/ta/tugasakhir/public/product_images/"
    class SearchDiscountViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val txtNama: TextView = v.findViewById(R.id.txtNamaProdukSearch)
        val txtHarga: TextView = v.findViewById(R.id.txtHargaProdukSearch)
        val imgProg: ImageView = v.findViewById(R.id.imgProductSearch)
        val btnDetail: Button = v.findViewById(R.id.btnDetailProdukSearch)
        val txtNilai: TextView = v.findViewById(R.id.txtNilaiSearch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchDiscountViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.product_discount_search_card_layout, parent, false)
    )

    override fun onBindViewHolder(holder: SearchDiscountViewHolder, position: Int) {
        val product = products[position]
        holder.txtNama.text = product.nama
        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(product.harga)
        holder.txtHarga.text = formattedNumber
        holder.txtNilai.text = product.nilai.toString()+"% off"
        Picasso.get().load(uri+product.foto).into(holder.imgProg)
//        holder.btnDetail.setOnClickListener {
//            val intent = Intent(holder.v.context, TransaksiDetailActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//            intent.putExtra("ORDER_ID", transaksi.order_id.toString())
//            holder.v.context.startActivity(intent)
//        }
    }

    override fun getItemCount(): Int {
        return products.size
    }
}