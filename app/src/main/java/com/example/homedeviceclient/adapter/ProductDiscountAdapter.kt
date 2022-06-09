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
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.NumberFormat

class ProductDiscountAdapter(val prods: ArrayList<Product>): RecyclerView.Adapter<ProductDiscountAdapter.ProductDiscountViewHolder>() {
    var uri = "http://192.168.0.104/ta/tugasakhir/public/product_images/"
    class ProductDiscountViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val imgProd: ImageView = v.findViewById(R.id.imgView)
        val txtNama: TextView = v.findViewById(R.id.txtProductName)
        val txtHarga: TextView = v.findViewById(R.id.txtProductPrice)
        val btnDetail: ImageButton = v.findViewById(R.id.btnDetail)
        val btnAddTocart: Button = v.findViewById(R.id.btnAddToCart)
        val txtDiskon: TextView = v.findViewById(R.id.txtDiskon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductDiscountViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.product_discount_home_layout, parent, false)
    )

    override fun onBindViewHolder(holder: ProductDiscountViewHolder, position: Int) {
        val prod = prods[position]
        Picasso.get().load(uri+prod.foto).into(holder.imgProd)
        holder.txtNama.text = prod.nama
        val final = prod.harga - (prod.harga * prod.nilai / 100)
        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(final)
        holder.txtHarga.text = "Rp."+formattedNumber
        holder.txtDiskon.text = prod.nilai.toString()+"%"
    }

    override fun getItemCount(): Int {
        return prods.size
    }
}