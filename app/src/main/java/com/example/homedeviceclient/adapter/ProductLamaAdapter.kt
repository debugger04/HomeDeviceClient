package com.example.homedeviceclient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.R
import com.example.homedeviceclient.model.Product
import com.example.homedeviceclient.model.ProductLama
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.NumberFormat

class ProductLamaAdapter(val prods: ArrayList<ProductLama>): RecyclerView.Adapter<ProductLamaAdapter.ProductLamaViewHolder>() {
    var uri = "http://192.168.0.104/ta/tugasakhir/public/flagship_images/"
    class ProductLamaViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val imgProd: ImageView = v.findViewById(R.id.imgView)
        val txtNama: TextView = v.findViewById(R.id.txtProductName)
        val txtHarga: TextView = v.findViewById(R.id.txtProductPrice)
        val btnDetail: ImageButton = v.findViewById(R.id.btnDetail)
        val btnAddTocart: Button = v.findViewById(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductLamaViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.product_home_card_layout, parent, false)
    )

    override fun onBindViewHolder(holder: ProductLamaViewHolder, position: Int) {
        val prod = prods[position]
        Picasso.get().load(uri+prod.foto).into(holder.imgProd)
        holder.txtNama.text = prod.nama
        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(prod.harga_jual)
        holder.txtHarga.text = "Rp."+formattedNumber
    }

    override fun getItemCount(): Int {
        return prods.size
    }
}